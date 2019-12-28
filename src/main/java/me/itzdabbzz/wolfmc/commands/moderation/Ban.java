package me.itzdabbzz.wolfmc.commands.moderation;



import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;


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