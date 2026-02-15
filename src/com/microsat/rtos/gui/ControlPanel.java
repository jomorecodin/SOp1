package com.microsat.rtos.gui;

import com.microsat.rtos.core.PCB;
import com.microsat.rtos.core.Scheduler;
import com.microsat.rtos.core.algorithms.*;
import com.microsat.rtos.datastructures.CustomLinkedList;
import com.microsat.rtos.io.ProcessLoader;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ControlPanel extends JPanel {



    private final Scheduler scheduler;

    private final JButton startPauseButton;

    private final JComboBox<String> algorithmComboBox;

    private static final Random random = new Random();

    private static int nextProcessId = 1;



    private static final Color PANEL_BACKGROUND = new Color(21, 25, 28);

    private static final Color TEXT_COLOR = new Color(0, 255, 200);

    private static final Color BORDER_COLOR = new Color(50, 180, 150);

    private static final Color EMERGENCY_COLOR = new Color(255, 60, 60);



    public ControlPanel(Scheduler scheduler) {

        this.scheduler = scheduler;

        

        setBackground(PANEL_BACKGROUND);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(

            BorderFactory.createLineBorder(BORDER_COLOR, 1), "Control del Sistema");

        titledBorder.setTitleColor(TEXT_COLOR);

        setBorder(BorderFactory.createCompoundBorder(

            BorderFactory.createEmptyBorder(5, 5, 5, 5),

            titledBorder

        ));

        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));



        // --- Botones y Controles ---

        startPauseButton = new JButton("Iniciar Simulación");

        configureComponent(startPauseButton);

        startPauseButton.addActionListener(e -> toggleSimulation());



        JButton addProcessesButton = new JButton("Generar 20");

        configureComponent(addProcessesButton);

        addProcessesButton.addActionListener(e -> generateProcesses());



        JButton loadButton = new JButton("Cargar Archivo...");

        configureComponent(loadButton);

        loadButton.addActionListener(e -> loadProcessesFromFile());



        JButton emergencyButton = new JButton("! EMERGENCIA !");

        configureComponent(emergencyButton);

        emergencyButton.setForeground(EMERGENCY_COLOR.brighter());

        emergencyButton.setBorder(BorderFactory.createLineBorder(EMERGENCY_COLOR));

        emergencyButton.addActionListener(e -> createEmergencyProcess());



        String[] algorithms = {"FCFS", "Round Robin", "SRT", "Priority", "EDF"};

        algorithmComboBox = new JComboBox<>(algorithms);

        configureComponent(algorithmComboBox);

        algorithmComboBox.addActionListener(e -> switchAlgorithm());



        JSlider speedSlider = new JSlider(0, 200, 100);

        TitledBorder sliderBorder = BorderFactory.createTitledBorder("Velocidad de Reloj (Más Rápido ->)");

        sliderBorder.setTitleColor(TEXT_COLOR.darker());

        speedSlider.setBorder(sliderBorder);

        configureComponent(speedSlider);

        speedSlider.addChangeListener(e -> scheduler.setClockSpeed(200 - speedSlider.getValue()));



        add(startPauseButton);

        add(addProcessesButton);

        add(loadButton);

        add(emergencyButton);

        add(algorithmComboBox);

        add(speedSlider);

    }

    

    private void configureComponent(JComponent component) {

        component.setBackground(PANEL_BACKGROUND.brighter());

        component.setForeground(TEXT_COLOR);

        component.setFont(new Font("Monospaced", Font.BOLD, 12));

        if (component instanceof JButton) {

            ((JButton) component).setBorder(BorderFactory.createCompoundBorder(

                BorderFactory.createLineBorder(BORDER_COLOR),

                BorderFactory.createEmptyBorder(4, 8, 4, 8)

            ));

        }

    }



    private void toggleSimulation() {

        if (scheduler.isPaused()) {

            scheduler.startSimulation();

            startPauseButton.setText("Pausar Sim");

        } else {

            scheduler.pauseSimulation();

            startPauseButton.setText("Reanudar Sim");

        }

    }



    private void generateProcesses() {

        for (int i = 0; i < 20; i++) {

            int totalTicks = random.nextInt(50) + 10;

            int deadline = totalTicks + random.nextInt(100) + 20;

            int priority = random.nextInt(10);

            PCB pcb = new PCB(nextProcessId, "Proc-" + nextProcessId, priority, deadline, totalTicks);

            scheduler.addProcess(pcb);

            nextProcessId++;

        }

    }



    private void loadProcessesFromFile() {

        JFileChooser fileChooser = new JFileChooser(".");

        fileChooser.setDialogTitle("Seleccionar Archivo de Procesos");

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            File selectedFile = fileChooser.getSelectedFile();

            try {

                CustomLinkedList<PCB> loadedPcbList = ProcessLoader.loadFromFile(selectedFile);

                for (int i = 0; i < loadedPcbList.size(); i++) {

                    scheduler.addProcess(loadedPcbList.get(i));

                }

                JOptionPane.showMessageDialog(this,

                    loadedPcbList.size() + " procesos cargados exitosamente.",

                    "Archivo Cargado", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {

                JOptionPane.showMessageDialog(this,

                    "Error cargando archivo: " + e.getMessage(),

                    "Error de Carga", JOptionPane.ERROR_MESSAGE);

            }

        }

    }



    private void createEmergencyProcess() {

        int totalTicks = 15;

        int deadline = 20;

        int priority = 0; // Máxima prioridad

        PCB pcb = new PCB(nextProcessId, "EMERGENCIA-" + nextProcessId, priority, deadline, totalTicks);

        scheduler.addProcess(pcb);

        nextProcessId++;

    }



    private void switchAlgorithm() {

        String selected = (String) algorithmComboBox.getSelectedItem();

        if (selected == null) return;



        SchedulingAlgorithm newAlgorithm;

        switch (selected) {

            case "Round Robin": newAlgorithm = new RoundRobinAlgorithm(); break;

            case "SRT": newAlgorithm = new SRTAlgorithm(); break;

            case "Priority": newAlgorithm = new PriorityAlgorithm(); break;

            case "EDF": newAlgorithm = new EDFAlgorithm(); break;

            case "FCFS": default: newAlgorithm = new FCFSAlgorithm(); break;

        }

        scheduler.setAlgorithm(newAlgorithm);

    }

}
