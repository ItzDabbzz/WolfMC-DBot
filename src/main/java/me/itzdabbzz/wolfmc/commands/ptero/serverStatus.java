package me.itzdabbzz.wolfmc.commands.ptero;

import com.stanjg.ptero4j.PteroAdminAPI;
import com.stanjg.ptero4j.entities.panel.admin.Server;
import me.itzdabbzz.wolfmc.Constants;
import me.itzdabbzz.wolfmc.commands.moderation.SecureCommand;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class serverStatus extends SecureCommand {

	private static serverStatus instance;
	public static serverStatus getInstance() {
		return instance;
	}
	private static PteroAdminAPI api = new PteroAdminAPI("http://panel.itzdabbzz.me/", "6Q4cE2Jq8m9AaZvYxMaQKE85NIuRD0fGSq18zAzgvnOdluTM");
	public static void initialize() {
		if(instance == null)
			instance = new serverStatus();
	}

	private serverStatus() {
		super("server");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;
		Server server = api.getServersController().getServer(Integer.parseInt(args[0]));
		//Respond.async(event.getChannel(), ":ping_pong: Your Ping Is " + DiscordBot.getInstance().getJDA() + ".");
		EmbedBuilder embed = new EmbedBuilder().setColor(Constants.embedCyan);
		embed.addField("Server: ", server.getName(), true);
		embed.addField("Is Suspended?: ", String.valueOf(server.isSuspended()), true);
		embed.addField("CPU: ", String.valueOf(server.getLimits().getIo()), true);
		embed.addField("Ram: ", String.valueOf(server.getLimits().getMemory()), true);
		embed.setAuthor(event.getGuild().getName(), null, event.getGuild().getIconUrl());
		embed.setTimestamp(Instant.now());
		Respond.async(event.getChannel(), embed);
		//Respond.async(event.getChannel(), "Ping: " + server.getName()  + "ms | Websocket: " + event.getJDA().getGatewayPing() + "ms");
		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`server` <id> -- Displays server info."};
	}

	@Override
	public String getDescription() {
		return "Shows server status";
	}

	@Override
	public boolean hasPermissions(GuildMessageReceivedEvent event, String... args) {
		return true;
	}

	@Override
	protected void unload() {
		instance = null;
	}

	@Override
	public List<String> getValidKeySet() {
		return null;
	}
}