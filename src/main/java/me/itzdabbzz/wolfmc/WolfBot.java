package me.itzdabbzz.wolfmc;

import me.itzdabbzz.wolfmc.commands.administration.Announcement;
import me.itzdabbzz.wolfmc.commands.general.Ping;
import me.itzdabbzz.wolfmc.commands.general.Status;
import me.itzdabbzz.wolfmc.commands.general.XP;
import me.itzdabbzz.wolfmc.commands.moderation.*;
import me.itzdabbzz.wolfmc.commands.tickets.setChannel;
import me.itzdabbzz.wolfmc.commands.tickets.tadd;
import me.itzdabbzz.wolfmc.commands.tickets.tblacklist;
import me.itzdabbzz.wolfmc.commands.tickets.tremove;
import me.itzdabbzz.wolfmc.util.Utils;
import me.vem.jdab.DiscordBot;
import me.vem.jdab.sqlite.SqliteDatabase;
import me.vem.jdab.sqlite.SqliteQuery;
import me.vem.jdab.utils.Console;
import me.vem.jdab.utils.ExtFileManager;
import me.vem.jdab.utils.Logger;
import me.vem.jdab.utils.Version;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class WolfBot {

    /**
     *  How do i set it a system of some sort to easily access users and guild settings through sqlite for example
     *  Integer XP = new User(event.getUser()).getXP();
     *  would return a users XP
     *
     */


    public static DiscordBot getClient() {
        return DiscordBot.getInstance();
    }

    public static Utils utils;

    public static Utils getUtils() {
        return utils;
    }

    public static void main(String[] args) {
        Logger.setupFileLogging();
        Version.initialize(0 , 0 , 1 , 9 , "WolfMC");
        Console.initialize();
        Logger.infof("Hello World! From %s" , Version.getVersion());

        String tokenFile = args.length > 0 ? fetchToken(args[0]) : "config/token.txt";
        DiscordBot.initialize("");
        SQLiteSetup();

        //Permissions is critical to the function of several other commands, so it must be initialized first.
        Permissions.initialize();
        //DiscordBot.getInstance().addEventListener(ReactionListener.getInstance());
        DiscordBot.getInstance().addEventListener(new MessageListeners());
        DiscordBot.getInstance().addEventListener(Monitor.getInstance());
        DiscordBot.getInstance().addEventListener(new me.itzdabbzz.wolfmc.data.EXPSystem());
        DiscordBot.getInstance().addEventListener(new GuildListeners());

        me.itzdabbzz.wolfmc.data.EXPSystem system = new me.itzdabbzz.wolfmc.data.EXPSystem();
        system.startTimer();

        EXPSystem.initialize();
        Ping.initialize();
        ModLog.initialize();
        XP.initialize();
        Kick.initialize();
        Ban.initialize();
        setChannel.initialize();
        tadd.initialize();
        tblacklist.initialize();
        tremove.initialize();
        Purge.initialize();
        AntiPurge.initialize();
        TempMute.initialize();
        Announcement.initialize();
        Reports.initialize();
        ForceSave.initialize();
        VoiceKick.initialize();
        Status.initialize();
        //serverStatus.initialize();
    }


    public static String fetchToken(String file) {
        FileReader fReader = ExtFileManager.getFileReader(new File(file));
        if(fReader == null) return null;
        try {
            BufferedReader reader = new BufferedReader(fReader);
            String out = reader.readLine();
            reader.close();
            return out;
        } catch(IOException e) {
            return null;
        }
    }

    private static SqliteDatabase db;

    public static void SQLiteSetup() {
        SqliteDatabase.setDefaultConnection("wolfbot.db");
        db = SqliteDatabase.create();

        String sqlGuilds = "CREATE TABLE IF NOT EXISTS wb_guilds("
                + "'id' INTEGER PRIMARY KEY, "
                + "'name' TEXT, "
                + "'prefix' TEXT, "
                + "'welcomeChan' INTEGER, "
                + "'welcomeMSG' TEXT, "
                + "'welcomeMSGEnabled' TEXT, "
                + "'ticketChan' INTEGER, "
                + "'modRole' INTEGER, "
                + "'staffRole' INTEGER "
                + "'supportRole' INTEGER, "
                + "'modlogChan' INTEGER "
                + ");";

        String sqlUsers = "CREATE TABLE IF NOT EXISTS wb_users("
                + "'id' INTEGER PRIMARY KEY, "
                + "'name' TEXT, "
                + "'group' TEXT, "
                + "'xp' INTEGER, "
                + "'level' INTEGER, "
                + "'muted' BOOLEAN "
                + ");";

        String sqlTickets = "CREATE TABLE IF NOT EXISTS wb_tickets("
                + "'id' INTEGER PRIMARY KEY, "
                + "'ticketID' INTEGER, "
                + "'user' TEXT, "
                + "'reason' TEXT, "
                + "'level' INTEGER, "
                + "'finished' INTEGER, "
                + "'department' TEXT"
                + ");";

        try(SqliteQuery cmd = db.getQuery(sqlGuilds)) {
            cmd.execNonQuery();
            Logger.info("SQLite Guilds Table Created ");
        } catch(SQLException | IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to set up Guilds table for testing.");
        }

        try(SqliteQuery cmd = db.getQuery(sqlUsers)) {
            cmd.execNonQuery();
            Logger.info("SQLite Users Table Created ");
        } catch(SQLException | IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to set up Users table for testing.");
        }

        try(SqliteQuery cmd = db.getQuery(sqlTickets)) {
            cmd.execNonQuery();
            Logger.info("SQLite Ticket Table Created ");
        } catch(SQLException | IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to set up Ticket table for testing.");
        }
    }

}
