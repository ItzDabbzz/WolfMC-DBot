package me.itzdabbzz.wolfmc.data;

import me.itzdabbzz.wolfmc.WolfBot;
import me.itzdabbzz.wolfmc.util.Constants;
import me.itzdabbzz.wolfmc.util.Utils;
import me.vem.jdab.sqlite.SqliteDatabase;
import me.vem.jdab.sqlite.SqliteQuery;
import me.vem.jdab.utils.ExtFileManager;
import me.vem.jdab.utils.Logger;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ticket {

    public Ticket(MessageReceivedEvent e, Category category){
        String content = e.getMessage().getContentRaw();
        this.author = e.getMember();
        this.supportChannel = e.getChannel();
        this.category = category;
        this.guild = e.getGuild();
        this.message = e.getMessage();
        this.ticketID = ticketID;
        this.ticketUser =  e.getMember().getId();
        this.ticketReason = e.getMessage().getContentStripped();
        this.department = department;
        createTicket();
    }

    private String ticketID;
    private String ticketUser;
    private String ticketReason;
    private String department;
    private final Message message;
    private final Member author;
    private final MessageChannel supportChannel;
    private final Category category;
    private final Guild guild;
    private TextChannel ticketChannel;
    private static final String INFO = "\n\n*To close your ticket , react with  " + Constants.CHECK + "*" + "\n\n Please @ <@&620316101796036628> , <@&620316100965564417> , or  depending on who you need.\n <@&614473605958598669> will be here to help you shortly.";
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
    private static SqliteDatabase db;

    public String getTicketID() {
        return ticketID;
    }

    public String getTicketUser() {
        return ticketUser;
    }

    public String getTicketReason() {
        return ticketReason;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public void setTicketUser(String ticketUser) {
        this.ticketUser = ticketUser;
    }

    public void setTicketReason(String ticketReason) {
        this.ticketReason = ticketReason;
    }

    //TODO: How to setup questions after creation of a ticket to setup ticket level and department.

    public void closeIfValid() {


        /**
         * Send the Ticket Transcript
         */
        EmbedBuilder transcriptMessage = new EmbedBuilder();

        transcriptMessage.setDescription("***Ticket Transcript***")
                .setColor(Constants.embedTeal)
                .setDescription("Ticket Data")
                //.addField("Ticket ID", getTicketID() + "", true)
                //.addField("Ticket Author", getTicketUser() + "", true)
                //.addField("Ticket Reason", getTicketReason() + "", true)
                .setAuthor("WolfMC Ticket System", null, "https://mpng.pngfly.com/20180423/htq/kisspng-computer-icons-ticket-cinema-ticket-vector-5addf7381775f4.6435650615244961840961.jpg");
        File f = export(ticketChannel, "ticketsLogs/"+ticketChannel.getName()+"_transcript/");

        if(Constants.ticketTranscriptInChannel)
        {
            guild.getTextChannelById(620316098390392885L).sendFile(f).queue();
            Respond.async(guild.getTextChannelById(620316098390392885L), transcriptMessage.build());
        }

        Utils.sendEmbededPM(author.getUser(), transcriptMessage);
        Utils.sendFilePM(author.getUser(), transcriptMessage, f);

        //TODO: Set Ticket Finished In SQLite wb_tickets Table

        /**
         * Mark Ticket As Closed
         * Send Message And Delete Channel
         */
        Message closeMessage = new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                        .setDescription("***Ticket has been marked as closed!***")
                        .setColor(Constants.embedRed)
                        .setAuthor("WolfMC Ticket System", null, "https://mpng.pngfly.com/20180423/htq/kisspng-computer-icons-ticket-cinema-ticket-vector-5addf7381775f4.6435650615244961840961.jpg")
                        .build()).build();
        ticketChannel.sendMessage(closeMessage).queue();
        ticketChannel.delete().completeAfter(3, TimeUnit.SECONDS);

    }

    public String getID() {
        return ticketChannel.getId();
    }



    private void createTicket() {
        String name = "t-" + author.getEffectiveName() + " - " + (int) (Math.random() * 1000);
        guild.createTextChannel(name).setParent(category).queue(success -> {
            this.ticketChannel = (TextChannel) success;
            sendMessage();
            supportChannel.deleteMessageById(message.getId()).queue();
        });
    }


    /**
     *         String sqlQuery = "INSERT INTO wb_tickets ('id', 'ticketID', 'user', 'reason', 'level', 'finished', 'department') VALUES (value-1, value-2, value-3, value-4, value-5, value-6, value-7)";
     *         db = SqliteDatabase.create();
     *         try(SqliteQuery command = db.getQuery(sqlQuery)){
     *             command.addParameter("value-1", e.getMember().getId());
     *             command.addParameter("value-2", getID());
     *             command.addParameter("value-3", e.getMember().getEffectiveName());
     *             command.addParameter("value-4", e.getMessage());
     *             command.addParameter("value-5", 0);
     *             command.addParameter("value-6", 0);
     *             command.addParameter("value-7", department);
     *             command.execNonQuery();
     *         } catch(IOException | SQLException er) {
     *             er.printStackTrace();
     *             Logger.err("Cannot Insert Ticket " + getTicketID()) ;
     *         }
     */


    private void sendMessage() {
        ticketChannel.putPermissionOverride(author).setAllow(Constants.TICKET).queue();
        ticketChannel.putPermissionOverride(guild.getRolesByName("Staff", true).get(0)).setAllow(Constants.TICKET).queue();
        Message initMessage = new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                .setDescription("Reason: " + message.getContentDisplay() + INFO)
                .setColor(Constants.embedCyan)
                .setAuthor("WolfMC Ticket System", null, "https://mpng.pngfly.com/20180423/htq/kisspng-computer-icons-ticket-cinema-ticket-vector-5addf7381775f4.6435650615244961840961.jpg")
                .build()).build();
        ticketChannel.sendMessage(initMessage).queue(message -> message.addReaction(Constants.CHECK).queue());
        //ticketChannel.sendMessage(initMessage).queue(message -> message.addReaction("U+1F4AC").queue());
        //ticketChannel.sendMessage(initMessage).queue(message -> message.addReaction("U+1F4B3").queue());
        //ticketChannel.sendMessage(initMessage).queue(message -> message.addReaction("U+1F512").queue());
    }

    private GregorianCalendar day;
    private File export(TextChannel channel, String dir) {
        day = null;
        String guildName = channel.getGuild().getName();
        String channelName = channel.getName();
        String date = dateTimeFormatter.format(Calendar.getInstance().getTime());

        File file = ExtFileManager.getFile(dir, channelName + "-" + date + ".txt");

        try (PrintWriter writer = new PrintWriter(file)){

            writer.printf("%s%n%s%n%s%n%s%n", guildName, channelName, date, author.getEffectiveName());


            channel.getIterableHistory().cache(false).forEach(m -> {
                OffsetDateTime creation = m.getTimeCreated();
                if(day == null || creation.getDayOfMonth() != day.get(GregorianCalendar.DAY_OF_MONTH) ||
                        creation.getMonthValue()-1 != day.get(GregorianCalendar.MONTH) ||
                        creation.getYear() != day.get(GregorianCalendar.YEAR)) {
                    day = new GregorianCalendar(creation.getYear(), creation.getMonthValue()-1, creation.getDayOfMonth());
                    writer.printf("%n%s%n", dateFormatter.format(day.getTime()));
                }

                writer.printf("[%02d:%02d] %s: %s%n", creation.getHour(), creation.getMinute(), m.getAuthor().getName(), m.getContentDisplay());

                for(Message.Attachment a : m.getAttachments())
                    writer.printf("[Embeded: %s]%n", a.getUrl());
            });

            writer.flush();
            writer.close();

            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
