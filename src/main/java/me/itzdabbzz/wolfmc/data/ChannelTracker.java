package me.itzdabbzz.wolfmc.data;

import me.itzdabbzz.wolfmc.WolfBot;
import me.vem.jdab.utils.Utilities;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Optional;

public class ChannelTracker {

    private static Category category;
    private static TextChannel textChannel;

    public static Optional<TextChannel> getSupportChannel() {
        return Optional.ofNullable(textChannel);

    }

    public static void setChannel(TextChannel channel) {
        textChannel = channel;
        category = channel.getParent();
    }

}
