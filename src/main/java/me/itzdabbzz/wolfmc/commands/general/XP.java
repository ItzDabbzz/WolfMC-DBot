package me.itzdabbzz.wolfmc.commands.general;

import me.itzdabbzz.wolfmc.EXPSystem;
import me.vem.jdab.cmd.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class XP extends Command{

	private static XP instance;
	public static XP getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new XP();
	}

	private XP() {
		super("xp");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		EXPSystem xp = EXPSystem.getInstance();

		//Respond.async(event.getChannel(), ":ping_pong: Your Ping Is " + DiscordBot.getInstance().getJDA() + ".");
		event.getChannel().sendMessage("You have " + xp.getPlayerXP(event.getMember()) + " EXP").queue();
		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`xp` -- Shows how much XP you have."};
	}

	@Override
	public boolean hasPermissions( Member member , String... args ) {
		return false;
	}

	@Override
	public String getDescription() {
		return "Shows your XP";
	}


	@Override
	protected void unload() {
		instance = null;
	}
}