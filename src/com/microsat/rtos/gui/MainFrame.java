package com.microsat.rtos.gui;

import com.microsat.rtos.core.Scheduler;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final Scheduler scheduler;
    private final Thread schedulerThread;

    private final CpuChartPanel cpuChartPanel;
    private final QueuePanel newQueuePanel;
    private final QueuePanel readyQueuePanel;
    private final QueuePanel blockedQueuePanel;
    private final QueuePanel readySuspendedQueuePanel;
    private final QueuePanel blockedSuspendedQueuePanel;
    private final QueuePanel terminatedQueuePanel;
    private final ProcessInfoPanel processInfoPanel;
    private final ControlPanel controlPanel;

    private static final Color FRAME_BACKGROUND = new Color(10, 12, 14);

    public MainFrame() {
        // 1. Inicializar el núcleo
        scheduler = new Scheduler();
        schedulerThread = new Thread(scheduler, "RTOS-Scheduler-Thread");

        // 2. Configurar la ventana principal (JFrame)
        setTitle("Simulador RTOS para Microsatélite - Control de Misión");
        setSize(1280, 800); // Aumentar altura para el gráfico
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(FRAME_BACKGROUND);

        // 3. Crear los paneles de la UI
        cpuChartPanel = new CpuChartPanel();
        processInfoPanel = new ProcessInfoPanel(scheduler);
        controlPanel = new ControlPanel(scheduler);

        newQueuePanel = new QueuePanel("Nuevos", scheduler.getNewQueue());
        readyQueuePanel = new QueuePanel("Listos (RAM)", scheduler.getReadyQueue());
        blockedQueuePanel = new QueuePanel("Bloqueados (RAM)", scheduler.getBlockedQueue());
        readySuspendedQueuePanel = new QueuePanel("Listos-Suspendidos (Disco)", scheduler.getReadySuspendedQueue());
        blockedSuspendedQueuePanel = new QueuePanel("Bloqueados-Suspendidos (Disco)", scheduler.getBlockedSuspendedQueue());
        terminatedQueuePanel = new QueuePanel("Terminados", scheduler.getTerminatedQueue());
        
        // 4. Organizar los paneles en el layout
        setupLayout();
        
        // 5. Iniciar el hilo del scheduler
        schedulerThread.start();

        // 6. Iniciar el temporizador de refresco de la UI
        Timer uiRefreshTimer = new Timer(100, e -> refreshUI());
        uiRefreshTimer.start();
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Panel de gráfico de CPU en la parte superior
        add(cpuChartPanel, BorderLayout.NORTH);

        // Panel de control en la parte inferior
        add(controlPanel, BorderLayout.SOUTH);

        // Panel de información de la CPU a la derecha
        add(processInfoPanel, BorderLayout.EAST);

        // Panel central para todas las colas
        JPanel queuesContainer = new JPanel(new GridLayout(0, 1, 5, 5));
        queuesContainer.setBackground(FRAME_BACKGROUND);
        queuesContainer.add(newQueuePanel);
        queuesContainer.add(readyQueuePanel);
        queuesContainer.add(blockedQueuePanel);
        queuesContainer.add(readySuspendedQueuePanel);
        queuesContainer.add(blockedSuspendedQueuePanel);
        queuesContainer.add(terminatedQueuePanel);
        
        // Para dar un poco de padding
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(FRAME_BACKGROUND);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerWrapper.add(queuesContainer, BorderLayout.CENTER);
        
        add(centerWrapper, BorderLayout.CENTER);
    }
    
    private void refreshUI() {
        cpuChartPanel.refresh();
        newQueuePanel.refresh();
        readyQueuePanel.refresh();
        blockedQueuePanel.refresh();
        readySuspendedQueuePanel.refresh();
        blockedSuspendedQueuePanel.refresh();
        terminatedQueuePanel.refresh();
        processInfoPanel.refresh();
    }

}