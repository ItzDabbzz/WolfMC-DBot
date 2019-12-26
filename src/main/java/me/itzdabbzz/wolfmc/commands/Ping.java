package me.itzdabbzz.wolfmc.commands;

import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.time.temporal.ChronoUnit.MILLIS;

public class Ping extends Command{

	private static Ping instance;
	public static Ping getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new Ping();
	}

	private Ping() {
		super("ping");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;
		
		//Respond.async(event.getChannel(), ":ping_pong: Your Ping Is " + DiscordBot.getInstance().getJDA() + ".");
		long ping = event.getMessage().getTimeCreated().until(event.getMessage().getTimeCreated(), ChronoUnit.MILLIS);
		Respond.async(event.getChannel(), "Ping: " + ping  + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms");
		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`ping` -- Displays the amount of ping that the bot has."};
	}

	@Override
	public String getDescription() {
		return "Prints the bots ping";
	}

	@Override
	public boolean hasPermissions(GuildMessageReceivedEvent event, String... args) {
		return true;
	}

	@Override
	protected void unload() {
		instance = null;
	}
}