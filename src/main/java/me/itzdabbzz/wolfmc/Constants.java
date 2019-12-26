package me.itzdabbzz.wolfmc;


import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static net.dv8tion.jda.api.Permission.*;

public class Constants {

    public static Color embedCyan = new Color(0, 212, 255, 0);
    public static Color embedLime = new Color(127, 255, 121, 0);
    public static Color embedTeal = new Color(0, 255, 197, 0);
    public static Color embedYellow = new Color(228, 255, 0, 0);
    public static Color embedOrange = new Color(255, 178, 0, 0);
    public static Color embedPurple = new Color(158, 0, 255, 0);
    public static Color embedDPink = new Color(255, 0, 243, 0);
    public static Color embedPink = new Color(255, 111, 211, 0);
    public static Color embedRed = new Color(255, 0, 0, 0);
    public static final String CHECK = "✅";
    public static final List<Permission> TICKET = Arrays.asList(MESSAGE_ADD_REACTION, MESSAGE_ATTACH_FILES, MESSAGE_EMBED_LINKS,
            MESSAGE_READ, MESSAGE_WRITE, MESSAGE_HISTORY);

}
