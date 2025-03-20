package dk.easv.ticketmanagementsystem.BLL;

import dk.easv.ticketmanagementsystem.BE.Ticket;
import dk.easv.ticketmanagementsystem.DAO.TicketDAL;

import java.sql.SQLException;

public class TicketBLL {
    private TicketDAL ticketDAL = new TicketDAL();

    public void addTicket(Ticket ticket) throws SQLException {
        ticketDAL.addTicket(ticket);
    }
}
