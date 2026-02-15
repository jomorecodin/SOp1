package com.microsat.rtos.core.algorithms;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.datastructures.CustomQueue;

/**
 * Interfaz para el patrón Strategy, define un algoritmo de planificación de CPU.
 */
public interface SchedulingAlgorithm {

    /**
     * Selecciona el siguiente proceso a ejecutar de la cola de listos.
     * La implementación de este método define la política de planificación (ej. SRT, Prioridad).
     * @param readyQueue La cola de procesos en estado LISTO.
     * @return El PCB del proceso seleccionado para ejecución.
     */
    PCB getNextProcess(CustomQueue<PCB> readyQueue);

    /**
     * Determina si el proceso actualmente en ejecución debe ser expropiado (preempted)
     * por un nuevo proceso que acaba de pasar al estado LISTO.
     * @param currentProcess El proceso actualmente en la CPU. Puede ser null.
     * @param newProcess El nuevo proceso que ha entrado en la cola de listos.
     * @return true si el proceso actual debe ser interrumpido en favor del nuevo.
     */
    boolean shouldPreempt(PCB currentProcess, PCB newProcess);
}
