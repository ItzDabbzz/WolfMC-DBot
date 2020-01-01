package me.itzdabbzz.wolfmc.util;

import me.itzdabbzz.wolfmc.WolfBot;
import me.vem.jdab.utils.Logger;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.awt.*;
import java.io.File;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.dv8tion.jda.api.EmbedBuilder.ZERO_WIDTH_SPACE;

public class Utils {

    private static final Pattern INVITE_REGEX = Pattern
            .compile("(?i)discord(\\.(com|gg|io|me|net|org|xyz)|app\\.com/invite)/[a-z0-9-_.]+");
    private static final Pattern LINK_REGEX = Pattern
            .compile("((http(s)?://)(www\\.)?)[a-zA-Z0-9-]+\\.[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)?/?(.+)?");
    private static final Pattern YOUTUBE_LINK_REGEX = Pattern
            .compile("(http(s)?://)?(www\\.)?youtu(be\\.com)?(\\.be)?/(watch\\?v=)?[a-zA-Z0-9-_]+");

    private static JDA cachedJDA;

    /**
     TODO: Use These More
     */

    public static void sendPM(User user, String message) {
        try {
            user.openPrivateChannel().complete()
                    .sendMessage(message.substring(0, Math.min(message.length(), 1999))).queue();
        } catch (ErrorResponseException ignored) {
        }
    }

    public static void sendFilePM(User user, String message, File file) {
        try {
            user.openPrivateChannel().complete()
                    .sendMessage(message).addFile(file).queue();
        } catch (ErrorResponseException ignored) {
        }
    }

    public static void sendFilePM(User user, EmbedBuilder message, File file) {
        try {
            user.openPrivateChannel().complete()
                    .sendMessage(message.build()).addFile(file).queue();
        } catch (ErrorResponseException ignored) {
        }
    }

    public static void sendPM(User user, Message message) {
        try {
            user.openPrivateChannel().complete()
                    .sendMessage(message).queue();
        } catch (ErrorResponseException ignored) {
        }
    }

    public static void sendEmbededPM(User user, EmbedBuilder message) {
        try {
            user.openPrivateChannel().complete()
                    .sendMessage(message.build()).queue();
        } catch (ErrorResponseException ignored) {
        }
    }

    public static void sendPM(TextChannel channel, User user, String message, String fail) {
        user.openPrivateChannel().queue(pc -> pc.sendMessage(message).queue(), t -> {
            channel.sendMessage(fail).queue();
        });
    }

    public static void sendPM(TextChannel channel, User user, String message) {
        try {
            user.openPrivateChannel().complete()
                    .sendMessage(message.substring(0, Math.min(message.length(), 1999))).queue();
        } catch (ErrorResponseException e) {
            channel.sendMessage(message).queue();
        }
    }

    public static void sendPM(TextChannel channel, User user, EmbedBuilder message) {
        try {
            user.openPrivateChannel().complete().sendMessage(message.build()).complete();
        } catch (ErrorResponseException e) {
            channel.sendMessage(message.build()).queue();
        }
    }

    public static void editMessage(Message message, String content) {
        message.editMessage(content).queue();
    }

    public static String getTag(User user) {
        return user.getName() + '#' + user.getDiscriminator();
    }

    public static String getUserAndId(User user) {
        return getTag(user) + " (" + user.getId() + ")";
    }

    public static String getAvatar(User user) {
        return user.getEffectiveAvatarUrl();
    }

    public static String getDefaultAvatar(User user) {
        return user.getDefaultAvatarUrl();
    }

    public static boolean hasInvite(Message message) {
        return INVITE_REGEX.matcher(message.getContentRaw()).find();
    }

    public static boolean hasInvite(String message) {
        return INVITE_REGEX.matcher(message).find();
    }

    public static String getInvite(String message) {
        Matcher matcher = INVITE_REGEX.matcher(message);
        if (matcher.find())
            return matcher.group();
        else
            return null;
    }

    public static boolean hasLink(Message message) {
        return LINK_REGEX.matcher(message.getContentRaw()).find();
    }

    public static boolean hasLink(String message) {
        return LINK_REGEX.matcher(message).find();
    }

    public static boolean hasYouTubeLink(Message message) {
        return YOUTUBE_LINK_REGEX.matcher(message.getContentRaw()).find();
    }

    public static void autoDeleteMessage(Message message, long delay) {
        message.delete().queueAfter(delay, TimeUnit.MILLISECONDS);
    }

    public static void sendAutoDeletedMessage(Message message, long delay, MessageChannel channel) {
        channel.sendMessage(message).queue(msg -> autoDeleteMessage(msg, delay));
    }

    public static void sendAutoDeletedMessage(MessageEmbed messageEmbed, long delay, MessageChannel channel) {
        channel.sendMessage(messageEmbed).queue(msg -> autoDeleteMessage(msg, delay));
    }

    private static void sendMessage(MessageEmbed embed, TextChannel channel) {
        if (channel == null) return;
        channel.sendMessage(embed).queue();
    }

    public static void sendMessage(MessageType type, String message, TextChannel channel) {
        sendMessage(type, message, channel, null);
    }

    public static void sendMessage(MessageType type, String message, TextChannel channel, User sender) {
        sendMessage(type, message, channel, sender, 0);
    }

    public static void sendMessage(MessageType type, String message, TextChannel channel, long autoDeleteDelay) {
        sendMessage(type, message, channel, null, autoDeleteDelay);
    }

    public static void sendMessage(MessageType type, String message, TextChannel channel, User sender, long autoDeleteDelay) {
        sendMessage(type, (sender != null ? getEmbed(sender) : getEmbed()).setColor(type.getColor())
                        .setTimestamp(OffsetDateTime.now(Clock.systemUTC()))
                        .setDescription(FormatUtils.formatCommandPrefix((channel != null ? channel.getGuild() : null), message))
                , channel, autoDeleteDelay);
    }

    public static void sendMessage(MessageType type, EmbedBuilder builder, TextChannel channel, long autoDeleteDelay) {
        if (builder.build().getColor() == null)
            builder.setColor(type.getColor());
        if (type == MessageType.ERROR) {
            builder.setDescription(builder.build().getDescription() + "\n\nIf you need more support contact ItzDabbzz");
        }

        if (type != MessageType.WARNING && type != MessageType.ERROR && builder.getFields().isEmpty()) {

                builder.setDescription(builder.build().getDescription() + "\n\n");

        }
        if (autoDeleteDelay > 0)
            sendAutoDeletedMessage(builder.build(), autoDeleteDelay, channel);
        else
            sendMessage(builder.build(), channel);
    }

    // Root of sendMessage(Type, Builder, channel)
    public static void sendMessage(MessageType type, EmbedBuilder builder, TextChannel channel) {
        sendMessage(type, builder, channel, 0);
    }

    public static void sendMessage(String message, TextChannel channel) {
        sendMessage(MessageType.NEUTRAL, message, channel);
    }

    public static void sendMessage(String message, TextChannel channel, User sender) {
        sendMessage(MessageType.NEUTRAL, message, channel, sender);
    }

    public static void sendErrorMessage(String message, TextChannel channel) {
        sendMessage(MessageType.ERROR, message, channel);
    }

    public static void sendErrorMessage(String message, TextChannel channel, User sender) {
        sendMessage(MessageType.ERROR, message, channel, sender);
    }

    public static void sendErrorMessage(EmbedBuilder builder, TextChannel channel) {
        sendMessage(MessageType.ERROR, builder, channel);
    }

    public static void sendWarningMessage(String message, TextChannel channel) {
        sendMessage(MessageType.WARNING, message, channel);
    }

    public static void sendWarningMessage(String message, TextChannel channel, User sender) {
        sendMessage(MessageType.WARNING, message, channel, sender);
    }

    public static void sendSuccessMessage(String message, TextChannel channel) {
        sendMessage(MessageType.SUCCESS, message, channel);
    }

    public static void sendSuccessMessage(String message, TextChannel channel, User sender) {
        sendMessage(MessageType.SUCCESS, message, channel, sender);
    }

    public static void sendInfoMessage(String message, TextChannel channel) {
        sendMessage(MessageType.INFO, message, channel);
    }

    public static void sendInfoMessage(String message, TextChannel channel, User sender) {
        sendMessage(MessageType.INFO, message, channel, sender);
    }

    public static void sendModMessage(String message, TextChannel channel) {
        sendMessage(MessageType.MODERATION, message, channel);
    }

    public static void sendModMessage(String message, TextChannel channel, User sender) {
        sendMessage(MessageType.MODERATION, message, channel, sender);
    }

    /**
    TODO: Setup Embeded SendAndLog Messages
     */

    public static void sendAndLogInfo(MessageType type, String message, TextChannel channel) {
        sendMessage(message, channel);
        Logger.info(message);
    }

    public static void sendAndLogErr(MessageType type, String message, TextChannel channel) {
        sendMessage(message, channel);
        Logger.err(message);
    }

    public static void sendAndLogWarn(MessageType type, String message, TextChannel channel) {
        sendMessage(message, channel);
        Logger.warn(message);
    }

    public static void sendAndLogDebug(MessageType type, String message, TextChannel channel) {
        sendMessage(message, channel);
        Logger.debug(message);
    }

    public static void editMessage(EmbedBuilder embed, Message message) {
        editMessage(message.getContentRaw(), embed, message);
    }

    public static void editMessage(String s, EmbedBuilder embed, Message message) {
        if (message != null)
            message.editMessage(new MessageBuilder().setContent((s == null ? ZERO_WIDTH_SPACE : s)).setEmbed(embed.build()).build()).queue();
    }


    public static EmbedBuilder getEmbed() {
        if (cachedJDA == null || cachedJDA.getStatus() != JDA.Status.CONNECTED)
            cachedJDA = WolfBot.getClient().getJDA();

        EmbedBuilder defaultEmbed = new EmbedBuilder().setColor(Constants.embedCyan);

        // We really need to PR getAuthor and things into EmbedBuilder.
        if (cachedJDA != null) {
            defaultEmbed.setAuthor("WolfMC", "http://wolfmc.net", cachedJDA.getSelfUser().getEffectiveAvatarUrl());
        }

        return defaultEmbed.setColor(Constants.embedCyan);
    }

    public static EmbedBuilder getEmbed(User user) {
        return getEmbed().setFooter("Requested by @" + getTag(user), user.getEffectiveAvatarUrl());
    }

    /**
     * Gets an int from a String.
     * The default value you pass is what it return if their was an error parsing the string.
     * This happens when the string you enter isn't an int. For example if you enter in 'no'.
     *
     * @param s            The string to parse an int from.
     * @param defaultValue The default int value to get in case parsing fails.
     * @return The int parsed from the string or the default value.
     */
    public static int getInt(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    private static final Random random = new Random();

    public static int getInt(int min, int max) {
        return random.nextInt((max == Integer.MAX_VALUE ? Integer.MAX_VALUE : max + 1) - min) + min;
    }

    public static String getRandomString(Collection<String> collection) {
        return collection.toArray(new String[collection.size()])[random.nextInt(collection.size())];
    }

    public static String getRandomStringFromArray(String[] array) {
        return array[random.nextInt(array.length)];
    }

    /**
     * Gets an int from a String.
     * The default value you pass is what it return if their was an error parsing the string.
     * This happens when the string you enter isn't an int. For example if you enter in 'no'.
     *
     * @param s            The string to parse an int from.
     * @param defaultValue The default int value to get in case parsing fails.
     * @return The int parsed from the string or the default value.
     */
    public static int getPositiveInt(String s, int defaultValue) {
        try {
            int i = Integer.parseInt(s);
            return (i >= 0 ? i : defaultValue);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Formats a color into a hex String.
     *
     * @param color The color to format.
     * @return The hex value of the color.
     */
    public static String colourFormat(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static TimeUnit parseTimeUnit(String time){
        TimeUnit unit = TimeUnit.SECONDS;
        String str;
        char[] t = time.toCharArray();
        int breakPoint = 0;
        String amount = "";
        int parsedAmount = 0;
        for(int i = 0; i < t.length; i++){
            if(t[i] == 's' || t[i] == 'S'){
                unit = TimeUnit.SECONDS;
                breakPoint = i;
                break;
            }else if(t[i] == 'm' || t[i] == 'M'){
                unit = TimeUnit.MINUTES;
                breakPoint = i;
                break;
            }else if(t[i] == 'h' || t[i] == 'H'){
                unit = TimeUnit.HOURS;
                breakPoint = i;
                break;
            }else if(t[i] == 'd' || t[i] == 'D'){
                unit = TimeUnit.DAYS;
                breakPoint = i;
                break;
            }
        }
        return unit;
    }

    public static int parseTimeAmount(String time){
        TimeUnit unit = TimeUnit.SECONDS;
        String str;
        char[] t = time.toCharArray();
        int breakPoint = 0;
        String amount = "";
        int parsedAmount = 0;
        for(int i = 0; i < t.length; i++){
            if(t[i] == 's' || t[i] == 'S'){
                unit = TimeUnit.SECONDS;
                breakPoint = i;
                break;
            }else if(t[i] == 'm' || t[i] == 'M'){
                unit = TimeUnit.MINUTES;
                breakPoint = i;
                break;
            }else if(t[i] == 'h' || t[i] == 'H'){
                unit = TimeUnit.HOURS;
                breakPoint = i;
                break;
            }else if(t[i] == 'd' || t[i] == 'D'){
                unit = TimeUnit.DAYS;
                breakPoint = i;
                break;
            }
        }
        for(int i = 0; i < breakPoint; i++){
            amount += t[i];
        }
        parsedAmount = Integer.parseInt(amount);
        return parsedAmount;
    }

}
