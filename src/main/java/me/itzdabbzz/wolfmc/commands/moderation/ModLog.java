package me.itzdabbzz.wolfmc.commands.moderation;

import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class ModLog extends SecureCommand {

	private static ModLog instance;
	public static ModLog getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new ModLog();
	}

	private long startTime;

	private ModLog() {
		super("modlog");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;
		
		//Respond.async(event.getChannel(), "This bot has been online for " + Utilities.formatTime(System.currentTimeMillis() - startTime) + ".");

		Message message = event.getMessage();
		if("setchannel".equals(args[0])){
			Respond.async(event.getChannel(), "Channel");
		}else
		if("setcolor".equals(args[0])){
			Respond.async(event.getChannel(), "Color");
		}
		if("setresponse".equals(args[0])){
			Respond.async(event.getChannel(), "Set Response");
		}
		if("response".equals(args[0])){
			Respond.async(event.getChannel(), "Response");
		}


		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`modlog` -- Configure Modlog Settings"};
	}

	@Override
	public String getDescription() {
		return "Configure mod log settings";
	}

	@Override
	protected void unload() {
		instance = null;
	}

	@Override
	public List<String> getValidKeySet() {
		return Arrays.asList("modlog.setchannel", "modlog.setcolor", "modlog.setresponse");
	}

	@Override
	public boolean hasPermissions(GuildMessageReceivedEvent event, String... args) {
		if(args.length == 0) return true;

		String key = null;

		if("setchannel".equals(args[0]))
			key = "modlog.setchannel";
		else if("setcolor".equals(args[0]))
			key = "modlog.setcolor";
		else if("setresponse".equals(args[0]))
			key =  "modlog.setresponse";
		else if("response".equals(args[0]))
			key = "modlog.response";
		else return true;

		return Permissions.getInstance().hasPermissionsFor(event.getMember(), key);
	}
}