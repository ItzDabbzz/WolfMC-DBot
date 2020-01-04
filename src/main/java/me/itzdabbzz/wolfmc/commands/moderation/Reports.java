package me.itzdabbzz.wolfmc.commands.moderation;


import me.vem.jdab.cmd.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;

public class Reports extends Command {

    private static Reports instance;
    public static Reports getInstance() {
        return instance;
    }

    public static void initialize() {
        if(instance == null)
            instance = new Reports();
    }

    private Reports() {
        super("report");

    }

    @Override
    public boolean run(GuildMessageReceivedEvent event, String... args) {
        if(!super.run(event, args))
            return false;

        User traitor = event.getMessage().getMentionedUsers().get(0);
        Member author = event.getMember();
        //TODO: Fix reason from displaying "@!517493459641171990> Hes a big meanie head"
        StringBuilder argsString = new StringBuilder();
        Arrays.stream(args).forEach(s -> argsString.append(" " + s));
        String reason = argsString.toString().replace("@" + event.getGuild().getMember(traitor).getAsMention(), "").substring(2);

        MessageEmbed embedBuilder = new EmbedBuilder()
                .setAuthor(author.getEffectiveName() + " submitted a report.", null, author.getUser().getAvatarUrl())
                .addField("Author", author.getEffectiveName() + " (" + author.getAsMention() + ")", true)
                .addField("Reported Member", traitor.getName() + " (" + traitor.getAsMention() + ")", true)
                .addField("Reason", reason, false)
                .build();

        event.getGuild().getTextChannelById( 620316091477917728L ).sendMessage( embedBuilder ).queue();

        author.getUser().openPrivateChannel().complete().sendMessage(
                new EmbedBuilder()
                        .setDescription("Thanks for your report submit.\nYour report got send to all WolfMC staff.")
                        .build()
        ).queue();

        return true;
    }

    @Override
    public String[] usages() {
        return new String[] {"`report <@mention> <reason>` -- Reports a user"};
    }

    @Override
    public String getDescription() {
        return "Reports a user";
    }


    @Override
    public boolean hasPermissions( Member member, String... args) { return true; }

    @Override
    protected void unload() {
        instance = null;
    }
}