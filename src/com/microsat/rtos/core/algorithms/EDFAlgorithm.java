package com.microsat.rtos.core.algorithms;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.datastructures.CustomQueue;

/**
 * Implementación del algoritmo Earliest Deadline First (EDF).
 * Es expropiativo y dinámico.
 */
public class EDFAlgorithm implements SchedulingAlgorithm {

    @Override
    public PCB getNextProcess(CustomQueue<PCB> readyQueue) {
        if (readyQueue.isEmpty()) {
            return null;
        }

        // Se busca en toda la cola el proceso con el deadline más cercano.
        CustomQueue<PCB> tempQueue = new CustomQueue<>();
        PCB bestProcess = readyQueue.dequeue();
        tempQueue.enqueue(bestProcess);

        while (!readyQueue.isEmpty()) {
            PCB current = readyQueue.dequeue();
            if (current.getDeadlineTicks() < bestProcess.getDeadlineTicks()) {
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
        // Se interrumpe si el nuevo proceso tiene un deadline más cercano (urgente).
        return newProcess.getDeadlineTicks() < currentProcess.getDeadlineTicks();
    }
}
