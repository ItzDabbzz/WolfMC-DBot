package me.itzdabbzz.wolfmc.util;

import java.awt.*;

public enum MessageType {

    INFO(Constants.embedCyan),
    SUCCESS(Constants.embedLime),
    WARNING(Constants.embedYellow),
    MODERATION(Constants.embedDPink),
    ERROR(Color.RED),
    NEUTRAL(Constants.embedTeal);

    private final Color color;

    MessageType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}