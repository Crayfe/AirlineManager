package paa.airline.presentation;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import paa.airline.business.AirlineService;
import paa.airline.business.AirlineServiceException;
import paa.airline.model.Airport;
import paa.airline.util.AirportQuery;

public class CreateAirportDialog extends JDialog {
	private AirlineService airline;
	private Airport airport;
	private JTextField iataCodeT;
	private JTextField nameT;
	private JTextField cityT;
	private JTextField longitudeT;
	private JTextField latitudeT;
	private boolean ok = false;
	
	public CreateAirportDialog(JFrame owner, AirlineService airline) {
		super(owner, "Create Airport", true);
		this.airline = airline;
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(5,2));
		formPanel.add(new JLabel("IATA code:"));
		this.iataCodeT = new JTextField();
		formPanel.add(this.iataCodeT);
		formPanel.add(new JLabel("Name:"));
		this.nameT = new JTextField();
		formPanel.add(this.nameT);
		formPanel.add(new JLabel("City:"));
		this.cityT = new JTextField();
		formPanel.add(this.cityT);
		formPanel.add(new JLabel("Longitude:"));
		this.longitudeT = new JTextField();
		formPanel.add(this.longitudeT);
		formPanel.add(new JLabel("Latitude:"));
		this.latitudeT = new JTextField();
		formPanel.add(this.latitudeT);
		
		JPanel buttonPanel = new JPanel();
		JButton okBtn = new JButton("Ok");
		okBtn.addActionListener(new okListener());
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new cancelListener());
		JButton autofillBtn = new JButton("Autofill");
		autofillBtn.addActionListener(new autofillListener());
		buttonPanel.add(okBtn);
		buttonPanel.add(cancelBtn);
		buttonPanel.add(autofillBtn);
		
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
	public Airport getAirport() {
        return this.airport;
    }
	class okListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				String iataCode = CreateAirportDialog.this.iataCodeT.getText();
				String name = CreateAirportDialog.this.nameT.getText();
				String city = CreateAirportDialog.this.cityT.getText();
				double longitude = Double.parseDouble(CreateAirportDialog.this.longitudeT.getText());
				double latitude = Double.parseDouble(CreateAirportDialog.this.latitudeT.getText());
				CreateAirportDialog.this.airport = CreateAirportDialog.this.airline.createAirport(iataCode, city, name, longitude, latitude);
				CreateAirportDialog.this.ok = true;
				setVisible(false);
			}catch(AirlineServiceException ae) {
				JOptionPane.showMessageDialog(CreateAirportDialog.this, ae.getMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
			}catch(NumberFormatException nm){
				JOptionPane.showMessageDialog(CreateAirportDialog.this, "Por favor, asegúrese de introducir valores numéricos para los campos Longitude y Latitude.", "Aviso", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class cancelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CreateAirportDialog.this.dispose();
			
		}
	}
	class autofillListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String iataCode = CreateAirportDialog.this.iataCodeT.getText();
			if(iataCode.equals(iataCode.toUpperCase()) && iataCode.length()==3) {
				try {
					CreateAirportDialog.this.nameT.setText(AirportQuery.getName(iataCode));
					CreateAirportDialog.this.cityT.setText(AirportQuery.getLocation(iataCode));
					CreateAirportDialog.this.longitudeT.setText(AirportQuery.getLongitude(iataCode).toString());
					CreateAirportDialog.this.latitudeT.setText(AirportQuery.getLatitude(iataCode).toString());
				}catch(Exception e1){
					
				}

				
			}
			
			
		}
	}
}
