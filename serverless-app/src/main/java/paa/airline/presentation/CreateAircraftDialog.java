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
import javax.swing.JSpinner;
import javax.swing.JTextField;

import paa.airline.business.AirlineService;
import paa.airline.business.AirlineServiceException;
import paa.airline.model.AircraftType;
import paa.airline.model.Airport;
import paa.airline.presentation.CreateAirportDialog.cancelListener;
import paa.airline.presentation.CreateAirportDialog.okListener;

public class CreateAircraftDialog extends JDialog {
	private AirlineService airline;
	private JTextField manufacturerT;
	private JTextField modelT;
	private JSpinner seatRows;
	private JSpinner seatColumns;
	private boolean ok = false;
	private AircraftType aircraft;
	
	public CreateAircraftDialog(JFrame owner, AirlineService airline) {
		super(owner, "Create Airport", true);
		this.airline = airline;
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(4,2));
		formPanel.add(new JLabel("Manufacturer:"));
		this.manufacturerT = new JTextField();
		formPanel.add(this.manufacturerT);
		formPanel.add(new JLabel("Model:"));
		this.modelT = new JTextField();
		formPanel.add(this.modelT);
		formPanel.add(new JLabel("Seat rows:"));
		this.seatRows = new JSpinner();
		formPanel.add(this.seatRows);
		formPanel.add(new JLabel("Seat columns:"));
		this.seatColumns = new JSpinner();
		formPanel.add(this.seatColumns);
		
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
	public AircraftType getAircraftType() {
        return this.aircraft;
    }
	class okListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				String manufacturer = CreateAircraftDialog.this.manufacturerT.getText();
				String model = CreateAircraftDialog.this.modelT.getText();
				int seatRows = (Integer) CreateAircraftDialog.this.seatRows.getValue();
				int seatColumns = (Integer) CreateAircraftDialog.this.seatColumns.getValue();
				CreateAircraftDialog.this.aircraft = CreateAircraftDialog.this.airline.createAircraft(manufacturer, model, seatRows, seatColumns);
				CreateAircraftDialog.this.ok = true;
				setVisible(false);
			}catch(AirlineServiceException ae) {
				JOptionPane.showMessageDialog(CreateAircraftDialog.this, ae.getMessage(), "Aviso", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	class cancelListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CreateAircraftDialog.this.dispose();

			
		}
	}
}
