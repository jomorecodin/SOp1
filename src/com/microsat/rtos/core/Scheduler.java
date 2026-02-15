package com.microsat.rtos.core;

import com.microsat.rtos.core.algorithms.FCFSAlgorithm;
import com.microsat.rtos.core.algorithms.RoundRobinAlgorithm;
import com.microsat.rtos.core.algorithms.SchedulingAlgorithm;
import com.microsat.rtos.datastructures.CustomQueue;
import com.microsat.rtos.metrics.MetricsManager;

/**
 * El núcleo del sistema operativo. Implementa la lógica de planificación y el reloj del sistema.
 * Se ejecuta en su propio hilo.
 */
public class Scheduler implements Runnable {

    // Colas de gestión de procesos
    private final CustomQueue<PCB> newQueue;
    private final CustomQueue<PCB> readyQueue;
    private final CustomQueue<PCB> blockedQueue;
    private final CustomQueue<PCB> readySuspendedQueue;
    private final CustomQueue<PCB> blockedSuspendedQueue;
    private final CustomQueue<PCB> terminatedQueue;

    private volatile PCB runningProcess;
    private final MemoryManager memoryManager;
    private final MetricsManager metricsManager;

    private volatile boolean simulationRunning = false;
    private volatile boolean paused = true; // La simulación comienza pausada
    private volatile long clockSpeedMs;
    private SchedulingAlgorithm currentAlgorithm;
    private int quantumCounter;

    /**
     * Constructor del Scheduler. Inicializa todas las colas y componentes.
     */
    public Scheduler() {
        this.newQueue = new CustomQueue<>();
        this.readyQueue = new CustomQueue<>();
        this.blockedQueue = new CustomQueue<>();
        this.readySuspendedQueue = new CustomQueue<>();
        this.blockedSuspendedQueue = new CustomQueue<>();
        this.terminatedQueue = new CustomQueue<>();
        this.memoryManager = new MemoryManager();
        this.metricsManager = MetricsManager.getInstance();
        this.runningProcess = null;
        this.clockSpeedMs = SystemConfig.CLOCK_SPEED_MS;
        // Por defecto, iniciamos con FCFS
        this.currentAlgorithm = new FCFSAlgorithm();
    }

    @Override
    public void run() {
        simulationRunning = true;
        while (simulationRunning) {
            try {
                // Bucle de pausa
                while (paused && simulationRunning) {
                    Thread.sleep(200); // No consumir CPU mientras está en pausa
                }
                if (!simulationRunning) break;

                Thread.sleep(clockSpeedMs);
                tick();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                simulationRunning = false;
            }
        }
    }

    /**
     * El corazón del scheduler. Se ejecuta con cada pulso del reloj.
     */
    private synchronized void tick() {
        if (paused) return; // No hacer nada si se pausa justo después de despertar
        
        metricsManager.recordTick(runningProcess != null);

        // 1. Admitir nuevos procesos
        while (!newQueue.isEmpty()) {
            PCB newPcb = newQueue.dequeue();
            admitToReady(newPcb);
        }

        // 2. Gestionar el proceso en ejecución
        if (runningProcess != null) {
            runningProcess.setProgramCounter(runningProcess.getProgramCounter() + 1);
            runningProcess.setDeadlineTicks(runningProcess.getDeadlineTicks() - 1);

            // Verificar si el proceso ha terminado
            if (runningProcess.getProgramCounter() >= runningProcess.getTotalExecutionTicks()) {
                terminateProcess(runningProcess);
                runningProcess = null;
            } else if (currentAlgorithm instanceof RoundRobinAlgorithm) {
                // Manejo del quantum para Round Robin
                quantumCounter--;
                if (quantumCounter <= 0) {
                    runningProcess.setState(ProcessState.LISTO);
                    readyQueue.enqueue(runningProcess);
                    runningProcess = null;
                }
            }
        }

        // 3. Si la CPU está libre, despachar el siguiente proceso
        if (runningProcess == null) {
            dispatch();
        }
    }

    /**
     * Mueve un proceso a la cola de listos o listos-suspendidos y comprueba si debe expropiar al actual.
     */
    private void admitToReady(PCB pcb) {
        boolean wasMemoryFull = memoryManager.isMemoryFull(readyQueue, blockedQueue);
        
        memoryManager.admitProcess(pcb, readyQueue, blockedQueue, readySuspendedQueue);

        // Solo se puede expropiar si el proceso nuevo entró a RAM y hay un proceso en ejecución
        if (!wasMemoryFull && runningProcess != null) {
            if (currentAlgorithm.shouldPreempt(runningProcess, pcb)) {
                runningProcess.setState(ProcessState.LISTO);
                readyQueue.enqueue(runningProcess); // El proceso expropiado vuelve a la cola de listos
                runningProcess = null;
            }
        }
    }
    
    /**
     * Mueve un proceso a la cola de terminados.
     */
    private void terminateProcess(PCB pcb) {
        pcb.setState(ProcessState.TERMINADO);
        terminatedQueue.enqueue(pcb);
        metricsManager.recordProcessCompletion(pcb);
    }

    /**
     * Asigna un proceso de la cola de listos a la CPU usando el algoritmo actual.
     */
    private void dispatch() {
        if (!readyQueue.isEmpty()) {
            runningProcess = currentAlgorithm.getNextProcess(readyQueue);
            if (runningProcess != null) {
                runningProcess.setState(ProcessState.EJECUCION);
                // Reiniciar el contador de quantum cada vez que un proceso entra a la CPU
                this.quantumCounter = SystemConfig.DEFAULT_QUANTUM;
            }
        }
    }

    // --- Métodos de control y acceso ---

    /**
     * Cambia dinámicamente el algoritmo de planificación.
     * @param algorithm El nuevo algoritmo a utilizar.
     */
    public synchronized void setAlgorithm(SchedulingAlgorithm algorithm) {
        this.currentAlgorithm = algorithm;
        // Si hay un proceso en ejecución, podría ser necesario re-evaluar la decisión
        // Por simplicidad, el cambio aplicará en el siguiente dispatch.
    }

    public synchronized void addProcess(PCB pcb) {
        newQueue.enqueue(pcb);
    }
    
    public void startSimulation() {
        this.paused = false;
    }
    
    public void pauseSimulation() {
        this.paused = true;
    }

    public void stopSimulation() {
        this.paused = true;
        this.simulationRunning = false;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setClockSpeed(long milliseconds) {
        this.clockSpeedMs = milliseconds;
    }

    public synchronized SchedulingAlgorithm getCurrentAlgorithm() {
        return currentAlgorithm;
    }

    public synchronized PCB getRunningProcess() {
        return runningProcess;
    }

    public synchronized CustomQueue<PCB> getReadyQueue() {
        return readyQueue;
    }

    public synchronized CustomQueue<PCB> getBlockedQueue() {
        return blockedQueue;
    }
    
    public synchronized CustomQueue<PCB> getNewQueue() {
        return newQueue;
    }

    public synchronized CustomQueue<PCB> getReadySuspendedQueue() {
        return readySuspendedQueue;
    }



    public synchronized CustomQueue<PCB> getBlockedSuspendedQueue() {
        return blockedSuspendedQueue;
    }

    public synchronized CustomQueue<PCB> getTerminatedQueue() {
        return terminatedQueue;
    }
}