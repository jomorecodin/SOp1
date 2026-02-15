package com.microsat.rtos.io;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.datastructures.CustomLinkedList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase de utilidad para cargar definiciones de procesos desde un archivo.
 */
public class ProcessLoader {

    /**
     * Carga una lista de procesos desde un archivo. Detecta el formato (CSV) por extensión.
     * @param file El archivo a cargar.
     * @return Una lista enlazada de PCBs.
     * @throws IOException Si ocurre un error de lectura o el formato no es soportado.
     */
    public static CustomLinkedList<PCB> loadFromFile(File file) throws IOException {
        String name = file.getName();
        if (name.toLowerCase().endsWith(".csv")) {
            return loadFromCSV(file);
        } else {
            // En el futuro se podría implementar JSON aquí.
            throw new IOException("Formato de archivo no soportado: " + name);
        }
    }

    /**
     * Implementación específica para cargar desde un archivo CSV.
     * Formato: ID,Nombre,Instrucciones,Prioridad,CiclosParaDeadline
     * @param file Archivo CSV.
     * @return Lista de PCBs.
     * @throws IOException Si hay un error de I/O.
     */
    private static CustomLinkedList<PCB> loadFromCSV(File file) throws IOException {
        CustomLinkedList<PCB> pcbList = new CustomLinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Omitir encabezado si existe
            // reader.readLine(); 
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue; // Ignorar líneas vacías o comentarios
                }
                String[] parts = line.split(",");
                if (parts.length != 5) {
                    System.err.println("Línea mal formada en CSV, se ignora: " + line);
                    continue;
                }
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    int totalTicks = Integer.parseInt(parts[2].trim());
                    int priority = Integer.parseInt(parts[3].trim());
                    int deadline = Integer.parseInt(parts[4].trim());
                    
                    pcbList.add(new PCB(id, name, priority, deadline, totalTicks));
                } catch (NumberFormatException e) {
                    System.err.println("Error de formato numérico en línea, se ignora: " + line);
                }
            }
        }
        return pcbList;
    }
}
