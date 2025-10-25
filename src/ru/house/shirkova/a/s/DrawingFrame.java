package ru.house.shirkova.a.s;

import javax.swing.*;
import java.awt.*;

public class DrawingFrame extends JFrame {
    private final int WIDTH = 1400;
    private final int HEIGHT = 900;

    public DrawingFrame() {
        setTitle("Гроза в небоскребном городе");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        DrawingPanel drawingPanel = new DrawingPanel();
        add(drawingPanel);
    }
}