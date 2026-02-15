package com.microsat.rtos.core;

/**
 * Enumera los posibles estados de un proceso en el RTOS.
 * Basado en el modelo de 7 estados.
 */
public enum ProcessState {
    /**
     * El proceso ha sido creado pero aún no ha sido admitido en la cola de listos.
     */
    NUEVO,

    /**
     * El proceso está esperando para ser asignado a un procesador.
     */
    LISTO,

    /**
     * Las instrucciones del proceso están siendo ejecutadas.
     */
    EJECUCION,

    /**
     * El proceso está esperando a que ocurra algún evento (p. ej., E/S).
     */
    BLOQUEADO,

    /**
     * El proceso ha finalizado su ejecución.
     */
    TERMINADO,

    /**
     * Un proceso que estaba listo es suspendido y movido a memoria secundaria.
     */
    LISTO_SUSPENDIDO,

    /**
     * Un proceso que estaba bloqueado es suspendido y movido a memoria secundaria.
     */
    BLOQUEADO_SUSPENDIDO
}
