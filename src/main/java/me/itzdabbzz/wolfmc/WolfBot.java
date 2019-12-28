package me.itzdabbzz.wolfmc;

import me.itzdabbzz.wolfmc.commands.general.Ping;
import me.itzdabbzz.wolfmc.commands.general.XP;
import me.itzdabbzz.wolfmc.commands.moderation.Kick;
import me.itzdabbzz.wolfmc.commands.moderation.ModLog;
import me.itzdabbzz.wolfmc.commands.reaction.ReactionListener;
import me.itzdabbzz.wolfmc.commands.tickets.setChannel;
import me.itzdabbzz.wolfmc.commands.tickets.tadd;
import me.vem.jdab.DiscordBot;
import me.itzdabbzz.wolfmc.commands.moderation.Permissions;
import me.itzdabbzz.wolfmc.commands.general.StreamTrack;
import me.vem.jdab.utils.Console;
import me.vem.jdab.utils.ExtFileManager;
import me.vem.jdab.utils.Logger;
import me.vem.jdab.utils.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WolfBot {

    public static DiscordBot getClient() {
        return discordBot;
    }

    public static DiscordBot discordBot;



    public static void main(String[] args) {
        Logger.setupFileLogging();
        Version.initialize(0,0,1,1, "WolfMC");
        Console.buildConsole();

        Logger.infof("Hello World! From %s", Version.getVersion());

        String tokenFile = args.length > 0 ? fetchToken(args[0]) : "config/token.txt";
        discordBot.initialize(fetchToken(tokenFile));

        //Permissions is critical to the function of several other commands, so it must be initialized first.
        Permissions.initialize();
        discordBot.getInstance().addEventListener(ReactionListener.getInstance());
        discordBot.getInstance().addEventListener(new MessageListeners());

        DiscordBot.getInstance().addEventListener(new EXPSystem());

        EXPSystem expSystem = new EXPSystem();
        expSystem.startTimer();

        StreamTrack.initialize();
        Ping.initialize();
        ModLog.initialize();
        XP.initialize();
        Kick.initialize();
        setChannel.initialize();
        tadd.initialize();
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

}
