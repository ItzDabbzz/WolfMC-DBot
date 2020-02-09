package me.itzdabbzz.wolfmc;

import me.vem.jdab.sqlite.SqliteDatabase;
import me.vem.jdab.sqlite.SqliteQuery;
import me.vem.jdab.utils.Logger;
import me.vem.jdab.utils.Utilities;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuildListeners extends ListenerAdapter {

    private WolfBot bot;
    private static SqliteDatabase db;

    public void onDisconnect(DisconnectEvent event) {
        Logger.info("DISCONNECTED! ");
    }

    @Override
    public void onStatusChange(StatusChangeEvent event) {

    }

    @Override
    public void onResume(ResumedEvent event) {
    }

    @Override
    public void onReconnect(ReconnectedEvent event) {
        Logger.warn("RECONNECTING!!!");
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e){

    }

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {

    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        Logger.info( "**" + event.getUser().getName() + "#" + event.getUser().getDiscriminator() + "** has been banned");
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        Member member = Utilities.getMemberFromMention(event.getGuild(), event.getUser().getAsMention().toString());
        List<Role> roles = new ArrayList<>();
        String sqlQuery = "INSERT INTO wb_users ('id', 'name', 'group', 'xp', 'level', 'muted') VALUES (value-1, value-2, value-3, value-4, value-5, value-6)";
        db = SqliteDatabase.create();


        for (Role memberRole : member.getRoles()){
            roles.add(memberRole);
        }

        try(SqliteQuery command = db.getQuery(sqlQuery)){
            command.addParameter("value-1", member.getId());
            command.addParameter("value-2", member.getEffectiveName());
            command.addParameter("value-3", roles.toString());
            command.addParameter("value-4", 0);
            command.addParameter("value-5", 1);
            command.addParameter("value-6", "0");
            command.execNonQuery();
            Logger.info("Inserted User " + member.getEffectiveName());
        } catch(IOException | SQLException e) {
            e.printStackTrace();
            Logger.err("Cannot Insert User " + member.getEffectiveName()) ;
        }
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {

        String sqlQuery = "DELETE FROM wb_users WHERE id LIKE " + event.getMember().getIdLong();

        db = SqliteDatabase.create();

        try(SqliteQuery command = db.getQuery(sqlQuery)){
            command.execNonQuery();
            Logger.info("Removed User " + event.getMember().getEffectiveName());
        } catch(IOException | SQLException e) {
            e.printStackTrace();
            Logger.err("Cannot Insert User " + event.getMember().getEffectiveName()) ;
        }
    }



   /*
        String sqlQuery = "INSERT INTO wb_tickets ('id', 'ticketID', 'user', 'reason', 'level', 'finished', 'department') VALUES (value-1, value-2, value-3, value-4, value-5, value-6, value-7)";
        db = SqliteDatabase.create();
        try(SqliteQuery command = db.getQuery(sqlQuery)){
            command.addParameter("value-1", id);
            command.addParameter("value-2", getID());
            command.addParameter("value-3", user);
            command.addParameter("value-4", reason);
            command.addParameter("value-5", level);
            command.addParameter("value-6", finished);
            command.addParameter("value-7", department);
            command.execNonQuery();
        } catch(IOException | SQLException e) {
            e.printStackTrace();
            Logger.err("Cannot Insert Ticket " + getTicketID()) ;
        }
    */
}

