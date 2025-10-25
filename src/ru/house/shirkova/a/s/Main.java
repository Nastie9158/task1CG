package ru.house.shirkova.a.s;

import ru.house.shirkova.a.s.DrawObjects.DrawingFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DrawingFrame frame = new DrawingFrame();
            frame.setVisible(true);
        });
    }
}

