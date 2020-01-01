package me.itzdabbzz.wolfmc.commands.tickets;

import me.itzdabbzz.wolfmc.commands.moderation.Permissions;
import me.itzdabbzz.wolfmc.commands.moderation.SecureCommand;
import me.itzdabbzz.wolfmc.util.Constants;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class tremove extends SecureCommand {

	private static tremove instance;
	public static tremove getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new tremove();
	}

	private tremove() {
		super("tremove");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		//String name = "ticket-" + event.getMember().getEffectiveName() + " - ";
		Member member = Utilities.getMemberFromMention(event.getGuild(), args[0]);
		TextChannel textChannel = Utilities.getTextChannelFromMention(event.getGuild(), args[1]);
		textChannel.putPermissionOverride(member).deny(Constants.TICKET).queue();
		Message ticketAdd = new MessageBuilder()
				.setEmbed(new EmbedBuilder()
						.setDescription("Removed " + member.getEffectiveName() + " to the ticket.")
						.setColor(Constants.embedRed)
						.setAuthor("WolfMC Ticket System", null, "https://us.123rf.com/450wm/urfandadashov/urfandadashov1809/urfandadashov180901225/109135155-live-support-vector-icon-isolated-on-transparent-background-live-support-logo-concept.jpg?ver=6")
						.build()).build();
		textChannel.sendMessage(ticketAdd).queue();

		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`tremove` <user> -- Removes a user to a ticket"};
	}

	@Override
	public String getDescription() {
		return "Removes a user to a ticket";
	}

	@Override
	public List<String> getValidKeySet() {
		return Arrays.asList("ticket.useremove");
	}

	@Override
	public boolean hasPermissions(Member member, String... args) {
		if(args.length == 0) return true;

		String key = null;

		if("tremove".equals(args[0]))
			key = "ticket.useremove";
		else return true;

		return Permissions.getInstance().hasPermissionsFor(member, key);
	}

	@Override
	protected void unload() { }

	private Member getMember(Guild guild, String id){
		Member member;
		try{
			member = guild.getMemberById(id);
		}catch(Exception ex){
			member = null;
		}

		return member;
	}

	private Role getRole(Guild guild, String id){
		Role role;
		try{
			role = guild.getRoleById(id);
		}catch(Exception ex){
			role = null;
		}

		return role;
	}
}