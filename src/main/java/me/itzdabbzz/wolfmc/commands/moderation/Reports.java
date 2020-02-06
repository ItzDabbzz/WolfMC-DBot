package me.itzdabbzz.wolfmc.commands.moderation;


import me.vem.jdab.cmd.Command;
import me.vem.jdab.utils.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("HH:mm:ss MMMM/dd/yyyy");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
    private GregorianCalendar day;
    @Override
    public boolean run(GuildMessageReceivedEvent event, String... args) {
        if(!super.run(event, args))
            return false;

        day = null;
        String date = dateTimeFormatter.format( Calendar.getInstance().getTime());


        User traitor = Utilities.getUserFromMention( args[0] );
        Member author = event.getMember();
        StringBuilder argsString = new StringBuilder();
        Arrays.stream(args).forEach(s -> argsString.append(" " + s));
        String reason = argsString.toString().replace(traitor.getAsMention(), "").substring(1);

            OffsetDateTime creation = event.getMessage().getTimeCreated();
            if(day == null || creation.getDayOfMonth() != day.get(GregorianCalendar.DAY_OF_MONTH) ||
                    creation.getMonthValue()-1 != day.get(GregorianCalendar.MONTH) ||
                    creation.getYear() != day.get(GregorianCalendar.YEAR)) {
                day = new GregorianCalendar(creation.getYear(), creation.getMonthValue()-1, creation.getDayOfMonth(), creation.getHour(), creation.getMinute(), creation.getSecond());

                MessageEmbed embedBuilder = new EmbedBuilder()
                        .setDescription( "[Report]" )
                        .setAuthor(author.getEffectiveName() + " submitted a report.", null, author.getUser().getAvatarUrl())
                        .addField("Author", author.getEffectiveName() + " (" + author.getAsMention() + ")", true)
                        .addField("Reported Member", traitor.getName() + " (" + traitor.getAsMention() + ")", true)
                        .addField("Reason", reason, false)
                        .setFooter( dateTimeFormatter.format(day.getTime()) )
                        .build();

                event.getGuild().getTextChannelById( 620316091477917728L ).sendMessage( embedBuilder ).queue();
            }



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