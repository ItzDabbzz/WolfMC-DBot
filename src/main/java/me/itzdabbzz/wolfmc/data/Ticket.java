package me.itzdabbzz.wolfmc.data;

import me.itzdabbzz.wolfmc.util.Constants;
import me.vem.jdab.utils.ExtFileManager;
import me.vem.jdab.utils.Respond;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Ticket {

    public Ticket(MessageReceivedEvent e, Category category){
        this.author = e.getMember();
        this.supportChannel = e.getChannel();
        this.category = category;
        this.guild = e.getGuild();
        this.message = e.getMessage();
        this.ticketID = ticketID;
        this.ticketUser =  ticketUser;
        this.ticketReason = ticketReason;
        createTicket();
    }

    private String ticketID;
    private String ticketUser;
    private String ticketReason;
    private final Message message;
    private final Member author;
    private final MessageChannel supportChannel;
    private final Category category;
    private final Guild guild;
    private TextChannel ticketChannel;
    private static final String INFO = "\n\n*To close your ticket , react with  " + Constants.CHECK + "*";
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");


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



    public void closeIfValid() {


        EmbedBuilder transcriptMessage = new EmbedBuilder();

        transcriptMessage.setDescription("***Ticket Transcript***")
                .setColor(Constants.embedTeal)
                .setAuthor("WolfMC Ticket System", null, "https://mpng.pngfly.com/20180423/htq/kisspng-computer-icons-ticket-cinema-ticket-vector-5addf7381775f4.6435650615244961840961.jpg")
                .build();
        File f = export(ticketChannel, ticketChannel.getName()+"_transcript/");
        guild.getTextChannelById(620316098390392885L).sendFile(f).queue();
        Respond.async(guild.getTextChannelById(620316098390392885L), transcriptMessage);

        //Send file to channel 620316098390392885 - Long ID for ticket-logs

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



    private void sendMessage() {
        ticketChannel.putPermissionOverride(author).setAllow(Constants.TICKET).queue();
        ticketChannel.putPermissionOverride(guild.getRolesByName("Staff", true).get(0)).setAllow(Constants.TICKET).queue();
        Message initMessage = new MessageBuilder()
                .setEmbed(new EmbedBuilder()
                .setDescription(message.getContentDisplay() + INFO)
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

            writer.printf("%s%n%s%n%s%n", guildName, channelName, date);


            channel.getIterableHistory().cache(false).forEach(m -> {
                OffsetDateTime creation = m.getTimeCreated();
                if(day == null || creation.getDayOfMonth() != day.get(GregorianCalendar.DAY_OF_MONTH) ||
                        creation.getMonthValue()-1 != day.get(GregorianCalendar.MONTH) ||
                        creation.getYear() != day.get(GregorianCalendar.YEAR)) {
                    day = new GregorianCalendar(creation.getYear(), creation.getMonthValue()-1, creation.getDayOfMonth());
                    writer.printf("%n%s%n", dateFormatter.format(day.getTime()));
                }

                writer.printf("[%02d:%02d] %s: %s%n" + author.getNickname(), creation.getHour(), creation.getMinute(), m.getAuthor().getName(), m.getContentDisplay());

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
