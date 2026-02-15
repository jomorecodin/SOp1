package com.microsat.rtos.core;

/**
 * Clase final para almacenar constantes de configuración global del sistema.
 * No es instanciable.
 */
public final class SystemConfig {

    /**
     * Máximo número de procesos que pueden residir en "RAM" (colas Ready y Blocked) simultáneamente.
     */
    public static final int MAX_MEMORY_PROCESSES = 5;

    /**
     * Duración del quantum para algoritmos de planificación como Round Robin (en ticks de reloj).
     * Se define aquí aunque la lógica se implemente más adelante.
     */
    public static final int DEFAULT_QUANTUM = 10;

    /**
     * Velocidad del reloj del sistema en milisegundos.
     * Corresponde al delay del hilo del Scheduler entre ticks.
     */
    public static final long CLOCK_SPEED_MS = 100; // 10 ticks por segundo

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     */
    private SystemConfig() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no puede ser instanciada.");
    }
}
