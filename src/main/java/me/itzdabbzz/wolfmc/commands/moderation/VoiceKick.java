package me.itzdabbzz.wolfmc.commands.moderation;


import me.vem.jdab.utils.Utilities;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VoiceKick extends SecureCommand{

    private static VoiceKick instance;
    public static VoiceKick getInstance() {
        return instance;
    }

    public static void initialize() {
        if(instance == null)
            instance = new VoiceKick();
    }

    private VoiceKick() {
        super("vkick");

    }

    @Override
    public boolean run(GuildMessageReceivedEvent event, String... args) {
        if(!super.run(event, args))
            return false;

        User temp = Utilities.getUserFromMention( args[0] );

        if (temp == null) {
            event.getChannel().sendMessage("The user `{0}` does not exist.").queue();
            return true;
        }

        if (!event.getGuild().getMember(temp).getVoiceState().inVoiceChannel()) {
            event.getChannel().sendMessage("User is not in voice channel.").queue();
            return true;
        }

        event.getGuild().createVoiceChannel( "kick-" + temp.getIdLong()).queue(voiceChannel -> {
            event.getGuild().moveVoiceMember( event.getGuild().getMember( temp ), ( VoiceChannel)voiceChannel ).queue(o -> {
                event.getChannel().sendMessage( "Successfully Voice Kicked " + temp.getName() );
                voiceChannel.delete().queueAfter( 1, TimeUnit.SECONDS );
            });
        });

        return true;
    }

    @Override
    public String[] usages() {
        return new String[] {"`vkick <member>` -- vkick a user from the Voice Chat"};
    }

    @Override
    public String getDescription() {
        return "vkick a user from the Voice Chat";
    }

    @Override
    public List<String> getValidKeySet() {
        return Arrays.asList("moderation.vkick");
    }

    @Override
    public boolean hasPermissions( Member member, String... args) {
        return Permissions.getInstance().hasPermissionsFor(member, "moderation.vkick");
    }

    @Override
    protected void unload() {
        instance = null;
    }
}