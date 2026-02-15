package com.microsat.rtos.core;

/**
 * Process Control Block (PCB).
 * Almacena toda la información de contexto de un proceso.
 */
public class PCB {

    private final int processId;
    private final String processName;
    private ProcessState state;
    private int programCounter;
    private int memoryAddressRegister;
    private int priority;
        private int deadlineTicks;
        private final int totalExecutionTicks;
        private final int initialDeadlineTicks;
    
        /**
         * Constructor para un nuevo PCB.
         *
         * @param processId ID único del proceso.
         * @param processName Nombre descriptivo del proceso.
         * @param priority Prioridad de ejecución.
         * @param deadlineTicks Ticks de reloj restantes para cumplir el deadline.
         * @param totalExecutionTicks Ticks totales de CPU que el proceso necesita para completarse.
         */
        public PCB(int processId, String processName, int priority, int deadlineTicks, int totalExecutionTicks) {
            this.processId = processId;
            this.processName = processName;
            this.state = ProcessState.NUEVO; // Todos los procesos inician en NUEVO
            this.programCounter = 0; // Se inicializa en 0
            this.memoryAddressRegister = 0; // Puede cambiar según la gestión de memoria
            this.priority = priority;
            this.deadlineTicks = deadlineTicks;
            this.totalExecutionTicks = totalExecutionTicks;
            this.initialDeadlineTicks = deadlineTicks; // Guardar el valor inicial
        }
    
        // --- Getters y Setters ---
    
        public int getProcessId() {
            return processId;
        }
    
        public String getProcessName() {
            return processName;
        }
    
        public ProcessState getState() {
            return state;
        }
    
        public void setState(ProcessState state) {
            this.state = state;
        }
    
        public int getProgramCounter() {
            return programCounter;
        }
    
        public void setProgramCounter(int programCounter) {
            this.programCounter = programCounter;
        }
    
        public int getMemoryAddressRegister() {
            return memoryAddressRegister;
        }
    
        public void setMemoryAddressRegister(int memoryAddressRegister) {
            this.memoryAddressRegister = memoryAddressRegister;
        }
    
        public int getPriority() {
            return priority;
        }
    
        public void setPriority(int priority) {
            this.priority = priority;
        }
    
        public int getDeadlineTicks() {
            return deadlineTicks;
        }
    
        public void setDeadlineTicks(int deadlineTicks) {
            this.deadlineTicks = deadlineTicks;
        }
    
        public int getTotalExecutionTicks() {
            return totalExecutionTicks;
        }
    
        public int getInitialDeadlineTicks() {
            return initialDeadlineTicks;
        }
    
        @Override
        public String toString() {
            return "PCB{" +
                   "id=" + processId +
               ", name='" + processName + "'" +
               ", state=" + state +
               ", priority=" + priority +
               ", pc=" + programCounter +
               '}';
    }
}
