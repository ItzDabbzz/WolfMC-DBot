package me.itzdabbzz.wolfmc.commands.tickets;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import me.itzdabbzz.wolfmc.commands.moderation.Permissions;
import me.itzdabbzz.wolfmc.commands.moderation.SecureCommand;
import me.itzdabbzz.wolfmc.data.TicketBlacklist;
import me.itzdabbzz.wolfmc.util.Constants;
import me.vem.jdab.utils.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class tblacklist extends SecureCommand {

	private static tblacklist instance;
	public static tblacklist getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new tblacklist();
	}

	private tblacklist() {
		super("tblacklist");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		if("add".equals(args[0]))
		{
			TicketBlacklist.addPlayer(Utilities.getMemberFromMention(event.getGuild(), args[1]));
			Message ticketAdd = new MessageBuilder()
					.setEmbed(new EmbedBuilder()
							.setDescription("Blacklisted " + Utilities.getMemberFromMention(event.getGuild(), args[1]) + " to the ticket system.")
							.setColor(Constants.embedDPink)
							.setAuthor("WolfMC Ticket System", null, "https://us.123rf.com/450wm/urfandadashov/urfandadashov1809/urfandadashov180901225/109135155-live-support-vector-icon-isolated-on-transparent-background-live-support-logo-concept.jpg?ver=6")
							.build()).build();
			event.getChannel().sendMessage(ticketAdd).queue();
		}else
			if("remove".equals(args[0]))
			{
				TicketBlacklist.removePlayer(Utilities.getMemberFromMention(event.getGuild(), args[1]));
				Message ticketAdd = new MessageBuilder()
						.setEmbed(new EmbedBuilder()
								.setDescription("Removed " +Utilities.getMemberFromMention(event.getGuild(), args[1]) + " from the Blacklist to the ticket system.")
								.setColor(Constants.embedDPink)
								.setAuthor("WolfMC Ticket System", null, "https://us.123rf.com/450wm/urfandadashov/urfandadashov1809/urfandadashov180901225/109135155-live-support-vector-icon-isolated-on-transparent-background-live-support-logo-concept.jpg?ver=6")
								.build()).build();
				event.getChannel().sendMessage(ticketAdd).queue();
			}else
			{
				Message ticketAdd = new MessageBuilder()
						.setEmbed(new EmbedBuilder()
								.setDescription("Not Enough Args to complete the Blacklist command.")
								.setColor(Constants.embedDPink)
								.setAuthor("WolfMC Ticket System", null, "https://us.123rf.com/450wm/urfandadashov/urfandadashov1809/urfandadashov180901225/109135155-live-support-vector-icon-isolated-on-transparent-background-live-support-logo-concept.jpg?ver=6")
								.build()).build();
				event.getChannel().sendMessage(ticketAdd).queue();
			}

		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`tblacklist` <user> -- Blacklist's a user from creating a ticket"};
	}

	@Override
	public String getDescription() {
		return "Blacklist's a user from creating a ticket";
	}

	@Override
	public List<String> getValidKeySet() {
		return Arrays.asList("ticket.blacklist");
	}

	@Override
	public boolean hasPermissions(GuildMessageReceivedEvent event, String... args) {
		if(args.length == 0) return true;

		String key = null;

		if("tblacklist".equals(args[0]))
			key = "ticket.blacklist";
		else return true;

		return Permissions.getInstance().hasPermissionsFor(event.getMember(), key);
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