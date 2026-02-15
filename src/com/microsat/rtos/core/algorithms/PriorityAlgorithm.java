package com.microsat.rtos.core.algorithms;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.datastructures.CustomQueue;

/**
 * Implementación de un algoritmo de planificación por prioridad estática.
 * Es expropiativo. Se asume que un valor de prioridad MENOR es MÁS prioritario.
 */
public class PriorityAlgorithm implements SchedulingAlgorithm {

    @Override
    public PCB getNextProcess(CustomQueue<PCB> readyQueue) {
        if (readyQueue.isEmpty()) {
            return null;
        }

        // Se busca en toda la cola el proceso con la mayor prioridad (menor valor).
        CustomQueue<PCB> tempQueue = new CustomQueue<>();
        PCB bestProcess = readyQueue.dequeue();
        tempQueue.enqueue(bestProcess);

        while (!readyQueue.isEmpty()) {
            PCB current = readyQueue.dequeue();
            if (current.getPriority() < bestProcess.getPriority()) {
                bestProcess = current;
            }
            tempQueue.enqueue(current);
        }

        // Se reconstruye la cola original sin el proceso seleccionado.
        PCB result = bestProcess;
        while (!tempQueue.isEmpty()) {
            PCB p = tempQueue.dequeue();
            if (p != result) {
                readyQueue.enqueue(p);
            }
        }
        return result;
    }

    @Override
    public boolean shouldPreempt(PCB currentProcess, PCB newProcess) {
        if (currentProcess == null) return false;
        // Se interrumpe si el nuevo proceso tiene una prioridad mayor (valor más bajo).
        return newProcess.getPriority() < currentProcess.getPriority();
    }
}
