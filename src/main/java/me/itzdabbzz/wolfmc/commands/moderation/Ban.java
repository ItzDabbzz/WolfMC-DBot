package me.itzdabbzz.wolfmc.commands.moderation;

import me.itzdabbzz.wolfmc.mod.ModAction;
import me.itzdabbzz.wolfmc.mod.ModlogHandler;
import me.itzdabbzz.wolfmc.util.GuildUtils;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import sun.misc.MessageUtils;

import java.util.Arrays;
import java.util.List;

public class Ban extends SecureCommand{

	private static Ban instance;
	public static Ban getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new Ban();
	}

	private Ban() {
		super("kick");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		if (args.length >= 1) {
			User target = GuildUtils.getUser(args[0], guild.getGuildId());
			if (target == null) {
				MessageUtils.sendErrorMessage("We cannot find that user! Try their ID if you didn't already.", channel, sender);
				return;
			}
			String reason = null;
			if (args.length >= 2)
				reason = MessageUtils.getMessage(args, 1);

			ModlogHandler.getInstance().handleAction(guild, channel, sender, target, ModAction.BAN, reason);
		} else {
			MessageUtils.sendUsage(this, channel, sender, args);
		}

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
	public List<String> getValidKeySet() {
		return Arrays.asList("stream.setchannel", "stream.adduser", "stream.setresponse", "stream.removeuser");
	}

	@Override
	public boolean hasPermissions(GuildMessageReceivedEvent event, String... args) {
		if(args.length == 0) return true;

		String key = null;

		if("add".equals(args[0]))
			key = "stream.adduser";
		else if("remove".equals(args[0]))
			key = "stream.removeuser";
		else if("channel".equals(args[0]))
			key =  "stream.setchannel";
		else if("response".equals(args[0]))
			key = "stream.setresponse";
		else return true;

		return Permissions.getInstance().hasPermissionsFor(event.getMember(), key);
	}

	@Override
	protected void unload() {
		instance = null;
	}
}