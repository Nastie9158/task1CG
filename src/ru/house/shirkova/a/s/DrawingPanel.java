package ru.house.shirkova.a.s;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawingPanel extends JPanel implements ActionListener, MouseListener {
    private final int PANEL_WIDTH = 1400;
    private final int PANEL_HEIGHT = 900;

    private final Timer animationTimer;
    private final Random random;

    private final Background background;
    private final RainSystem rainSystem;
    private final CloudSystem cloudSystem;
    private final LightningSystem lightningSystem;
    private final List<Skyscraper> skyscrapers;

    public DrawingPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);

        random = new Random();

        background = new Background(PANEL_WIDTH, PANEL_HEIGHT);
        rainSystem = new RainSystem(PANEL_WIDTH, PANEL_HEIGHT, 600);
        cloudSystem = new CloudSystem(PANEL_WIDTH, PANEL_HEIGHT, 7);
        lightningSystem = new LightningSystem(PANEL_WIDTH, PANEL_HEIGHT);
        skyscrapers = new ArrayList<>();

        initializeSkyscrapers();

        animationTimer = new Timer(16, this);
        animationTimer.start();

        addMouseListener(this);
    }

    private void initializeSkyscrapers() {
        int[] heights = {600, 550, 650, 500, 580};
        int[] widths = {180, 200, 160, 220, 190};
        int startY = 225;

        for (int i = 0; i < heights.length; i++) {
            int x = 200 + i * 240;
            skyscrapers.add(new Skyscraper(x, startY, widths[i], heights[i]));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        background.draw(g2d);
        lightningSystem.drawLightningEffect(g2d);

        cloudSystem.draw(g2d);

        for (Skyscraper skyscraper : skyscrapers) {
            skyscraper.draw(g2d);
        }

        rainSystem.draw(g2d);
        lightningSystem.draw(g2d);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cloudSystem.update();
        rainSystem.update();
        lightningSystem.update();

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        CloudSystem.Cloud nearestCloud = cloudSystem.getNearestCloud(e.getX(), e.getY());
        if (nearestCloud != null) {
            lightningSystem.triggerLightningFromCloud(nearestCloud, PANEL_HEIGHT - 50);
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}