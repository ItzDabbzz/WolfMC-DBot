package me.itzdabbzz.wolfmc;

import me.itzdabbzz.wolfmc.data.Ticket;
import me.itzdabbzz.wolfmc.data.TicketTracker;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Optional;

public class MessageListeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(!e.isWebhookMessage() && !e.isFromType(ChannelType.GROUP) && !e.isFromType(ChannelType.PRIVATE)) {
                ChannelTracker.getSupportChannel().ifPresent(channel -> {
                    if(e.getMessage().getChannel().equals(channel) || !e.getAuthor().isBot()) {
                        TicketTracker.addTicket(new Ticket(e));
                    }
                });
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if(e.getReactionEmote().getName().equals(Constants.CHECK) && !e.getUser().isBot()) {
            TicketTracker.getTicketFromID(e.getChannel().getId()).ifPresent(Ticket::closeIfValid);
        }
    }
}

