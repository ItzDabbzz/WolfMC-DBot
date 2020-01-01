package me.itzdabbzz.wolfmc;

import me.itzdabbzz.wolfmc.data.Ticket;
import me.itzdabbzz.wolfmc.data.TicketBlacklist;
import me.itzdabbzz.wolfmc.data.TicketTracker;
import me.itzdabbzz.wolfmc.util.ChannelTracker;
import me.itzdabbzz.wolfmc.util.Constants;
import me.itzdabbzz.wolfmc.util.Utils;
import me.vem.jdab.utils.emoji.Emoji;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListeners extends ListenerAdapter {

    public EXPSystem xp = EXPSystem.getInstance();
    private WolfBot bot;

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getChannel().getIdLong() == 620316094317592581L && e.getChannel().getIdLong() == 658909111932551199L){ // Open-A-Ticket Channel
            return;
        }

        //XP System
        if(xp.canMemberGetXP(e.getMember()) && e.getAuthor().isBot()){
            xp.randXP(e.getMember());
            xp.setPlayerTime(e.getMember(), 60);
        }

        //Ticket System
        if(!e.isWebhookMessage() && !e.isFromType(ChannelType.GROUP) && !e.isFromType(ChannelType.PRIVATE)) {
                ChannelTracker.getSupportChannel().ifPresent(channel -> {
                    if(e.getMessage().getChannel().equals(channel) && !e.getAuthor().isBot() && !TicketBlacklist.doesPlayerExist(e.getMember())){
                            e.getMessage().delete();
                            TicketTracker.addTicket(new Ticket(e, channel.getParent()));
                    }
                });
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if(e.getReactionEmote().getName().equals(Constants.CHECK) && !e.getUser().isBot()) {
            TicketTracker.getTicketFromID(e.getChannel().getId()).ifPresent(Ticket::closeIfValid);
        }


        //Support U+1F4AC
        /*if(e.getReactionEmote().getName().equals(new Emoji("U+1F4AC")) && !e.getUser().isBot()) {
            TicketTracker.getTicketFromID(e.getChannel().getId()).ifPresent(Ticket::closeIfValid);
        }

        //Sales U+1F4B3
        if(e.getReactionEmote().getName().equals(new Emoji("U+1F4B3")) && !e.getUser().isBot()) {
            TicketTracker.getTicketFromID(e.getChannel().getId()).ifPresent(Ticket::closeIfValid);
        }

        //Security 	U+1F512
        if(e.getReactionEmote().getName().equals(new Emoji("U+1F512")) && !e.getUser().isBot()) {
            TicketTracker.getTicketFromID(e.getChannel().getId()).ifPresent(Ticket::closeIfValid);
        }*/
    }
}

