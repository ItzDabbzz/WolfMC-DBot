package me.itzdabbzz.wolfmc.commands.tickets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.itzdabbzz.wolfmc.commands.moderation.Permissions;
import me.itzdabbzz.wolfmc.util.ChannelTracker;
import me.itzdabbzz.wolfmc.util.Constants;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.cmd.Configurable;
import me.vem.jdab.utils.ExtFileManager;
import me.vem.jdab.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class setChannel extends Command {

	private static setChannel instance;
	public static setChannel getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new setChannel();
	}

	private setChannel() {
		super("setChannel");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		Member member = event.getMember();
		TextChannel channel = event.getChannel();

		if(member.hasPermission(Permission.ADMINISTRATOR)) {
			if(!(args.length == 0)) {
				String channelName = args[0];
				List<TextChannel> channels = event.getGuild().getTextChannelsByName(channelName, true);
				if(!channels.isEmpty()) {
					TextChannel support = channels.get(0);
					ChannelTracker.setChannel(support);
					Message setChannel = new MessageBuilder()
							.setEmbed(new EmbedBuilder()
									.setDescription("Set Ticket Open Channel To "+ support)
									.setColor(Constants.embedOrange)
									.setAuthor("WolfMC Ticket System", null, "https://us.123rf.com/450wm/urfandadashov/urfandadashov1809/urfandadashov180901225/109135155-live-support-vector-icon-isolated-on-transparent-background-live-support-logo-concept.jpg?ver=6")
									.build()).build();
					channel.sendMessage(setChannel).queue();
				} else {
					Message setChannel = new MessageBuilder()
							.setEmbed(new EmbedBuilder()
									.setDescription(channelName + " does not exist!")
									.setColor(Constants.embedOrange)
									.setAuthor("WolfMC Ticket System", null, "https://us.123rf.com/450wm/urfandadashov/urfandadashov1809/urfandadashov180901225/109135155-live-support-vector-icon-isolated-on-transparent-background-live-support-logo-concept.jpg?ver=6")
									.build()).build();
					channel.sendMessage(setChannel).queue();
				}
			} else {
				Message setChannel = new MessageBuilder()
						.setEmbed(new EmbedBuilder()
								.setDescription("You have entered an invalid amount of arguments!")
								.setColor(Constants.embedOrange)
								.setAuthor("WolfMC Ticket System", null, "https://us.123rf.com/450wm/urfandadashov/urfandadashov1809/urfandadashov180901225/109135155-live-support-vector-icon-isolated-on-transparent-background-live-support-logo-concept.jpg?ver=6")
								.build()).build();
				channel.sendMessage(setChannel).queue();
			}
		} else {
			channel.sendMessage("You do not have the permission to do that!").queue();
			Message setChannel = new MessageBuilder()
					.setEmbed(new EmbedBuilder()
							.setDescription("You do not have the permission to do that!")
							.setColor(Constants.embedRed)
							.setAuthor("WolfMC Ticket System", null, "https://us.123rf.com/450wm/urfandadashov/urfandadashov1809/urfandadashov180901225/109135155-live-support-vector-icon-isolated-on-transparent-background-live-support-logo-concept.jpg?ver=6")
							.build()).build();
			channel.sendMessage(setChannel).queue();
		}
		return true;
	}


	
	@Override
	public String[] usages() {
		return new String[] {"`setChannel` -- ."};
	}

	@Override
	public String getDescription() {
		return "setChannel";
	}

	@Override
	public boolean hasPermissions(Member member, String... args) {
		return true;
	}

	@Override
	protected void unload() {
		instance = null;
	}
}