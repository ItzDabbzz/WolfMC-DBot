package me.itzdabbzz.wolfmc.commands.tickets;

import com.google.gson.Gson;
import me.itzdabbzz.wolfmc.util.Constants;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.cmd.Configurable;
import me.vem.jdab.utils.ExtFileManager;
import me.vem.jdab.utils.Logger;
import me.vem.jdab.utils.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class tadd extends Command  {

	private static tadd instance;
	public static tadd getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new tadd();
	}

	private tadd() {
		super("tadd");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		//String name = "ticket-" + event.getMember().getEffectiveName() + " - ";
		Member member = Utilities.getMemberFromMention(event.getGuild(), args[0]);
		TextChannel textChannel = Utilities.getTextChannelFromMention(event.getGuild(), args[1]);
		textChannel.putPermissionOverride(member).setAllow(Constants.TICKET).queue();
		Message ticketAdd = new MessageBuilder()
				.setEmbed(new EmbedBuilder()
						.setDescription("Added " + member.getEffectiveName() + " to the ticket.")
						.setColor(new Color(0x2CD900))
						.setAuthor("WolfMC Ticket System", null, "https://us.123rf.com/450wm/urfandadashov/urfandadashov1809/urfandadashov180901225/109135155-live-support-vector-icon-isolated-on-transparent-background-live-support-logo-concept.jpg?ver=6")
						.build()).build();
		textChannel.sendMessage(ticketAdd).queue();

		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`tadd` -- Adds a user to a ticket"};
	}

	@Override
	public String getDescription() {
		return "Adds a user to a ticket";
	}

	@Override
	public boolean hasPermissions(GuildMessageReceivedEvent event, String... args) {
		return true;
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