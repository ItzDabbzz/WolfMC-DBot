package me.itzdabbzz.wolfmc.util;

import me.itzdabbzz.wolfmc.objects.GuildWrapper;
import me.vem.jdab.cmd.Prefix;
import net.dv8tion.jda.api.entities.Guild;

public class FormatUtils {

    /**
     * Formats a duration to a readable string.
     *
     * @param duration The duration as a long in millis to format.
     * @return A string representing the duration with the format h:m:s
     */
    public static String formatDuration(long duration) {
        long totalSeconds = duration / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = (totalSeconds / 3600);
        return (hours > 0 ? (hours < 10 ? "0" + hours : hours) + ":" : "")
                + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    /**
     * Formats a String to replace {%} with the prefix from the guild wrapper for a {@link Guild}.
     *
     * @param guild The guild wrapper for the {@link Guild} the get prefix from.
     * @param usage The String to format with prefix.
     * @return The String with the prefix in place of {%}.
     */
    public static String formatCommandPrefix(GuildWrapper guild, String usage) {
        return FormatUtils.formatCommandPrefix(guild.getGuild(), usage);
    }

    /**
     * Formats a String to replace {%} with the prefix from the {@link Guild}.
     *
     * @param guild The {@link Guild} the get prefix from.
     * @param usage The String to format with prefix.
     * @return The String with the prefix in place of of {%}.
     */
    public static String formatCommandPrefix(Guild guild, String usage) {
        if (guild == null) return Constants.COMMAND_CHAR_STRING;
        String prefix = String.valueOf(Prefix.getInstance().getPrefix(guild));
        if (usage.contains("{%}"))
            return usage.replace("{%}", prefix);
        return usage;
    }

    /**
     * Truncates a String to the given length. With ellipses. This truncates from the end.
     * Note that this will truncate the string to three less then the length because of the ellipses.
     * (The total length of the string would be the provided length).
     *
     * @param length The amount to truncate the string to.
     * @param string The string to truncate.
     * @return The truncated String.
     */
    public static String truncate(int length, String string) {
        return truncate(length, string, true);
    }

    /**
     * Truncates a String to the given length. This truncates from the end.
     * If you do use eclipse the length would be three less then the provided length due to the ellipses.
     * (The total length of the string would be the provided length).
     *
     * @param length The amount to truncate the string to.
     * @param string The string to add them to.
     * @param ellipse Weather or not to use ellipses.
     * @return The truncated String.
     */
    public static String truncate(int length, String string, boolean ellipse) {
        return string.substring(0, Math.min(string.length(), length - (ellipse ? 3 : 0))) + (string.length() >
                length - (ellipse ? 3 : 0) && ellipse ? "..." : "");
    }

}
