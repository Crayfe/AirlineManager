package paa.airline.presentation;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import paa.airline.business.AirlineService;
import paa.airline.business.AirlineServiceJPA;
import paa.airline.model.Flight;

public class Prueba4JFrame extends JFrame {
	JComboBox lista;
	public Prueba4JFrame(AirlineService airline) {
		super("Prueba4Jframe");	
		List<String> vuelos = new ArrayList<String>();
		for (Flight f : airline.listFlights()) vuelos.add(f.toString());
		lista = new JComboBox(vuelos.toArray());
		add(lista);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	
	
	


}
