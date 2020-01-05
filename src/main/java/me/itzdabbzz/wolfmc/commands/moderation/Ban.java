package me.itzdabbzz.wolfmc.commands.moderation;



import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.entities.Member;
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
        super("ban");

    }

    @Override
    public boolean run(GuildMessageReceivedEvent event, String... args) {
        if(!super.run(event, args))
            return false;

        List<Member> mentionedMembers = event.getMessage().getMentionedMembers();
        if(mentionedMembers.isEmpty()) {
            Respond.async(event.getChannel(), "You must mention who you want to be ban");
            return false;
        }
//		mentionedMembers.remove("WolfBot");

        Member member = event.getGuild().getMemberById(mentionedMembers.get(0).getId());
        System.out.println(member.getNickname());
        member.ban(7).queue(success-> {
            Respond.async(event.getChannel(),"Successfully banned " + mentionedMembers.get(0).getUser().getName());
            ModerationEmbeds.banEmbed( mentionedMembers.get( 0 ), event.getMember(), "", event.getGuild().getTextChannelById( 657726698800021535L ) );
        }, error->{
            Respond.async(event.getChannel(),"Unable to ban " + mentionedMembers.get(0).getUser().getName() + ": " + error);
        });


        return true;
    }

    @Override
    public String[] usages() {
        return new String[] {"`ban <member>` -- bans a user from the server"};
    }

    @Override
    public String getDescription() {
        return "Bans a user from the server";
    }

    @Override
    public List<String> getValidKeySet() {
        return Arrays.asList("moderation.ban");
    }

    @Override
    public boolean hasPermissions( Member member, String... args) {
        return Permissions.getInstance().hasPermissionsFor(member, "moderation.ban");
    }

    @Override
    protected void unload() {
        instance = null;
    }
}