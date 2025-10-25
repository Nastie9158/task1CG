package ru.house.shirkova.a.s;

import java.awt.*;
import java.util.Random;

public class Background {
    private final int width;
    private final int height;
    private final Random random;

    public Background(int width, int height) {
        this.width = width;
        this.height = height;
        this.random = new Random();
    }

    public void draw(Graphics2D g2d) {
        drawSky(g2d);
        drawStars(g2d);
    }

    private void drawSky(Graphics2D g2d) {
        GradientPaint skyGradient = new GradientPaint(
                0, 0, new Color(10, 8, 25),
                0, height, new Color(20, 15, 40)
        );
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, width, height);
    }

    private void drawStars(Graphics2D g2d) {
        long time = System.currentTimeMillis();

        for (int i = 0; i < 120; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int size = random.nextInt(2) + 1;


            float flicker = (float) (Math.sin(time * 0.003 + i) * 0.4 + 0.6);
            int brightness = (int) (flicker * 200);

            g2d.setColor(new Color(255, 255, 255, brightness));
            if (random.nextDouble() < 0.3) {

                g2d.fillRect(x - size, y, size * 3, size);
                g2d.fillRect(x, y - size, size, size * 3);
            } else {
                g2d.fillOval(x, y, size, size);
            }
        }
    }
}