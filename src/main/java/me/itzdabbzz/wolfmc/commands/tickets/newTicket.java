package me.itzdabbzz.wolfmc.commands.tickets;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.itzdabbzz.wolfmc.data.Ticket;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.cmd.Configurable;
import me.vem.jdab.utils.ExtFileManager;
import me.vem.jdab.utils.Logger;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class newTicket extends Command implements Configurable {

	private static newTicket instance;
	public static newTicket getInstance() {
		return instance;
	}

	public static void initialize() {
		if(instance == null)
			instance = new newTicket();
	}

	private newTicket() {
		super("tnew");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;



		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`tnew` -- Creates a new ticket."};
	}

	@Override
	public String getDescription() {
		return "Creates a new Ticket";
	}

	@Override
	public boolean hasPermissions(GuildMessageReceivedEvent event, String... args) {
		return true;
	}

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

	@Override
	public void save() {
		try {
			PrintWriter out = ExtFileManager.getConfigOutput("tickets.json");
			//out.print(ExtFileManager.getGsonPretty().toJson(prefixDatabase));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Logger.infof("Prefix database saved...");
	}

	@Override
	public void load() {
		//prefixDatabase = new HashMap<>();

		File configFile = ExtFileManager.getConfigFile("tickets.json");
		if(configFile == null) return;

		String content = ExtFileManager.readFileAsString(configFile);
		if(content == null || content.length() == 0) return;

		Gson gson = ExtFileManager.getGsonPretty();

		//prefixDatabase = gson.fromJson(content, new TypeToken<HashMap<Long, String>>(){}.getType());
	}

	@Override
	protected void unload() {
		save();
		instance = null;
	}
}