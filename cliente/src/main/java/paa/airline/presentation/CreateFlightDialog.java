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
import paa.airline.model.AircraftType;
import paa.airline.model.Airport;
import paa.airline.model.Flight;

public class CreateFlightDialog extends JDialog{
	private AirlineService airline;
	private JComboBox<Airport> origin;
	private JComboBox<Airport> destination;
	private JComboBox<AircraftType> aircraftType;
	private Flight vuelo;
	private boolean ok = false;
	
	public CreateFlightDialog(JFrame owner, AirlineService airline) {
		super(owner, "Create Flight", true);
		this.airline = airline;
		List<Airport> aeropuertos = new ArrayList<Airport>();
		for (Airport f : this.airline.listAirports()) aeropuertos.add(f);
		List<AircraftType> aviones = new ArrayList<AircraftType>();
		for (AircraftType v : this.airline.listAircraftTypes()) aviones.add(v);
		
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(3,3));
		formPanel.add(new JLabel("Origin:"));
		this.origin = new JComboBox(aeropuertos.toArray());
		formPanel.add(this.origin);
		JButton newAirport1 = new JButton("New airport");
		newAirport1.addActionListener(new ButtonListener());
		formPanel.add(newAirport1);
		formPanel.add(new JLabel("Destination:"));
		this.destination = new JComboBox(aeropuertos.toArray());
		formPanel.add(this.destination);
		JButton newAirport2 = new JButton("New airport");
		newAirport2.addActionListener(new ButtonListener());
		formPanel.add(newAirport2);
		formPanel.add(new JLabel("Aircraft type:"));
		this.aircraftType = new JComboBox(aviones.toArray());
		formPanel.add(this.aircraftType);
		JButton newAircraft = new JButton("New aircraft");
		newAircraft.addActionListener(new ButtonListener());
		formPanel.add(newAircraft);
		
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
	public boolean isCompleted() {
        return this.ok;
    }
	public Flight getFlight() {
        return this.vuelo;
    }
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			String opt = b.getText();
			if(opt.equals("New airport")){
				CreateAirportDialog airport = new CreateAirportDialog(null, CreateFlightDialog.this.airline);
				if (airport.isCompleted()) {
					CreateFlightDialog.this.origin.addItem(airport.getAirport());
					CreateFlightDialog.this.destination.addItem(airport.getAirport());
	            }
			}else if(opt.equals("New aircraft")){
				CreateAircraftDialog aircraft = new CreateAircraftDialog(null, CreateFlightDialog.this.airline);
				CreateFlightDialog.this.aircraftType.addItem(aircraft.getAircraftType());
			}
		}
	}
	class okListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				Airport airport = (Airport)CreateFlightDialog.this.origin.getSelectedItem();
				String origen = airport.getIataCode();
				airport = (Airport)CreateFlightDialog.this.destination.getSelectedItem();
				String destino = airport.getIataCode();
				AircraftType aircraft = (AircraftType)CreateFlightDialog.this.aircraftType.getSelectedItem();
				CreateFlightDialog.this.vuelo = CreateFlightDialog.this.airline.createFlight(origen, destino, aircraft.getId());
				CreateFlightDialog.this.ok = true;
				setVisible(false);
			}catch(AirlineServiceException ae) {
				JOptionPane.showMessageDialog(CreateFlightDialog.this, ae.getMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class cancelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CreateFlightDialog.this.dispose();
		}
	}
}
