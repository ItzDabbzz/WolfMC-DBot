package me.itzdabbzz.wolfmc.commands.moderation;

import me.itzdabbzz.wolfmc.util.GuildUtils;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class TempMute extends Command{

	private static TempMute instance;
	public static TempMute getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new TempMute();
	}

	private TempMute() {
		super("tmute");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
		if(mentionedMembers.isEmpty()){
			Respond.async(event.getChannel(), "You must mention who you want to be muted");
			return true;
		}

		Member toMute = mentionedMembers.get(0);

		Role muteRole = event.getGuild().getRoles().stream().filter(r-> r.getName().equals("Muted")).findFirst().orElse(null);
		if(muteRole == null) {
			Respond.async(event.getChannel(), "No role named 'Muted' Found");
			return true;
		}

		toMute.getRoles().add(muteRole);


		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`tmute <member>` -- kicks a user from the server"};
	}

	@Override
	public String getDescription() {
		return "tmute a user from the server";
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