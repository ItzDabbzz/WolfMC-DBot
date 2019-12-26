package me.itzdabbzz.wolfmc;

import me.itzdabbzz.wolfmc.commands.Ping;
import me.itzdabbzz.wolfmc.commands.XP;
import me.itzdabbzz.wolfmc.commands.moderation.Kick;
import me.itzdabbzz.wolfmc.commands.moderation.ModLog;
import me.itzdabbzz.wolfmc.commands.reaction.ReactionListener;
import me.vem.jdab.DiscordBot;
import me.itzdabbzz.wolfmc.commands.moderation.Permissions;
import me.itzdabbzz.wolfmc.commands.StreamTrack;
import me.vem.jdab.utils.Console;
import me.vem.jdab.utils.ExtFileManager;
import me.vem.jdab.utils.Logger;
import me.vem.jdab.utils.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WolfBot {

    public static void main(String[] args) {
        Logger.setupFileLogging();
        Version.initialize(0,0,1,1, "WolfMC");
        Console.buildConsole();

        Logger.infof("Hello World! From %s", Version.getVersion());

        String tokenFile = args.length > 0 ? fetchToken(args[0]) : "token.txt";
        //DiscordBot.initialize(fetchToken(tokenFile));
        DiscordBot.initialize("NjA5NTYyNzcxODg5MDYxODk4.XXCpwg.KjTz41c5r-tQ6JfCBdDyOenmYwY");

        //Permissions is critical to the function of several other commands, so it must be initialized first.
        Permissions.initialize();
        DiscordBot.getInstance().addEventListener(ReactionListener.getInstance());

        DiscordBot.getInstance().addEventListener(new EXPSystem());

        EXPSystem expSystem = new EXPSystem();
        expSystem.startTimer();

        StreamTrack.initialize();
        Ping.initialize();
        ModLog.initialize();
        XP.initialize();
        Kick.initialize();
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
