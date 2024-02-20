package paa.airline.presentation;
import java.awt.Color;
import java.awt.HeadlessException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import paa.airline.util.FlightMap;

public class Prueba2JFrame extends JFrame {
	private JMenuBar barraMenu = new JMenuBar();
	private JMenu fichero = new JMenu("Fichero");
	private JMenuItem abrir = new JMenuItem("Abrir");
	
	public Prueba2JFrame(){
		super("Prueba2Jframe");
		JPanel pane = new JPanel();
		this.add(pane);
		barraMenu.add(fichero);
		fichero.add(abrir);
		this.setJMenuBar(barraMenu);
		FlightMap fMap = new FlightMap(500, 500, null);
		pane.add(fMap);
		pane.setBorder(BorderFactory.createTitledBorder("Flight Map"));
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		JOptionPane.showMessageDialog(null, "Autor: Cristian Ayuso Ferr�n");

	}

}
