package paa.airline.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;


import paa.airline.business.AirlineService;
import paa.airline.business.AirlineServiceJPA;
import paa.airline.model.Flight;

public class Prueba3JFrame extends JFrame{
	//String[] c = {"Vuelo1", "Vuelo2", "Vuelo3"};
	JList<String> lista;
	
	public Prueba3JFrame(AirlineService airline){
		super("Prueba3Jframe");
		List<String> vuelos = new ArrayList<String>();
		for (Flight f : airline.listFlights()) vuelos.add(f.toString());
		lista = new JList(vuelos.toArray());
		//lista.setListData(c);
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lista.setVisibleRowCount(3);
		JScrollPane scroller = new JScrollPane(lista);
		add(scroller);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		
	}

}
