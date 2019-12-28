package me.itzdabbzz.wolfmc.data;


import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import me.itzdabbzz.wolfmc.util.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
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
    }

}
