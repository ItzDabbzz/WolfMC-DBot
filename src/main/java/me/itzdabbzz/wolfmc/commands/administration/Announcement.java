package me.itzdabbzz.wolfmc.commands.administration;


import me.itzdabbzz.wolfmc.commands.moderation.Permissions;
import me.itzdabbzz.wolfmc.commands.moderation.SecureCommand;
import me.itzdabbzz.wolfmc.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Announcement extends SecureCommand{

    private static Announcement instance;
    public static Announcement getInstance() {
        return instance;
    }

    public static void initialize() {
        if(instance == null)
            instance = new Announcement();
    }

    private Announcement() {
        super("announcement");

    }

    @Override
    public boolean run(GuildMessageReceivedEvent event, String... args) {
        if(!super.run(event, args))
            return false;

        String message = String.join(" ", args);
        User author = event.getAuthor();

        EmbedBuilder eb = new EmbedBuilder()
                .setColor( Constants.embedPink )
                .setAuthor("WolfMC", "http://wolfmc.net", author.getAvatarUrl())
                .setDescription(message)
                .setFooter("WolfMC Network", "");

        event.getJDA().getGuilds().stream()
                .forEach(g -> {
                    try {
                        g.getTextChannelById(620316089888407594L).sendMessage(eb.build()).queue();
                    } catch (Exception e) { }
                });

        return true;
    }

    @Override
    public String[] usages() {
        return new String[] {"`announcement <message>` -- send a announcement to the server"};
    }

    @Override
    public String getDescription() {
        return "send a announcement to  the server";
    }

    @Override
    public List<String> getValidKeySet() {
        return Arrays.asList("admin.announce");
    }

    @Override
    public boolean hasPermissions( Member member, String... args) {
        return Permissions.getInstance().hasPermissionsFor(member, "admin.announce");
    }

    @Override
    protected void unload() {
        instance = null;
    }
}