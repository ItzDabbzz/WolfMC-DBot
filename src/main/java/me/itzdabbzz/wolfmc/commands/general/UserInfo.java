package me.itzdabbzz.wolfmc.commands.general;


import me.itzdabbzz.wolfmc.commands.moderation.Permissions;
import me.itzdabbzz.wolfmc.commands.moderation.SecureCommand;
import me.itzdabbzz.wolfmc.util.Constants;
import me.vem.jdab.cmd.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class UserInfo extends Command {

    private static UserInfo instance;
    public static UserInfo getInstance() {
        return instance;
    }

    public static void initialize() {
        if(instance == null)
            instance = new UserInfo();
    }

    private UserInfo() {
        super("userinfo");

    }

    @Override
    public boolean run(GuildMessageReceivedEvent event, String... args) {
        if(!super.run(event, args))
            return false;

        Member memb;

        if (args.length > 0) {
            memb = event.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
        } else {
            memb = event.getMember();
        }

        String NAME = memb.getEffectiveName();
        String TAG = memb.getUser().getName() + "#" + memb.getUser().getDiscriminator();
        String GUILD_JOIN_DATE = memb.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String DISCORD_JOINED_DATE = memb.getUser().getTimeCreated().format( DateTimeFormatter.RFC_1123_DATE_TIME);
        String ID = memb.getUser().getId();
        String STATUS = memb.getOnlineStatus().getKey();
        String ROLES = "";
        String GAME;
        String AVATAR = memb.getUser().getAvatarUrl();

        try {
            GAME = memb.getOnlineStatus().name();
        } catch (Exception e) {
            GAME = "-/-";
        }

        for ( Role r : memb.getRoles() ) {
            ROLES += r.getName() + ", ";
        }
        if (ROLES.length() > 0)
            ROLES = ROLES.substring(0, ROLES.length()-2);
        else
            ROLES = "No roles on this server.";

        if (AVATAR == null) {
            AVATAR = "No Avatar";
        }

        EmbedBuilder em = new EmbedBuilder().setColor( Constants.embedPurple);
        em.setDescription(":spy:   **User information for " + memb.getUser().getName() + ":**")
                .addField("Name / Nickname", NAME, false)
                .addField("User Tag", TAG, false)
                .addField("ID", ID, false)
                .addField("Current Status", STATUS, false)
                .addField("Current Game", GAME, false)
                .addField("Roles", ROLES, false)
                //addField("Guild Permission Level", core.Perms.getLvl(memb) + "", false)
                .addField("Guild Joined", GUILD_JOIN_DATE, false)
                .addField("Discord Joined", DISCORD_JOINED_DATE, false)
                .addField("Avatar-URL", AVATAR, false);

        if (AVATAR != "No Avatar") {
            em.setThumbnail(AVATAR);
        }

        event.getChannel().sendMessage(em.build()).queue();

        return true;
    }

    @Override
    public String[] usages() {
        return new String[] {"`userinfo <member>` -- displays user info"};
    }

    @Override
    public String getDescription() {
        return "shows user info";
    }

    @Override
    public boolean hasPermissions( Member member, String... args) { return true; }

    @Override
    protected void unload() {
        instance = null;
    }
}