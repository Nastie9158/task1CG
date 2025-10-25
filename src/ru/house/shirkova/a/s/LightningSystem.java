package ru.house.shirkova.a.s;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LightningSystem {
    private final int width;
    private final int height;
    private final List<Lightning> lightnings;
    private final Random random;
    private boolean lightningActive;
    private int lightningCounter;

    public LightningSystem(int width, int height) {
        this.width = width;
        this.height = height;
        this.random = new Random();
        this.lightnings = new ArrayList<>();
    }

    public void triggerLightningFromCloud(CloudSystem.Cloud cloud, int groundY) {
        lightningActive = true;
        lightningCounter = 0;

        float startX = cloud.getCenterX() + random.nextFloat() * cloud.width * 0.3f - cloud.width * 0.15f;
        float startY = cloud.getCenterY() + cloud.height * 0.2f;

        lightnings.add(new Lightning(startX, startY, groundY, 1.0f, true));

        int branchCount = random.nextInt(3) + 1;
        for (int i = 0; i < branchCount; i++) {
            float branchStartY = startY + random.nextFloat() * (groundY - startY) * 0.5f;
            lightnings.add(new Lightning(startX, branchStartY, groundY, 0.6f, false));
        }

        for (int i = 0; i < 2; i++) {
            createCloudLightning(cloud);
        }
    }

    private void createCloudLightning(CloudSystem.Cloud cloud) {
        float x1 = cloud.x + random.nextFloat() * cloud.width;
        float y1 = cloud.y + random.nextFloat() * cloud.height;
        float x2 = x1 + random.nextFloat() * 100 - 50;
        float y2 = y1 + random.nextFloat() * 60 - 30;

        Lightning cloudLightning = new Lightning(x1, y1, y2, 0.4f, false);
        cloudLightning.isCloudLightning = true;
        lightnings.add(cloudLightning);
    }

    public void update() {
        if (lightningActive) {
            lightningCounter++;
            if (lightningCounter > 15) {
                lightningActive = false;
            }
        }

        lightnings.removeIf(lightning -> !lightning.isActive());

        for (Lightning lightning : lightnings) {
            lightning.update();
        }
    }

    public void drawLightningEffect(Graphics2D g2d) {
        if (lightningActive) {
            int baseAlpha = 180 - lightningCounter * 12;
            int alpha = Math.max(0, baseAlpha + (int)(Math.sin(lightningCounter * 0.8) * 30));

            g2d.setColor(new Color(255, 255, 220, alpha));
            g2d.fillRect(0, 0, width, height);
        }
    }

    public void draw(Graphics2D g2d) {
        for (Lightning lightning : lightnings) {
            lightning.draw(g2d);
        }
    }

    private class Lightning {
        float startX, startY, endY;
        float intensity;
        boolean isMain;
        boolean isCloudLightning;
        int life;
        int maxLife;
        List<LightningSegment> segments;

        Lightning(float startX, float startY, float endY, float intensity, boolean isMain) {
            this.startX = startX;
            this.startY = startY;
            this.endY = endY;
            this.intensity = intensity;
            this.isMain = isMain;
            this.maxLife = random.nextInt(10) + 15;
            this.life = maxLife;
            this.segments = new ArrayList<>();

            generateSegments();
        }

        private void generateSegments() {
            int segmentCount = 15;
            float segmentHeight = (endY - startY) / segmentCount;
            float currentX = startX;
            float currentY = startY;

            for (int i = 0; i < segmentCount; i++) {
                float nextX = currentX + random.nextFloat() * 40 - 20;
                float nextY = currentY + segmentHeight;

                segments.add(new LightningSegment(currentX, currentY, nextX, nextY));
                currentX = nextX;
                currentY = nextY;
            }

            if (isMain) {
                addBranches();
            }
        }

        private void addBranches() {
            for (int i = 2; i < segments.size() - 2; i += 2) {
                if (random.nextDouble() < 0.6) {
                    LightningSegment segment = segments.get(i);
                    createBranch(segment.endX, segment.endY);
                }
            }
        }

        private void createBranch(float startX, float startY) {
            int branchSegments = random.nextInt(3) + 2;
            float currentX = startX;
            float currentY = startY;

            for (int i = 0; i < branchSegments; i++) {
                float nextX = currentX + random.nextFloat() * 20 - 10;
                float nextY = currentY + random.nextFloat() * 30 + 10;
                segments.add(new LightningSegment(currentX, currentY, nextX, nextY));
                currentX = nextX;
                currentY = nextY;
            }
        }

        void update() {
            life--;
        }

        boolean isActive() {
            return life > 0;
        }

        void draw(Graphics2D g2d) {
            float progress = (float) life / maxLife;
            int alpha = (int) (progress * intensity * 255);

            Color boltColor = isCloudLightning ?
                    new Color(200, 220, 255, alpha) :
                    new Color(180, 200, 255, alpha);

            g2d.setColor(boltColor);

            float thickness = isMain ? 3.0f : (isCloudLightning ? 1.5f : 2.0f);
            g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            for (LightningSegment segment : segments) {
                g2d.drawLine((int)segment.startX, (int)segment.startY,
                        (int)segment.endX, (int)segment.endY);
            }

            if (isMain && progress > 0.5f) {
                g2d.setColor(new Color(200, 220, 255, (int)(alpha * 0.3f)));
                g2d.setStroke(new BasicStroke(thickness * 3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                for (LightningSegment segment : segments) {
                    if (random.nextDouble() < 0.7) {
                        g2d.drawLine((int)segment.startX, (int)segment.startY,
                                (int)segment.endX, (int)segment.endY);
                    }
                }
            }
        }
    }

    private static class LightningSegment {
        float startX, startY, endX, endY;

        LightningSegment(float startX, float startY, float endX, float endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }
    }
}