package me.itzdabbzz.wolfmc.commands;

import me.itzdabbzz.wolfmc.ChannelTracker;
import me.itzdabbzz.wolfmc.WolfBot;
import me.vem.jdab.DiscordBot;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class setChannel extends Command{

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
					channel.sendMessage("The support channel was set to " + support).queue();
				} else {
					channel.sendMessage(channelName + " does not exist!").queue();
				}
			} else {
				channel.sendMessage("You have entered an invalid amount of arguments!").queue();
			}
		} else {
			channel.sendMessage("You do not have the permission to do that!").queue();
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
	public boolean hasPermissions(GuildMessageReceivedEvent event, String... args) {
		return true;
	}

	@Override
	protected void unload() {
		instance = null;
	}
}