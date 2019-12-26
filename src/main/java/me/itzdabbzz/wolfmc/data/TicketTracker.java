package me.itzdabbzz.wolfmc.data;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TicketTracker {

    private static Set<Ticket> tickets = new HashSet<>();

    public static Optional<Ticket> getTicketFromID(String ID) {
        for(Ticket ticket : tickets) {
            if(ticket.getID().equals(ID)) {
                return Optional.of(ticket);
            }
        }
        return Optional.empty();
    }

    public static void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }
}
