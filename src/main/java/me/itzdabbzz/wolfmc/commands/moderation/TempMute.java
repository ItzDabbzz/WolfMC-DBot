package me.itzdabbzz.wolfmc.commands.moderation;

import me.itzdabbzz.wolfmc.util.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TempMute extends SecureCommand {

	private static TempMute instance;
	public static TempMute getInstance() {
		return instance;
	}
	int counter = 0;

	public static void initialize() {
		if(instance == null)
			instance = new TempMute();
	}

	private TempMute() {
		super("tmute");

	}

	@Override
	public boolean run(GuildMessageReceivedEvent event, String... args) {
		if(!super.run(event, args))
			return false;

		/*List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
		if(mentionedMembers.isEmpty()){
			Respond.async(event.getChannel(), "You must mention who you want to be muted");
			return true;
		}

		Member toMute = mentionedMembers.get(0);

		Role muteRole = event.getGuild().getRoles().stream().filter(r-> r.getName().equals("Muted")).findFirst().orElse(null);
		if(muteRole == null) {
			Respond.async(event.getChannel(), "No role named 'Muted' Found");
			return true;
		}

		toMute.getRoles().add(muteRole);*/

		if("tempmute".equals(args[0])){
			if(args.length <= 2){
				Utils.sendErrorMessage("", event.getChannel());
			}else{
				Member target = event.getMessage().getMentionedMembers().get(0);
				tempMute(target, Utils.parseTimeAmount(args[2]), Utils.parseTimeUnit(args[2]));
				if(args.length >= 4) {
					String reason = "";
					for(int i = 3; i <args.length; i++){
						reason += args[1] + "";
						ModerationEmbeds.tempMuteEmbed(target, event.getMember(), reason, event.getGuild().getTextChannelById(657726698800021535L));

					}
				}
				ModerationEmbeds.tempMuteEmbed(target, event.getMember(), " ", event.getGuild().getTextChannelById(657726698800021535L));
			}
		}

		return true;
	}
	
	@Override
	public String[] usages() {
		return new String[] {"`tmute <member>` -- kicks a user from the server"};
	}

	@Override
	public String getDescription() {
		return "tmute a user from the server";
	}


	@Override
	public boolean hasPermissions(Member member, String... args) {
		return Permissions.getInstance().hasPermissionsFor(member, "moderation.tempmute");
	}

	@Override
	public List<String> getValidKeySet() {
		return Arrays.asList("moderation.tempmute");
	}

	@Override
	protected void unload() {
		instance = null;
	}

	private void tempMute(Member target, int time, TimeUnit unit){
		Role muted = target.getGuild().getRoleById(648680031748489226L);
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			@Override
			public void run(){
				counter++;
				target.getGuild().addRoleToMember(target, muted).queue();
				 if(counter == 2){
					 target.getGuild().removeRoleFromMember(target, muted).queue();
					 this.cancel();
				 }
			}
		};
		switch (unit){
			case SECONDS:
				timer.schedule(task, 0, time * 1000);
				break;

			case MINUTES:
				timer.schedule(task, 0, (time * 1000) * 60);
				break;

			case HOURS:
				timer.schedule(task, 0, ((time * 1000) * 60) * 60);
				break;
		}
	}
}