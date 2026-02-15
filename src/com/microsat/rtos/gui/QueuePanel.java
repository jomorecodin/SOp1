package com.microsat.rtos.gui;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.datastructures.CustomQueue;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Un panel reutilizable para visualizar el contenido de una CustomQueue<PCB>.
 */
public class QueuePanel extends JPanel {
    private final CustomQueue<PCB> pcbQueue;
    private final JPanel contentPanel;
    private final TitledBorder titledBorder;

    private static final Color PANEL_BACKGROUND = new Color(21, 25, 28);
    private static final Color TEXT_COLOR = new Color(0, 255, 200);
    private static final Color BORDER_COLOR = new Color(50, 180, 150);

    public QueuePanel(String title, CustomQueue<PCB> pcbQueue) {
        this.pcbQueue = pcbQueue;
        
        setBackground(PANEL_BACKGROUND);
        setLayout(new BorderLayout());
        
        titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1), title);
        titledBorder.setTitleColor(TEXT_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            titledBorder
        ));

        contentPanel = new JPanel();
        contentPanel.setBackground(PANEL_BACKGROUND);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getViewport().setBackground(PANEL_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Refresca la visualización de la cola. Debe ser llamado desde el EDT.
     */
    public void refresh() {
        contentPanel.removeAll();
        titledBorder.setTitle(getTitleBase() + " (" + pcbQueue.size() + ")");

        if (pcbQueue.isEmpty()) {
            JLabel emptyLabel = new JLabel("  - EMPTY -");
            emptyLabel.setForeground(TEXT_COLOR.darker());
            contentPanel.add(emptyLabel);
        } else {
            // Iterar de forma no destructiva
            CustomQueue<PCB> tempQueue = new CustomQueue<>();
            while (!pcbQueue.isEmpty()) {
                PCB pcb = pcbQueue.dequeue();
                contentPanel.add(createPcbLabel(pcb));
                tempQueue.enqueue(pcb);
            }
            // Restaurar la cola original
            while (!tempQueue.isEmpty()) {
                pcbQueue.enqueue(tempQueue.dequeue());
            }
        }
        
        revalidate();
        repaint();
    }

    private String getTitleBase() {
        String currentTitle = titledBorder.getTitle();
        int countIndex = currentTitle.lastIndexOf(" (");
        return (countIndex != -1) ? currentTitle.substring(0, countIndex) : currentTitle;
    }

    private Component createPcbLabel(PCB pcb) {
        String text = String.format("  ID: %-3d | Pri: %-3d | PC: %d/%d",
                pcb.getProcessId(), pcb.getPriority(), pcb.getProgramCounter(), pcb.getTotalExecutionTicks());
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Monospaced", Font.PLAIN, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}
