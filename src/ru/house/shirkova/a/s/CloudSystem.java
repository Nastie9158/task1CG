package ru.house.shirkova.a.s;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CloudSystem {
    private final int width;
    private final int height;
    private final List<Cloud> clouds;
    private final Random random;

    public CloudSystem(int width, int height, int cloudCount) {
        this.width = width;
        this.height = height;
        this.random = new Random();
        this.clouds = new ArrayList<>();

        initializeClouds(cloudCount);
    }

    private void initializeClouds(int cloudCount) {
        for (int i = 0; i < cloudCount; i++) {
            int cloudWidth = random.nextInt(400) + 200;
            int cloudHeight = random.nextInt(80) + 60;
            int x = random.nextInt(width + 400) - 200;
            int y = random.nextInt(20) + 80;
            float speed = random.nextFloat() * 0.8f + 0.3f;

            clouds.add(new Cloud(x, y, cloudWidth, cloudHeight, speed, i));
        }
    }

    public void update() {
        for (Cloud cloud : clouds) {
            cloud.x += cloud.speed;

            if (cloud.x > width + 400) {
                cloud.x = -400;
                cloud.y = random.nextInt(20) + 80;
            }

            cloud.y += Math.sin(System.currentTimeMillis() * 0.001 + cloud.id) * 0.1f;
        }
    }

    public void draw(Graphics2D g2d) {
        for (Cloud cloud : clouds) {
            cloud.draw(g2d);
        }
    }

    public Cloud getNearestCloud(int mouseX, int mouseY) {
        Cloud nearest = null;
        float minDistance = Float.MAX_VALUE;

        for (Cloud cloud : clouds) {
            float distance = (float) Math.sqrt(
                    Math.pow(cloud.x + cloud.width/2 - mouseX, 2) +
                            Math.pow(cloud.y + cloud.height/2 - mouseY, 2)
            );

            if (distance < minDistance && distance < 300) {
                minDistance = distance;
                nearest = cloud;
            }
        }

        return nearest;
    }

    public static class Cloud {
        float x, y, width, height, speed;
        int id;
        Color baseColor;
        Color highlightColor;

        public Cloud(float x, float y, float width, float height, float speed, int id) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.speed = speed;
            this.id = id;
            this.baseColor = new Color(60, 55, 95, 180);
            this.highlightColor = new Color(80, 75, 115, 120);
        }

        public void draw(Graphics2D g2d) {

            g2d.setColor(baseColor);

            int circleCount = 7;
            for (int i = 0; i < circleCount; i++) {
                int positionFromCenter = i - circleCount / 2;

                float sizeMultiplier = 1.0f - Math.abs(positionFromCenter) * 0.15f;
                float circleSize = height * sizeMultiplier;

                float circleX = x + width * 0.5f + (positionFromCenter * width * 0.15f) - circleSize * 0.5f;
                float circleY = y + height * 0.3f + (float) Math.sin(positionFromCenter * 0.5) * height * 0.1f;

                g2d.fillOval((int)circleX, (int)circleY, (int)circleSize, (int)circleSize);
            }

            g2d.setColor(baseColor);
            float coreSize = height * 1.2f;
            g2d.fillOval((int)(x + width * 0.5f - coreSize * 0.5f),
                    (int)(y + height * 0.3f - coreSize * 0.5f),
                    (int)coreSize, (int)coreSize);

            g2d.setColor(highlightColor);
            int highlightCount = 3;
            for (int i = 0; i < highlightCount; i++) {
                int posFromCenter = i - highlightCount / 2;
                float highlightSize = height * (0.7f - Math.abs(posFromCenter) * 0.1f);
                float highlightX = x + width * 0.5f + (posFromCenter * width * 0.2f) - highlightSize * 0.5f;
                float highlightY = y + height * 0.1f;

                g2d.fillOval((int)highlightX, (int)highlightY, (int)highlightSize, (int)highlightSize);
            }

            g2d.setColor(new Color(65, 60, 100, 160));
            for (int i = 0; i < circleCount - 1; i++) {
                int posFromCenter1 = i - circleCount / 2;
                int posFromCenter2 = (i + 1) - circleCount / 2;

                float connectorX = x + width * 0.5f + ((posFromCenter1 + posFromCenter2) * 0.5f * width * 0.15f);
                float connectorY = y + height * 0.4f;
                float connectorSize = height * 0.3f;

                g2d.fillOval((int)(connectorX - connectorSize * 0.5f),
                        (int)(connectorY - connectorSize * 0.5f),
                        (int)connectorSize, (int)connectorSize);
            }
        }

        public float getCenterX() {
            return x + width / 2;
        }

        public float getCenterY() {
            return y + height / 2;
        }
    }
}