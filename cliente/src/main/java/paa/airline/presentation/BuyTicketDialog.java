package paa.airline.presentation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import paa.airline.business.AirlineService;
import paa.airline.business.AirlineServiceException;
import paa.airline.model.AircraftType;
import paa.airline.model.Airport;
import paa.airline.model.Flight;
import paa.airline.model.Ticket;
import paa.airline.presentation.CreateAircraftDialog.cancelListener;
import paa.airline.presentation.CreateAircraftDialog.okListener;

public class BuyTicketDialog extends JDialog{
	private AirlineService airline;
	private LocalDate fechaActual;
	private JTextField firstName;
	private JTextField lastName;
	private JComboBox flight;
	private JTextField date;
	private boolean ok = false;
	private Ticket ticket;
	private Flight flightTicket;
	
	public BuyTicketDialog(JFrame owner, AirlineService airline, LocalDate fechaActual, int index) {
		super(owner, "Buy Ticket", true);
		this.airline = airline;
		this.fechaActual = fechaActual;
		List<Flight> auxvuelos = new ArrayList<Flight>();
		for (Flight f : this.airline.listFlights()) auxvuelos.add(f);
		this.flight = new JComboBox(auxvuelos.toArray());
		this.flight.setSelectedIndex(index);
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(4,2));
		formPanel.add(new JLabel("Traveller first name:"));
		this.firstName = new JTextField();
		formPanel.add(this.firstName);
		formPanel.add(new JLabel("Traveller last name:"));
		this.lastName = new JTextField();
		formPanel.add(this.lastName);
		formPanel.add(new JLabel("Flight:"));
		
		formPanel.add(this.flight);
		formPanel.add(new JLabel("Flight date:"));
		this.date = new JTextField();
		date.setText(this.fechaActual.toString());
		formPanel.add(this.date);
		
		
		
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
	
	class okListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				BuyTicketDialog.this.flightTicket = (Flight)BuyTicketDialog.this.flight.getSelectedItem();
				BuyTicketDialog.this.ticket = BuyTicketDialog.this.airline.purchaseTicket(BuyTicketDialog.this.firstName.getText(), BuyTicketDialog.this.lastName.getText(), BuyTicketDialog.this.flightTicket.getFlightNumber(), BuyTicketDialog.this.fechaActual , LocalDate.parse(BuyTicketDialog.this.date.getText()) );
				BuyTicketDialog.this.ok = true;
				setVisible(false);
			}catch(AirlineServiceException ae) {
				JOptionPane.showMessageDialog(BuyTicketDialog.this, ae.getMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
			}catch(DateTimeParseException t) {
				JOptionPane.showMessageDialog(BuyTicketDialog.this, "La fecha de vuelo no está en el formato correcto.\n Por favor, cámbielo.", "Aviso", JOptionPane.ERROR_MESSAGE);
			}
			
		}
	}
	class cancelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			BuyTicketDialog.this.dispose();
		}
	}
}
