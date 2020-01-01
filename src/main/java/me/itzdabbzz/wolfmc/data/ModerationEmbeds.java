package me.itzdabbzz.wolfmc.data;

import me.itzdabbzz.wolfmc.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ModerationEmbeds {

    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
    private static SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
    private static Date date = new Date(System.currentTimeMillis());

    public static void tempMuteEmbed(Member muted, Member staff, String reason, TextChannel channel){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Mute Report");
        embed.setAuthor("WolfMC", "http://wolfmc.net", "");
        embed.setColor(Constants.embedLime);
        embed.addField("Muted User", muted.getAsMention(), false);
        embed.addField("Staff Member", staff.getAsMention(), false);
        embed.addField("Reason", reason, false);
        embed.addField("Data", sdf.format(date), false);
        embed.addField("Time", stf.format(date), false);
        channel.sendMessage(embed.build()).queue();
    }

    public static void kickEmbed(Member kicked, Member staff, String reason, TextChannel channel){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Kick Report");
        embed.setAuthor("WolfMC", "http://wolfmc.net", "");
        embed.setColor(Constants.embedLime);
        embed.addField("Kicked User", kicked.getAsMention(), false);
        embed.addField("Staff Member", staff.getAsMention(), false);
        embed.addField("Reason", reason, false);
        embed.addField("Data", sdf.format(date), false);
        embed.addField("Time", stf.format(date), false);
        channel.sendMessage(embed.build()).queue();
    }

    public static void banEmbed(Member banned, Member staff, String reason, TextChannel channel){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Ban Report");
        embed.setAuthor("WolfMC", "http://wolfmc.net", "");
        embed.setColor(Constants.embedLime);
        embed.addField("Banned User", banned.getAsMention(), false);
        embed.addField("Staff Member", staff.getAsMention(), false);
        embed.addField("Reason", reason, false);
        embed.addField("Data", sdf.format(date), false);
        embed.addField("Time", stf.format(date), false);
        channel.sendMessage(embed.build()).queue();
    }

}
