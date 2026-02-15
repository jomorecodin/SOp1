package com.microsat.rtos.core.algorithms;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.datastructures.CustomQueue;

/**
 * Implementación del algoritmo Round Robin.
 * No es expropiativo en el sentido de que un nuevo proceso interrumpa al actual,
 * pero el Scheduler lo manejará como expropiativo por tiempo (quantum).
 */
public class RoundRobinAlgorithm implements SchedulingAlgorithm {

    @Override
    public PCB getNextProcess(CustomQueue<PCB> readyQueue) {
        // Al igual que FCFS, simplemente toma el siguiente de la cola.
        // La magia del Round Robin la gestiona el Scheduler con el quantum.
        return readyQueue.dequeue();
    }

    @Override
    public boolean shouldPreempt(PCB currentProcess, PCB newProcess) {
        // Un nuevo proceso no interrumpe al actual en Round Robin.
        // La interrupción solo ocurre por fin de quantum.
        return false;
    }
}
