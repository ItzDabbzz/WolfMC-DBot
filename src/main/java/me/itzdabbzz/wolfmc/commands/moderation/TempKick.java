package me.itzdabbzz.wolfmc.commands.moderation;

import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class TempKick extends Command{

	private static TempKick instance;
	public static TempKick getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new TempKick();
	}

	private TempKick() {
		super("tkick");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
		if(mentionedMembers.isEmpty()) {
			Respond.async(event.getChannel(), "You must mention who you want to be kicked");
			return false;
		}
//		mentionedMembers.remove("WolfBot");

		Member member = event.getGuild().getMemberById(mentionedMembers.get(0).getId());
		System.out.println(member.getNickname());
		member.kick().queue(success-> {
			Respond.async(event.getChannel(),"Successfully kicked " + mentionedMembers.get(0).getUser().getName());
		}, error->{
			Respond.async(event.getChannel(),"Unable to kick " + mentionedMembers.get(0).getUser().getName() + ": " + error);
		});

		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`kick <member>` -- kicks a user from the server"};
	}

	@Override
	public String getDescription() {
		return "Kicks a user from the server";
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