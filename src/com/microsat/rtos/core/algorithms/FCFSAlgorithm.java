package com.microsat.rtos.core.algorithms;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.datastructures.CustomQueue;

/**
 * Implementación del algoritmo First-Come, First-Served (FCFS).
 * No es expropiativo.
 */
public class FCFSAlgorithm implements SchedulingAlgorithm {

    @Override
    public PCB getNextProcess(CustomQueue<PCB> readyQueue) {
        // El siguiente proceso es simplemente el que está al frente de la cola.
        return readyQueue.dequeue();
    }

    @Override
    public boolean shouldPreempt(PCB currentProcess, PCB newProcess) {
        // FCFS no es expropiativo, nunca interrumpe un proceso en ejecución.
        return false;
    }
}
