package me.itzdabbzz.wolfmc.commands.general;

import me.itzdabbzz.wolfmc.util.Constants;
import me.itzdabbzz.wolfmc.util.MineStat;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Respond;
import me.vem.jdab.utils.emoji.Emoji;
import me.vem.jdab.utils.emoji.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.temporal.ChronoUnit;

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

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		MineStat ms = new MineStat("216.82.132.117", 25565);
		if(ms.isServerUp())
		{
			EmbedBuilder em = new EmbedBuilder().setColor( Constants.embedPurple);
			em.setDescription("<:MCDirt:665978962240864276> WolfMC Network Status")
					.setColor(Constants.embedLime)
					.addField("`Players`", ms.getCurrentPlayers(), false)
					.addField("Status", Emojis.CHECK + "", false);

			event.getChannel().sendMessage(em.build()).queue();
		}else{
			EmbedBuilder em = new EmbedBuilder().setColor( Constants.embedPurple);
			em.setDescription("<:MCDirt:665978962240864276> WolfMC Network Status")
					.setColor(Constants.embedRed)
					.addField("`SERVER OFFLINE`", "", false);

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