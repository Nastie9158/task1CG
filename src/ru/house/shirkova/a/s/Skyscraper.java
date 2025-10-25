package ru.house.shirkova.a.s;

import java.awt.*;
import java.util.Random;

public class Skyscraper {
    private final int x, y, width, height;
    private final Random random;
    private final Color[] buildingColors;
    private final Color windowColor;
    private long lastWindowUpdate;
    private boolean[][] windowStates;
    private int windowsPerRow;
    private int rows;

    public Skyscraper(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.random = new Random();
        this.lastWindowUpdate = System.currentTimeMillis();

        this.buildingColors = new Color[] {
                new Color(25, 25, 40),
                new Color(30, 25, 45),
                new Color(35, 30, 50),
                new Color(40, 35, 55)
        };

        this.windowColor = new Color(200, 180, 100); // Теплый желтый для окон

        initializeWindows();
    }

    private void initializeWindows() {
        int windowWidth = 8;
        int windowHeight = 12;
        int horizontalSpacing = 10;
        int verticalSpacing = 15;

        this.windowsPerRow = (width - 20) / (windowWidth + horizontalSpacing);
        this.rows = (height - 50) / (windowHeight + verticalSpacing);

        this.windowStates = new boolean[rows][windowsPerRow];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < windowsPerRow; col++) {
                windowStates[row][col] = random.nextDouble() < getWindowLightProbability(row);
            }
        }
    }

    public void draw(Graphics2D g2d) {
        updateWindows();
        drawFoundation(g2d);
        drawMainStructure(g2d);
        drawWindows(g2d);
        drawTopDetails(g2d);
        drawAntennas(g2d);
    }

    private void drawFoundation(Graphics2D g2d) {
        g2d.setColor(new Color(15, 15, 25));

        int foundationHeight = 100;
        int foundationWidth = width + 40;

        g2d.fillRect(x - 20, y + height, foundationWidth, foundationHeight);

        g2d.setColor(new Color(0, 0, 0, 80));
        for (int i = 0; i < 10; i++) {
            g2d.fillRect(x - 20 - i, y + height + foundationHeight + i,
                    foundationWidth + i * 2, 3);
        }

        g2d.setColor(new Color(20, 20, 30));
        g2d.fillRect(x - 25, y + height - 20, 10, foundationHeight + 20);
        g2d.fillRect(x + width + 15, y + height - 20, 10, foundationHeight + 20);
    }

    private void updateWindows() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastWindowUpdate > 2000 + random.nextInt(3000)) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < windowsPerRow; col++) {
                    if (random.nextDouble() < 0.1) {
                        windowStates[row][col] = random.nextDouble() < getWindowLightProbability(row);
                    }
                }
            }
            lastWindowUpdate = currentTime;
        }
    }

    private void drawMainStructure(Graphics2D g2d) {
        Color buildingColor = buildingColors[random.nextInt(buildingColors.length)];

        GradientPaint gradient = new GradientPaint(
                x, y, buildingColor,
                x, y + height, buildingColor.darker().darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x, y, width, height);

        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillRect(x, y, 10, height);
        g2d.fillRect(x + width - 10, y, 10, height);

        g2d.setColor(new Color(0, 0, 0, 30));
        for (int i = 1; i < 8; i++) {
            int sectionY = y + (height / 8) * i;
            g2d.fillRect(x, sectionY, width, 4);
        }

        g2d.setColor(new Color(0, 0, 0, 20));
        for (int i = 1; i < 4; i++) {
            int columnX = x + (width / 4) * i;
            g2d.fillRect(columnX, y, 3, height);
        }

        g2d.setColor(new Color(30, 30, 45));
        g2d.fillRect(x - 5, y + height - 10, width + 10, 15);
    }

    private void drawWindows(Graphics2D g2d) {
        int windowWidth = 8;
        int windowHeight = 12;
        int horizontalSpacing = 10;
        int verticalSpacing = 15;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < windowsPerRow; col++) {
                int windowX = x + 10 + col * (windowWidth + horizontalSpacing);
                int windowY = y + 25 + row * (windowHeight + verticalSpacing);

                drawWindow(g2d, windowX, windowY, windowWidth, windowHeight, row, col);
            }
        }
    }

    private void drawWindow(Graphics2D g2d, int x, int y, int width, int height, int row, int col) {
        boolean isLightOn = windowStates[row][col];

        if (isLightOn) {
            g2d.setColor(windowColor);
            g2d.fillRect(x, y, width, height);

            g2d.setColor(new Color(255, 240, 180, 80));
            g2d.fillRect(x - 1, y - 1, width + 2, height + 2);

            g2d.setColor(new Color(150, 130, 80));
            g2d.drawLine(x + width/2, y, x + width/2, y + height);
            g2d.drawLine(x, y + height/2, x + width, y + height/2);
        } else {

            g2d.setColor(new Color(15, 15, 25));
            g2d.fillRect(x, y, width, height);

            g2d.setColor(new Color(40, 40, 60));
            g2d.fillRect(x + 1, y + 1, width - 2, 2);
        }
    }

    private float getWindowLightProbability(int row) {
        if (row < 8 || row > 50) return 0.3f;
        if (row >= 8 && row <= 20) return 0.5f;
        if (row > 20 && row <= 40) return 0.7f;
        return 0.4f;
    }

    private void drawTopDetails(Graphics2D g2d) {
        g2d.setColor(new Color(50, 50, 70));
        g2d.fillRect(x - 8, y, width + 16, 12);

        g2d.setColor(new Color(60, 60, 80));
        for (int i = 0; i < 5; i++) {
            int detailX = x + i * width/5;
            g2d.fillRect(detailX, y - 4, 4, 4);
        }

        g2d.setColor(new Color(70, 70, 90));
        g2d.fillRect(x + width/4, y - 8, width/2, 8);
    }

    private void drawAntennas(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);

        int mainAntennaX = x + width/2;
        int mainAntennaHeight = 35 + random.nextInt(15);
        g2d.fillRect(mainAntennaX, y - mainAntennaHeight, 3, mainAntennaHeight);

        int leftAntennaX = x + width/4;
        int rightAntennaX = x + 3 * width/4;
        int sideAntennaHeight = 25 + random.nextInt(10);

        g2d.fillRect(leftAntennaX, y - sideAntennaHeight, 2, sideAntennaHeight);
        g2d.fillRect(rightAntennaX, y - sideAntennaHeight, 2, sideAntennaHeight);

        long time = System.currentTimeMillis();
        boolean redLightOn = (time / 600) % 2 == 0;
        boolean whiteLightOn = (time / 400) % 2 == 0;

        if (redLightOn) {
            g2d.setColor(Color.RED);
            g2d.fillRect(mainAntennaX - 1, y - mainAntennaHeight, 5, 3);
        }

        if (whiteLightOn) {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(leftAntennaX - 1, y - sideAntennaHeight, 4, 2);
            g2d.fillRect(rightAntennaX - 1, y - sideAntennaHeight, 4, 2);
        }
    }
}