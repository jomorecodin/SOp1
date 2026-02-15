package com.microsat.rtos.core;

import com.microsat.rtos.datastructures.CustomQueue;

/**
 * Gestiona la admisión de procesos en la memoria principal (RAM simulada)
 * y la lógica de swapping hacia memoria secundaria (disco simulado).
 */
public class MemoryManager {

    /**
     * Verifica si la memoria principal (representada por las colas de listos y bloqueados) está llena.
     * @param readyQueue La cola de procesos listos en RAM.
     * @param blockedQueue La cola de procesos bloqueados en RAM.
     * @return true si el número de procesos en RAM ha alcanzado el límite.
     */
    public boolean isMemoryFull(CustomQueue<PCB> readyQueue, CustomQueue<PCB> blockedQueue) {
        return (readyQueue.size() + blockedQueue.size()) >= SystemConfig.MAX_MEMORY_PROCESSES;
    }

    /**
     * Intenta admitir un nuevo proceso. Si hay espacio en memoria, lo mueve a la cola de Listos.
     * Si no, lo mueve a la cola de Listos-Suspendidos.
     * @param pcb El proceso a admitir.
     * @param readyQueue La cola de procesos listos en RAM.
     * @param blockedQueue La cola de procesos bloqueados en RAM.
     * @param readySuspendedQueue La cola de procesos listos-suspendidos en Disco.
     */
    public void admitProcess(PCB pcb, CustomQueue<PCB> readyQueue, CustomQueue<PCB> blockedQueue, CustomQueue<PCB> readySuspendedQueue) {
        if (!isMemoryFull(readyQueue, blockedQueue)) {
            pcb.setState(ProcessState.LISTO);
            readyQueue.enqueue(pcb);
        } else {
            pcb.setState(ProcessState.LISTO_SUSPENDIDO);
            readySuspendedQueue.enqueue(pcb);
        }
    }

    // En el futuro, aquí se podrían implementar métodos más complejos para swapping.
    // Por ejemplo, un método que elija un proceso de la cola de listos para suspender
    // y así liberar espacio para un proceso de mayor prioridad que está en LISTO_SUSPENDIDO.
}
