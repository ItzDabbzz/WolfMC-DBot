package me.itzdabbzz.wolfmc.commands.general;

import me.itzdabbzz.wolfmc.util.Constants;
import me.itzdabbzz.wolfmc.util.mcping.MinecraftPing;
import me.itzdabbzz.wolfmc.util.mcping.MinecraftPingOptions;
import me.itzdabbzz.wolfmc.util.mcping.MinecraftPingReply;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.emoji.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;

public class Status extends Command{

	private static Status instance;
	public static Status getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new Status();
	}

	private Status() {
		super("status");
	}

	@lombok.SneakyThrows
	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		MinecraftPingReply data = null;
		try {
			data = new MinecraftPing().getPing(new MinecraftPingOptions().setHostname("216.82.132.117").setPort(25565));
		} catch(IOException e) {
			e.printStackTrace();
		}

		//MineStat ms = new MineStat("216.82.132.117", 25565);
		//if(ms.isServerUp())


		if(data.getDescription() != null)
		{
			EmbedBuilder em = new EmbedBuilder().setColor( Constants.embedPurple);
			em.setDescription("<:MCDirt:665978962240864276> WolfMC Network Status")
					.setColor(Constants.embedLime)
					//.addField("`Players`", ms.getCurrentPlayers(), true)
					//.addField("Status", Emojis.CHECK + "", true);

					.addField("`Players`", data.getPlayers().getOnline() + "", true)
					.addField("Status", Emojis.CHECK + "", true);

			event.getChannel().sendMessage(em.build()).queue();
		}else{
			EmbedBuilder em = new EmbedBuilder().setColor( Constants.embedPurple);
			em.setDescription("<:MCDirt:665978962240864276> WolfMC Network Status")
					.setColor(Constants.embedRed)
					.addField("`<:MCredstone:665978962211766292> SERVER OFFLINE`", "", false);

			event.getChannel().sendMessage(em.build()).queue();
		}

		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`status` -- Displays the status of the mc network."};
	}

	@Override
	public boolean hasPermissions( Member member , String... args ) {
		return true;
	}

	@Override
	public String getDescription() {
		return "Displays the status of the MC servers";
	}

	@Override
	protected void unload() {
		instance = null;
	}
}