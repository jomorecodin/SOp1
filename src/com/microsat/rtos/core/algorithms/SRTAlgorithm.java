package com.microsat.rtos.core.algorithms;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.datastructures.CustomQueue;

/**
 * Implementación del algoritmo Shortest Remaining Time (SRT).
 * Es la versión expropiativa de SJF.
 */
public class SRTAlgorithm implements SchedulingAlgorithm {

    @Override
    public PCB getNextProcess(CustomQueue<PCB> readyQueue) {
        if (readyQueue.isEmpty()) {
            return null;
        }

        // Se busca en toda la cola el proceso con el menor tiempo restante.
        CustomQueue<PCB> tempQueue = new CustomQueue<>();
        PCB bestProcess = readyQueue.dequeue();
        tempQueue.enqueue(bestProcess);

        while (!readyQueue.isEmpty()) {
            PCB current = readyQueue.dequeue();
            if (getRemainingTime(current) < getRemainingTime(bestProcess)) {
                bestProcess = current;
            }
            tempQueue.enqueue(current);
        }

        // Se reconstruye la cola original sin el proceso seleccionado.
        PCB result = bestProcess;
        while (!tempQueue.isEmpty()) {
            PCB p = tempQueue.dequeue();
            if (p != result) { // Comparación por referencia de objeto
                readyQueue.enqueue(p);
            }
        }
        return result;
    }

    @Override
    public boolean shouldPreempt(PCB currentProcess, PCB newProcess) {
        if (currentProcess == null) return false;
        // Se interrumpe si el nuevo proceso tiene un tiempo restante menor que el actual.
        return getRemainingTime(newProcess) < getRemainingTime(currentProcess);
    }

    private int getRemainingTime(PCB pcb) {
        return pcb.getTotalExecutionTicks() - pcb.getProgramCounter();
    }
}
