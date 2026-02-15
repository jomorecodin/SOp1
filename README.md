# Microsatellite RTOS Simulator

Un simulador de un Sistema Operativo de Tiempo Real (RTOS) para un microsatélite, desarrollado en Java 21. Este proyecto se enfoca en la gestión de procesos, planificación de CPU y asignación de memoria, implementando estructuras de datos y algoritmos desde cero, sin usar las Colecciones de Java estándar.

## Integrantes
*   Joseph Moreno

## Features

*   **GUI Mission Control:** Interfaz gráfica desarrollada con Swing con una estética de "panel de control", mostrando el estado del sistema en tiempo real.
*   **Planificación Dinámica:** Soporte para 5 algoritmos de planificación intercambiables en tiempo de ejecución:
    *   FCFS, Round Robin, SRT, Prioridad Estática y EDF.
*   **Gestión de Memoria:** Simulación de memoria principal y secundaria (disco) con lógica de swapping.
*   **Estructuras de Datos Propias:** Implementación de Colas y Listas Enlazadas personalizadas, cumpliendo la restricción de no usar `java.util.*` collections.
*   **Métricas y Visualización:** Recolección de métricas de rendimiento (uso de CPU, tasa de éxito de deadlines) y una gráfica en tiempo real del uso de la CPU.
*   **Carga de Procesos:** Capacidad para cargar lotes de procesos desde archivos `.csv`.
*   **Controles Interactivos:** Generación de procesos aleatorios, interrupciones de emergencia y control sobre la velocidad de la simulación.

## Algoritmos Implementados

1.  **First-Come, First-Served (FCFS):** El algoritmo más simple. Los procesos se ejecutan en el orden en que llegan. No expropiativo.
2.  **Round Robin (RR):** Un algoritmo expropiativo basado en un quantum de tiempo. Cada proceso se ejecuta por un pequeño lapso antes de ser devuelto a la cola de listos, garantizando que ningún proceso monopolice la CPU.
3.  **Shortest Remaining Time (SRT):** Algoritmo expropiativo que siempre selecciona el proceso al que le queda menos tiempo de ejecución. Óptimo para minimizar el tiempo de espera promedio.
4.  **Prioridad Estática (Priority):** Algoritmo expropiativo donde a cada proceso se le asigna una prioridad fija. La CPU siempre se le otorga al proceso con mayor prioridad (valor numérico más bajo).
5.  **Earliest Deadline First (EDF):** Algoritmo expropiativo y dinámico que da la mayor prioridad al proceso cuyo deadline está más cerca. Es óptimo para cumplir la mayor cantidad de deadlines.

## Cómo Compilar y Ejecutar

Este proyecto no requiere dependencias externas y está construido con el JDK 21.

### 1. Compilación
Abre una terminal en la raíz del proyecto y ejecuta el siguiente comando. Esto compilará todos los archivos `.java` y los dejará en un directorio `out/`.

```bash
# Para Windows
javac -d out @(Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })

# Para macOS/Linux
javac -d out $(find . -name "*.java")
```

### 2. Ejecución
Una vez compilado, ejecuta la clase `Main` desde el directorio raíz.

```bash
# Para Windows o macOS/Linux
java -cp out com.microsat.rtos.Main
```
La aplicación se iniciará, mostrando la interfaz gráfica del simulador.