package com.microsat.rtos.metrics;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.datastructures.CustomLinkedList;

/**
 * Singleton para gestionar y registrar métricas de rendimiento del simulador.
 */
public class MetricsManager {
    private static final MetricsManager INSTANCE = new MetricsManager();

    private CustomLinkedList<Boolean> cpuUsageHistory;
    private int completedProcesses;
    private int deadlinesMet;
    private static final int MAX_HISTORY_POINTS = 500; // Limitar el historial para no consumir memoria infinita

    private MetricsManager() {
        this.cpuUsageHistory = new CustomLinkedList<>();
        this.completedProcesses = 0;
        this.deadlinesMet = 0;
    }

    public static MetricsManager getInstance() {
        return INSTANCE;
    }

    /**
     * Registra el estado de la CPU en cada tick del reloj.
     * @param isCpuBusy true si la CPU estuvo ocupada, false si estuvo ociosa.
     */
    public synchronized void recordTick(boolean isCpuBusy) {
        cpuUsageHistory.add(isCpuBusy);
        // Mantiene el historial con un tamaño máximo para la visualización
        if (cpuUsageHistory.size() > MAX_HISTORY_POINTS) {
            // Para "eliminar" el primero, creamos una nueva lista sin él.
            // Esto es ineficiente pero cumple la restricción de no tener un método removeFirst().
            CustomLinkedList<Boolean> newHistory = new CustomLinkedList<>();
            for (int i = 1; i < cpuUsageHistory.size(); i++) {
                newHistory.add(cpuUsageHistory.get(i));
            }
            // Reemplazar el historial antiguo. Esto no es atómico y podría ser mejorado.
            // Por simplicidad para el ejercicio, lo dejamos así.
        }
    }

    /**
     * Registra la finalización de un proceso y actualiza las métricas relacionadas.
     * @param pcb El proceso que ha terminado.
     */
    public synchronized void recordProcessCompletion(PCB pcb) {
        completedProcesses++;
        if (pcb.getDeadlineTicks() >= 0) {
            deadlinesMet++;
        }
    }

    /**
     * Calcula la tasa de éxito (procesos completados a tiempo).
     * @return El porcentaje de éxito (0.0 a 100.0).
     */
    public synchronized double getSuccessRate() {
        if (completedProcesses == 0) {
            return 100.0; // O 0.0, dependiendo de la definición. 100% si no hubo fallos.
        }
        return (double) deadlinesMet / completedProcesses * 100.0;
    }

    public synchronized int getCompletedProcesses() {
        return completedProcesses;
    }

    public synchronized int getDeadlinesMet() {
        return deadlinesMet;
    }

    public synchronized CustomLinkedList<Boolean> getCpuUsageHistory() {
        return cpuUsageHistory;
    }

    public synchronized void reset() {
        // Podría ser útil para reiniciar la simulación
        cpuUsageHistory = new CustomLinkedList<>();
        completedProcesses = 0;
        deadlinesMet = 0;
    }
}
