package paa.airline.presentation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import paa.airline.business.AirlineService;
import paa.airline.business.AirlineServiceException;
import paa.airline.model.Flight;
import paa.airline.model.Ticket;


public class CancelTicketDialog extends JDialog{
	private AirlineService airline;
	private JComboBox<Flight> flights;
	private JComboBox<Ticket> tickets;
	private boolean ok = false;
	private Ticket ticket;
	private Flight flightTicket;
	
	public CancelTicketDialog(JFrame owner, AirlineService airline, int indexF, int indexT) {
		super(owner, "Cancel Ticket", true);
		this.airline = airline;
		this.flights = new JComboBox();
		this.tickets = new JComboBox();
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(2,2));
		formPanel.add(new JLabel("Flight:"));
		for (Flight f : this.airline.listFlights()) this.flights.addItem(f);
		this.flights.setSelectedIndex(indexF);
		this.flights.addActionListener(new FlightChange());
		formPanel.add(this.flights);
		formPanel.add(new JLabel("Ticket:"));
		this.tickets = new JComboBox();
		for (Ticket t : ((Flight)this.flights.getSelectedItem()).getTickets()) this.tickets.addItem(t);
		this.tickets.setSelectedIndex(indexT);
		this.tickets.addActionListener(new TicketChange());
		formPanel.add(this.tickets);

		JPanel buttonPanel = new JPanel();
		JButton okBtn = new JButton("Ok");
		okBtn.addActionListener(new okListener());
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new cancelListener());
		buttonPanel.add(okBtn);
		buttonPanel.add(cancelBtn);
		
		this.setLayout(new BorderLayout());
		this.add(formPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		this.pack();
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
	}
	public Ticket getTicket() {
        return this.ticket;
    }
	public Flight getFlight() {
        return this.flightTicket;
    }
	public boolean isCompleted() {
        return this.ok;
    }
	class FlightChange implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JComboBox items = (JComboBox)e.getSource();
			CancelTicketDialog.this.tickets.removeAllItems();
			for (Ticket t : ((Flight)items.getSelectedItem()).getTickets()) CancelTicketDialog.this.tickets.addItem(t);
		}
	}
	class TicketChange implements ActionListener{
		public void actionPerformed(ActionEvent e) {

		}
	}
	class okListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				CancelTicketDialog.this.ticket = ((Ticket)CancelTicketDialog.this.tickets.getSelectedItem());
				CancelTicketDialog.this.flightTicket = ((Flight)CancelTicketDialog.this.flights.getSelectedItem());
				CancelTicketDialog.this.airline.cancelTicket(((Ticket)CancelTicketDialog.this.tickets.getSelectedItem()).getTicketNumber(), ((Ticket)CancelTicketDialog.this.tickets.getSelectedItem()).getFlightDate());
				CancelTicketDialog.this.ok = true;
				setVisible(false);
			}catch(AirlineServiceException ae) {
				JOptionPane.showMessageDialog(CancelTicketDialog.this, ae.getMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	class cancelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CancelTicketDialog.this.dispose();

			
		}
	}
}
