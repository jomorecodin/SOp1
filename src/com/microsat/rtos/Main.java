package com.microsat.rtos;

import com.microsat.rtos.gui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada principal para el Simulador RTOS.
 */
public class Main {
    public static void main(String[] args) {
        // Asegurarse de que la GUI se cree y se muestre en el Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // Intentar establecer un Look and Feel más moderno para una mejor apariencia de los componentes
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception e) {
                // Si Nimbus no está disponible, el L&F por defecto del sistema será utilizado.
                System.err.println("Nimbus Look and Feel no encontrado, usando el predeterminado.");
            }
            
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
