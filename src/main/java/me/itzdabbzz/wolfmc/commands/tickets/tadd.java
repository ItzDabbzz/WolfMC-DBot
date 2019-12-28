package me.itzdabbzz.wolfmc.commands.tickets;

import com.google.gson.Gson;
import me.vem.jdab.cmd.Command;
import me.vem.jdab.cmd.Configurable;
import me.vem.jdab.utils.ExtFileManager;
import me.vem.jdab.utils.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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

		String name = "ticket-" + event.getMember().getEffectiveName() + " - ";

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