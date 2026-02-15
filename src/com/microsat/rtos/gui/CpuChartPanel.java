package com.microsat.rtos.gui;

import com.microsat.rtos.datastructures.CustomLinkedList;
import com.microsat.rtos.metrics.MetricsManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel que dibuja una gráfica del uso de la CPU a lo largo del tiempo.
 */
public class CpuChartPanel extends JPanel {

    private static final Color PANEL_BACKGROUND = new Color(21, 25, 28);
    private static final Color TEXT_COLOR = new Color(0, 255, 200);
    private static final Color BORDER_COLOR = new Color(50, 180, 150);
    private static final Color GRID_COLOR = new Color(40, 90, 80);
    private static final Color LINE_COLOR = new Color(10, 255, 150);

    public CpuChartPanel() {
        setBackground(PANEL_BACKGROUND);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1), "Historial de Uso de CPU");
        titledBorder.setTitleColor(TEXT_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                titledBorder
        ));
        setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 10;
        int graphHeight = height - 2 * padding;

        drawGrid(g2d, width, height, padding);

        CustomLinkedList<Boolean> history = MetricsManager.getInstance().getCpuUsageHistory();
        if (history.size() < 2) {
            return;
        }

        g2d.setColor(LINE_COLOR);
        g2d.setStroke(new BasicStroke(2));

        int maxPoints = Math.min(history.size(), 480); // Dibuja los últimos N puntos
        int startIdx = Math.max(0, history.size() - maxPoints);

        for (int i = 1; i < maxPoints; i++) {
            int idx1 = startIdx + i - 1;
            int idx2 = startIdx + i;
            
            int x1 = (int) ((double) (i - 1) / (maxPoints - 1) * (width - 2 * padding)) + padding;
            int y1 = history.get(idx1) ? padding : padding + graphHeight;
            
            int x2 = (int) ((double) i / (maxPoints - 1) * (width - 2 * padding)) + padding;
            int y2 = history.get(idx2) ? padding : padding + graphHeight;

            g2d.drawLine(x1, y1, x2, y2);
        }
    }
    
    private void drawGrid(Graphics2D g2d, int width, int height, int padding) {
        g2d.setColor(GRID_COLOR);
        // Líneas verticales
        for (int i = 0; i < width; i += 20) {
            g2d.drawLine(i, 0, i, height);
        }
        // Líneas horizontales
        for (int i = 0; i < height; i += 20) {
            g2d.drawLine(0, i, width, i);
        }
    }
    
    public void refresh() {
        repaint();
    }
}
