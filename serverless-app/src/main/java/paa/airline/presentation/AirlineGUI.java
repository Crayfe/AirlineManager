package paa.airline.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import paa.airline.business.AirlineService;
import paa.airline.business.AirlineServiceJPA;
import paa.airline.model.Flight;
import paa.airline.model.Ticket;
import paa.airline.util.FlightMap;

public class AirlineGUI extends JFrame{
	private AirlineService airline;
	private LocalDate fechaActual;
	private JLabel simulatedDate;
	private JMenuBar barraMenu = new JMenuBar();
	private JMenu operations = new JMenu("Operations");
	private JMenuItem newAirport = new JMenuItem("New airport");
	private JMenuItem newAircraft = new JMenuItem("New aircraft");
	private JMenuItem newFlight = new JMenuItem("New flight");
	private JMenuItem buyTicket = new JMenuItem("Buy ticket");
	private JMenuItem cancelTicket = new JMenuItem("Cancel ticket");
	private JMenuItem quit = new JMenuItem("Quit");
	private JMenuItem about = new JMenuItem("About");
	private JMenu help = new JMenu("Help");
	private JComboBox<Flight> listaVuelos;
	private JComboBox<LocalDate> selectorFecha;
	private JList<Ticket> listaTickets;
	private DefaultListModel tickets;
	private FlightMap fMap;

	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				AirlineGUI app = new AirlineGUI(new AirlineServiceJPA("AirlineServiceDB"));

			}
		});

	}
	public AirlineGUI(AirlineService airline) {
		super("Airline Manager");
		this.airline = airline;
		this.fMap= new FlightMap(1600, 850, this.airline);
		//BARRA DE MENU
		barraMenu.add(operations);
		newAirport.addActionListener(new MenuClicked());
		operations.add(newAirport);
		newAircraft.addActionListener(new MenuClicked());
		operations.add(newAircraft);
		newFlight.addActionListener(new MenuClicked());
		operations.add(newFlight);
		buyTicket.addActionListener(new MenuClicked());
		operations.add(buyTicket);
		cancelTicket.addActionListener(new MenuClicked());
		operations.add(cancelTicket);
		quit.addActionListener(new MenuClicked());
		operations.add(quit);
		barraMenu.add(help);
		about.addActionListener(new MenuClicked());
		help.add(about);
		setJMenuBar(barraMenu);
		
		//MAPA
		JPanel flightMapPanel = new JPanel();
		flightMapPanel.setLayout(new BorderLayout());
		
		this.selectorFecha = new JComboBox<LocalDate>();
		this.selectorFecha.addActionListener(new DateSelectChanged());
		flightMapPanel.add(this.selectorFecha, BorderLayout.NORTH);
		flightMapPanel.add(this.fMap, BorderLayout.CENTER);
		flightMapPanel.setBorder(BorderFactory.createTitledBorder("Flight Map"));
		
		//BARRA DE BOTONES Y SIMULADOR DE FECHAS
		JPanel ButtonBarPanel = new JPanel();
		ButtonBarPanel.setLayout(new BorderLayout());
		
		//BOTONES
		JPanel ButtonSetPanel = new JPanel();
		ButtonSetPanel.setLayout(new FlowLayout());
		Icon icon1 = new ImageIcon("src/main/resources/newFlight.png");
		JButton newFlightButton = new JButton(icon1);
		newFlightButton.addActionListener(new ButtonClicked1());
		Icon icon2 = new ImageIcon("src/main/resources/buyTicket.png");
		JButton buyTicketButton = new JButton(icon2);
		buyTicketButton.addActionListener(new ButtonClicked2());
		Icon icon3 = new ImageIcon("src/main/resources/cancelTicket.png");
		JButton cancelTicketButton = new JButton(icon3);
		cancelTicketButton.addActionListener(new ButtonClicked3());
		ButtonSetPanel.add(newFlightButton);
		ButtonSetPanel.add(buyTicketButton);
		ButtonSetPanel.add(cancelTicketButton);
		
		//SIMULADOR FECHAS
		JPanel dateSimulatorPanel = new JPanel();
		dateSimulatorPanel.setLayout(new BorderLayout());
		JLabel simulatorTitle = new JLabel("Current date (simulated):");
		this.fechaActual = LocalDate.now();
		this.simulatedDate = new JLabel(fechaActual.toString());
		JButton dayBefore = new JButton("<");
		dayBefore.addActionListener(new ButtonDateChange());
		JButton dayAfter = new JButton(">");
		dayAfter.addActionListener(new ButtonDateChange());
		dateSimulatorPanel.add(simulatorTitle, BorderLayout.NORTH);
		dateSimulatorPanel.add(simulatedDate, BorderLayout.CENTER);
		dateSimulatorPanel.add(dayBefore, BorderLayout.WEST);
		dateSimulatorPanel.add(dayAfter, BorderLayout.EAST);
		
		ButtonBarPanel.add(ButtonSetPanel, BorderLayout.WEST);
		ButtonBarPanel.add(dateSimulatorPanel, BorderLayout.EAST);
		
		//LISTA DESPLEGABLE DE VUELOS
		JPanel ListFlightsPanel = new JPanel();
		List<Flight> vuelos = new ArrayList<Flight>(airline.listFlights());
		this.listaVuelos = new JComboBox(vuelos.toArray());
		this.listaVuelos.addActionListener(new FlightSelectChanged());
		this.listaVuelos.setPreferredSize(new Dimension(240, 30));
		ListFlightsPanel.add(this.listaVuelos);
		ListFlightsPanel.setBorder(BorderFactory.createTitledBorder("Scheduled flights"));
		
		//LISTA DE BILLETES RESERVADOS
		JPanel ListTicketsPanel = new JPanel();
		this.tickets = new DefaultListModel();
		this.listaTickets = new JList<Ticket>(this.tickets);
		this.listaTickets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listaTickets.addListSelectionListener(new SelectionOfTicket());
		JScrollPane ticketScroller = new JScrollPane(this.listaTickets);
		ticketScroller.setPreferredSize(new Dimension(240, 800));
		ListTicketsPanel.add(ticketScroller);
		ListTicketsPanel.setBorder(BorderFactory.createTitledBorder("Tickets"));
		
		//BARRA LATERAL IZQUIERDA
		JPanel leftBar = new JPanel();
		leftBar.setLayout(new BorderLayout());
		leftBar.add(ListFlightsPanel, BorderLayout.NORTH);
		leftBar.add(ListTicketsPanel, BorderLayout.CENTER);
		leftBar.setPreferredSize(new Dimension(260, 100));
		
		//CONTENEDOR DE TODA LA GUI
		JPanel content = new JPanel();	
		content.setLayout(new BorderLayout());
		content.add(leftBar, BorderLayout.WEST);
		content.add(ButtonBarPanel, BorderLayout.NORTH);
		content.add(flightMapPanel, BorderLayout.CENTER);
		add(content);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	public void addTicket(Flight vuelo, Ticket ticket) {
		List<Ticket> tickets = vuelo.getTickets();
		if(tickets == null || tickets.isEmpty()) {
			tickets = new ArrayList<Ticket>();
		}
		tickets.add(ticket);
		vuelo.setTickets(tickets);
		this.updateLists();
		
	}
	public void removeTicket(Flight vuelo, Ticket ticket) {
		List<Ticket> tickets = vuelo.getTickets();
		tickets.remove(ticket);
		vuelo.setTickets(tickets);
		this.updateLists();
		
	}
	public void updateFlights(Flight nuevoVuelo) {
		if(nuevoVuelo != null) {
			this.listaVuelos.addItem(nuevoVuelo);
		}else {
			this.updateLists();
		}
		
	}
	public void updateLists() {
		this.tickets.removeAllElements();
		this.selectorFecha.removeAllItems();
		if(this.listaVuelos != null && this.listaVuelos.getItemCount()>0) {
			Flight vueloSeleccionado = (Flight) this.listaVuelos.getSelectedItem();
			if(vueloSeleccionado != null){
				List<Ticket> ticketsList = vueloSeleccionado.getTickets();
				if(ticketsList != null && !ticketsList.isEmpty()) {
					this.tickets.addAll(ticketsList);
					List<LocalDate> fechasDuplicadas =  new ArrayList<LocalDate>();
					for(Ticket t : ticketsList) {
						fechasDuplicadas.add(t.getFlightDate());
					}
					Set<LocalDate> set = new LinkedHashSet<LocalDate>(fechasDuplicadas);
					List<LocalDate>fechas = new ArrayList<LocalDate>(set);
					for(LocalDate f : fechas) this.selectorFecha.addItem(f);
				}
			}
		}
	}
	//EVENTO SELECCIÓN DE LISTA DE TICKETS
	class SelectionOfTicket implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent e) {
			Ticket ticket = AirlineGUI.this.listaTickets.getSelectedValue();
			if(ticket != null) {
				JOptionPane.showMessageDialog(AirlineGUI.this, "First name: "+ticket.getPassengerFirstName()+
						"\nLast Name: "+ticket.getPassengerLastName() +
						"\nPrice paid: "+(float)ticket.getPricePaid()/100 +
						"€\nSeat row: "+ticket.getSeatColumn() + "   Seat column: "+ticket.getSeatColumn() +
						"\nFlight: "+ticket.getFlight().toString()+"   Date: "+ticket.getFlightDate()
			, "Ticket nº: "+ticket.getTicketNumber(), JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	//EVENTO CAMBIO SELECCIÓN VUELO
	class FlightSelectChanged implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			AirlineGUI.this.updateLists();
		}
	}
	//EVENTO CAMBIO SELECCIÓN FECHA
	class DateSelectChanged implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(AirlineGUI.this.selectorFecha.getItemCount()>0) {
				if(AirlineGUI.this.selectorFecha.getSelectedItem() != null) {
					AirlineGUI.this.fMap.updateMap((LocalDate)AirlineGUI.this.selectorFecha.getSelectedItem(), ((Flight) AirlineGUI.this.listaVuelos.getSelectedItem()).getFlightNumber());
				}
			}else {
				AirlineGUI.this.fMap.updateMap(LocalDate.now(), ((Flight) AirlineGUI.this.listaVuelos.getSelectedItem()).getFlightNumber());
			}
		}
	}
	//EVENTO CREAR NUEVO VUELO
	class ButtonClicked1 implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CreateFlightDialog flight = new CreateFlightDialog(AirlineGUI.this, AirlineGUI.this.airline);
			if (flight.isCompleted()) {
				AirlineGUI.this.updateFlights(flight.getFlight());
			}
		}
	}
	//EVENTO COMPRAR BILLETE
	class ButtonClicked2 implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			BuyTicketDialog ticket = new BuyTicketDialog(AirlineGUI.this, AirlineGUI.this.airline, AirlineGUI.this.fechaActual, AirlineGUI.this.listaVuelos.getSelectedIndex() );
			if (ticket.isCompleted()) {
				AirlineGUI.this.addTicket(ticket.getFlight(),ticket.getTicket());
            }
		}
	}
	//EVENTO CANCELAR BILLETE
	class ButtonClicked3 implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			CancelTicketDialog ticket = new CancelTicketDialog(AirlineGUI.this, AirlineGUI.this.airline, AirlineGUI.this.listaVuelos.getSelectedIndex(), AirlineGUI.this.listaTickets.getSelectedIndex());
			if (ticket.isCompleted()) {
				AirlineGUI.this.removeTicket(ticket.getFlight(),ticket.getTicket());
            }
		}
	}
	//EVENTO CAMBIAR FECHA
	class ButtonDateChange implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			String opt = b.getText();
			if(opt.equals("<")) {
				AirlineGUI.this.fechaActual = AirlineGUI.this.fechaActual.minusDays(1);
			}else if(opt.equals(">")){
				AirlineGUI.this.fechaActual = AirlineGUI.this.fechaActual.plusDays(1);
			}
			AirlineGUI.this.simulatedDate.setText(AirlineGUI.this.fechaActual.toString());

		}
	}
	//EVENTOS DE MENU
	class MenuClicked implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String opt =  ((JMenuItem)e.getSource()).getText();
			if(opt.equals("New airport")){
				CreateAirportDialog airport = new CreateAirportDialog(AirlineGUI.this, AirlineGUI.this.airline);
				if (airport.isCompleted()) {
	            	
	            }
			}else if(opt.equals("New aircraft")){
				CreateAircraftDialog aircraft = new CreateAircraftDialog(AirlineGUI.this, AirlineGUI.this.airline);
				if (aircraft.isCompleted()) {
	            	
	            }
			}else if(opt.equals("New flight")){
				CreateFlightDialog flight = new CreateFlightDialog(AirlineGUI.this, AirlineGUI.this.airline);
				if (flight.isCompleted()) {
					AirlineGUI.this.updateFlights(flight.getFlight());
	            }
			}else if(opt.equals("Buy ticket")){
				BuyTicketDialog ticket = new BuyTicketDialog(AirlineGUI.this, AirlineGUI.this.airline, AirlineGUI.this.fechaActual, AirlineGUI.this.listaVuelos.getSelectedIndex());
				if (ticket.isCompleted()) {
					AirlineGUI.this.addTicket(ticket.getFlight(),ticket.getTicket());
	            }
			}else if(opt.equals("Cancel ticket")){
				CancelTicketDialog ticket = new CancelTicketDialog(AirlineGUI.this, AirlineGUI.this.airline, AirlineGUI.this.listaVuelos.getSelectedIndex(), AirlineGUI.this.listaTickets.getSelectedIndex());
				if (ticket.isCompleted()) {
					AirlineGUI.this.updateFlights(null);
	            }
			}else if(opt.equals("Quit")){
				System.exit(0);
			}else if(opt.equals("About")){
				JOptionPane.showMessageDialog(AirlineGUI.this, "Programa: Airline Manager\nVersión: 3\nAutor: Cristian Ayuso Ferrón");
			}
			
		}
		
	}

}
