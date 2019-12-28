package me.itzdabbzz.wolfmc.util;

import java.awt.*;

public enum MessageType {

    INFO(Color.CYAN),
    SUCCESS(Color.GREEN),
    WARNING(Color.YELLOW),
    MODERATION(Color.magenta),
    ERROR(Color.RED),
    NEUTRAL(Constants.embedCyan);

    private final Color color;

    MessageType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}