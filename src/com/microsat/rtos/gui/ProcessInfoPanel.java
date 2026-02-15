package com.microsat.rtos.gui;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.core.Scheduler;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel que muestra información detallada del proceso en ejecución (CPU).
 */
public class ProcessInfoPanel extends JPanel {
    private final Scheduler scheduler;
    private final JLabel idLabel = new JLabel("ID: --");
    private final JLabel stateLabel = new JLabel("State: --");
    private final JLabel pcLabel = new JLabel("PC: --");
    private final JLabel marLabel = new JLabel("MAR: --");
    private final JLabel deadlineLabel = new JLabel("Deadline Ticks: --");
    private final JProgressBar executionProgressBar = new JProgressBar();
    private final JProgressBar deadlineProgressBar = new JProgressBar();

    private static final Color PANEL_BACKGROUND = new Color(21, 25, 28);
    private static final Color TEXT_COLOR = new Color(0, 255, 200);
    private static final Color BORDER_COLOR = new Color(50, 180, 150);
    private static final Color PROGRESS_BAR_COLOR = new Color(255, 80, 80);

    public ProcessInfoPanel(Scheduler scheduler) {
        this.scheduler = scheduler;
        setupPanel();
        setupComponents();
    }

    private void setupPanel() {
        setBackground(PANEL_BACKGROUND);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 2), "CPU - Proceso en Ejecución");
        titledBorder.setTitleColor(TEXT_COLOR.brighter());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                titledBorder
        ));
        setLayout(new GridLayout(0, 1, 5, 5));
    }

    private void setupComponents() {
        Font font = new Font("Monospaced", Font.BOLD, 14);
        idLabel.setFont(font);
        stateLabel.setFont(font);
        pcLabel.setFont(font);
        marLabel.setFont(font);
        deadlineLabel.setFont(font);

        idLabel.setForeground(TEXT_COLOR);
        stateLabel.setForeground(TEXT_COLOR);
        pcLabel.setForeground(TEXT_COLOR);
        marLabel.setForeground(TEXT_COLOR);
        deadlineLabel.setForeground(TEXT_COLOR);

        configureProgressBar(executionProgressBar, "Ejecución");
        configureProgressBar(deadlineProgressBar, "Deadline");

        add(idLabel);
        add(stateLabel);
        add(pcLabel);
        add(marLabel);
        add(deadlineLabel);
        add(executionProgressBar);
        add(deadlineProgressBar);
    }

    private void configureProgressBar(JProgressBar progressBar, String title) {
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Monospaced", Font.PLAIN, 12));
        progressBar.setForeground(PROGRESS_BAR_COLOR);
        progressBar.setBackground(PANEL_BACKGROUND.brighter());
        progressBar.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        progressBar.setString(title + ": --%");
    }

    public void refresh() {
        PCB pcb = scheduler.getRunningProcess();
        if (pcb != null) {
            idLabel.setText("ID: " + pcb.getProcessId());
            stateLabel.setText("Estado: " + pcb.getState());
            pcLabel.setText("PC: " + pcb.getProgramCounter());
            marLabel.setText("MAR: " + pcb.getMemoryAddressRegister());
            deadlineLabel.setText("Ticks para Deadline: " + pcb.getDeadlineTicks());
            
            updateProgressBar(executionProgressBar, "Ejecución", pcb.getProgramCounter(), pcb.getTotalExecutionTicks());
            updateProgressBar(deadlineProgressBar, "Deadline", pcb.getInitialDeadlineTicks() - pcb.getDeadlineTicks(), pcb.getInitialDeadlineTicks());

        } else {
            idLabel.setText("ID: --");
            stateLabel.setText("Estado: CPU OCIOSA");
            pcLabel.setText("PC: --");
            marLabel.setText("MAR: --");
            deadlineLabel.setText("Ticks para Deadline: --");
            updateProgressBar(executionProgressBar, "Ejecución", 0, 1);
            updateProgressBar(deadlineProgressBar, "Deadline", 0, 1);
        }
    }

    private void updateProgressBar(JProgressBar progressBar, String title, int value, int max) {
        if (max > 0) {
            progressBar.setMaximum(max);
            progressBar.setValue(value);
            int percentage = (int) (100.0 * value / max);
            progressBar.setString(title + ": " + percentage + "%");
        } else {
            progressBar.setValue(0);
            progressBar.setString(title + ": N/A");
        }
    }
}
