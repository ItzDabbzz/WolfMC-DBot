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
import me.vem.jdab.utils.*;
import me.vem.jdab.sqlite.*;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class WolfBot {

    public static DiscordBot getClient(){
        return DiscordBot.getInstance();
    }
    public static Utils utils;
    public static Utils getUtils() { return utils; }

    public static void main(String[] args) {
        Logger.setupFileLogging();
        Version.initialize(0,0,1,9, "WolfMC");
        Console.initialize();
        Logger.infof("Hello World! From %s", Version.getVersion());

        String tokenFile = args.length > 0 ? fetchToken(args[0]) : "config/token.txt";
        DiscordBot.initialize(fetchToken(tokenFile));
        SQLiteSetup();

        //Permissions is critical to the function of several other commands, so it must be initialized first.
        Permissions.initialize();
        //DiscordBot.getInstance().addEventListener(ReactionListener.getInstance());
        DiscordBot.getInstance().addEventListener(new MessageListeners());
        DiscordBot.getInstance().addEventListener(Monitor.getInstance());
        DiscordBot.getInstance().addEventListener(new me.itzdabbzz.wolfmc.data.EXPSystem());

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
        } catch (IOException e) {
            return null;
        }
    }

    private static SqliteDatabase db;

    public static void SQLiteSetup() {
        SqliteDatabase.setDefaultConnection("wolfbot.db");
        db = SqliteDatabase.create();

        String sql = "CREATE TABLE IF NOT EXISTS wb_users("
                + "id INTEGER PRIMARY KEY, "
                + "name TEXT, "
                + "group TEXT, "
                + "xp INTEGER, "
                + "level INTEGER, "
                + "muted TEXT "
                + ");";

        try(SqliteQuery cmd = db.getQuery(sql)){
            cmd.execNonQuery();
            Logger.info("SQLite Table Created ");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to set up table for testing.");
        }
    }

}
