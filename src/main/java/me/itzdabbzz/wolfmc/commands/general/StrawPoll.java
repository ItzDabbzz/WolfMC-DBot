package me.itzdabbzz.wolfmc.commands.general;

import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class StrawPoll extends Command{

	private static StrawPoll instance;
	public static StrawPoll getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new StrawPoll();
	}

	private StrawPoll() {
		super("poll");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		if(args.length == 0)
			return sendHelp(event.getChannel(), true);

		/**
		 * TODO: Create Strawpoll command
		 * @param title - The title of the StrawPoll
		 * @param options - The list of options as a List<String>
		 * @param isMulti - A boolean that determines if a user is allowed to vote multiple times. If true, then the same person can vote multiple times. This is false by default.
		 * @param hasCaptcha - A boolean that determines if there is a captcha to verify if a user is human. True enables the captcha. This is false by default.
		 * @param DupCheck - This is an enum of DupCheckType that determines the type of duplication checking to be used. This is DupCheckType.NORMAL by default
		 *
		 * @param DupCheck has three different values:
		 *
		 * DupCheckType.NORMAL - This is the normal duplication checking. This is enabled by default
		 * DupCheckType.PERMISSIVE - This makes the poll more lenient in vote duplication checking.
		 * DupCheckType.DISABLED - This disables duplication checking for the poll.
		 *
		 *  https://github.com/Samuel-Maddock/StrawPoll-Java-API
		 *
		 * StrawPoll strawPoll = new StrawPoll(title, options, isMulti, hasCaptcha, DupCheck);
		 */

		String title = args[0];
		List<String> options = new ArrayList<>();



		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`ping` -- Displays the amount of ping that the bot has."};
	}

	@Override
	public boolean hasPermissions( Member member , String... args ) {
		return true;
	}

	@Override
	public String getDescription() {
		return "Prints the bots ping";
	}

	@Override
	protected void unload() {
		instance = null;
	}
}