package ru.house.shirkova.a.s.Objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RainSystem {
    private final int width;
    private final int height;
    private final List<RainDrop> rainDrops;
    private final List<RainSplash> rainSplashes;
    private final Random random;

    public RainSystem(int width, int height, int dropCount) {
        this.width = width;
        this.height = height;
        this.random = new Random();
        this.rainDrops = new ArrayList<>();
        this.rainSplashes = new ArrayList<>();

        initializeRain(dropCount);
    }

    private void initializeRain(int dropCount) {
        for (int i = 0; i < dropCount; i++) {
            rainDrops.add(createRainDrop());
        }
    }

    private RainDrop createRainDrop() {
        return new RainDrop(
                random.nextInt(width + 200) - 100,
                random.nextInt(height),
                random.nextInt(12) + 10,
                random.nextInt(25) + 20,
                random.nextFloat() * 0.4f + 0.6f,
                random.nextFloat() * 0.8f + 0.7f
        );
    }

    public void update() {
        for (RainDrop drop : rainDrops) {
            drop.y += drop.speed;
            drop.x -= 3;

            if (drop.y > height || drop.x < -50) {
                resetRainDrop(drop);
            }
        }

        rainSplashes.removeIf(splash -> !splash.update());

        if (random.nextDouble() < 0.3) {
            rainSplashes.add(new RainSplash(
                    random.nextInt(width),
                    height - 20
            ));
        }
    }

    private void resetRainDrop(RainDrop drop) {
        drop.y = random.nextInt(100) - 100;
        drop.x = random.nextInt(width + 100) - 50;
    }

    public void draw(Graphics2D g2d) {
        drawHeavyRain(g2d);
        drawLightRain(g2d);
        drawRainSplashes(g2d);
        drawRainMist(g2d);
    }

    private void drawHeavyRain(Graphics2D g2d) {
        for (RainDrop drop : rainDrops) {
            if (drop.opacity > 0.7f) {
                drop.draw(g2d);
            }
        }
    }

    private void drawLightRain(Graphics2D g2d) {
        for (RainDrop drop : rainDrops) {
            if (drop.opacity <= 0.7f) {
                drop.draw(g2d);
            }
        }
    }

    private void drawRainSplashes(Graphics2D g2d) {
        for (RainSplash splash : rainSplashes) {
            splash.draw(g2d);
        }
    }

    private void drawRainMist(Graphics2D g2d) {
        GradientPaint mist = new GradientPaint(
                0, height - 200, new Color(100, 100, 150, 20),
                0, height, new Color(80, 80, 120, 40)
        );
        g2d.setPaint(mist);
        g2d.fillRect(0, height - 200, width, 200);
    }

    private static class RainDrop {
        float x, y, speed, length, opacity, thickness;

        RainDrop(float x, float y, float speed, float length, float opacity, float thickness) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.length = length;
            this.opacity = opacity;
            this.thickness = thickness;
        }

        void draw(Graphics2D g2d) {
            g2d.setColor(new Color(180, 200, 255, (int)(opacity * 200)));
            g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            g2d.drawLine((int)x, (int)y, (int)(x - length * 0.3f), (int)(y + length));
        }
    }

    private static class RainSplash {
        float x, y, life, maxLife;

        RainSplash(float x, float y) {
            this.x = x;
            this.y = y;
            this.maxLife = 15;
            this.life = maxLife;
        }

        boolean update() {
            life--;
            return life > 0;
        }

        void draw(Graphics2D g2d) {
            float progress = life / maxLife;
            int alpha = (int) (progress * 120);
            int size = (int) ((1 - progress) * 6);

            g2d.setColor(new Color(150, 170, 220, alpha));
            g2d.setStroke(new BasicStroke(1.2f));

            g2d.drawLine((int)x - size, (int)y, (int)x + size, (int)y);
            g2d.drawLine((int)x, (int)y - size, (int)x, (int)y + size);

            g2d.drawLine((int)x - size, (int)y - size, (int)x + size, (int)y + size);
            g2d.drawLine((int)x - size, (int)y + size, (int)x + size, (int)y - size);
        }
    }
}