package me.itzdabbzz.wolfmc.commands.moderation;

import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class Kick extends SecureCommand{

	private static Kick instance;
	public static Kick getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new Kick();
	}

	private Kick() {
		super("kick");

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
			ModerationEmbeds.kickEmbed( member, event.getMember(), "", event.getGuild().getTextChannelById( 657726698800021535L ) );
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
	public List<String> getValidKeySet() { return Arrays.asList("moderation.kick"); }

	@Override
	public boolean hasPermissions(Member member, String... args) {
		if(args.length == 0) return true;

		String key = null;

		if("kick".equals(args[0]))
			key = "moderation.kick";
		else return true;

		return Permissions.getInstance().hasPermissionsFor(member, key);
	}

	@Override
	protected void unload() {
		instance = null;
	}
}