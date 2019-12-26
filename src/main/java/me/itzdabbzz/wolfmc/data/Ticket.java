package me.itzdabbzz.wolfmc.data;


public class Ticket {

    public Ticket(String ticketID, String ticketName, String ticketReason, String ticketTime, String ticketDate){
        this.ticketID = ticketID;
        this.ticketName =  ticketName;
        this.ticketReason = ticketReason;
        this.ticketTime = ticketTime;
        this.ticketDate = ticketDate;
    }

    private String ticketID;
    private String ticketName;
    private String ticketReason;
    private String ticketTime;
    private String ticketDate;

    public String getTicketDate() {
        return ticketDate;
    }

    public String getTicketID() {
        return ticketID;
    }

    public String getTicketName() {
        return ticketName;
    }

    public String getTicketReason() {
        return ticketReason;
    }

    public String getTicketTime() {
        return ticketTime;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public void setTicketReason(String ticketReason) {
        this.ticketReason = ticketReason;
    }

    public void setTicketTime(String ticketTime) {
        this.ticketTime = ticketTime;
    }

}
