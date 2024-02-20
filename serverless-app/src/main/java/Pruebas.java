import java.time.LocalDate;
import java.util.List;

import paa.airline.business.AirlineServiceException;
import paa.airline.business.AirlineServiceJPA;
import paa.airline.model.AircraftType;
import paa.airline.model.Airport;
import paa.airline.model.Flight;
import paa.airline.model.Ticket;
import paa.airline.presentation.AirlineGUI;
import paa.airline.presentation.Prueba1JFrame;
import paa.airline.presentation.Prueba2JFrame;
import paa.airline.presentation.Prueba3JFrame;
import paa.airline.presentation.Prueba4JFrame;

public class Pruebas {

	public static void main(String[] args) throws AirlineServiceException {
		
		//AirlineServiceJPA airlineService = new AirlineServiceJPA("AirlineServiceDB");
		/*
		Airport airport = airlineService.createAirport("MAD","Madrid", "Barajas", 0, 0);
		Airport airport2 = airlineService.createAirport("BAR","Barcelona", "El Prat", 10, 10);
		Airport airport1 = airlineService.createAirport("VLC","Valencia", "Cuart de Poblet", 13, 13);
		Airport airport4 = airlineService.createAirport("GAL","Galicia", "Santiago de compostela", 80, 50);
		Airport airport5 = airlineService.createAirport("HEX","Hexalia", "a", 80, 50);
		AircraftType aircra = airlineService.createAircraft("Airbus", "A319", 16, 8);
		AircraftType aircra1 = airlineService.createAircraft("Boeing", "737", 13, 10);
		AircraftType aircra3 = airlineService.createAircraft("Crayfty Crayfe", "Hexajet", 3, 2);
		Flight flight = airlineService.createFlight("MAD", "VLC", 1L);
		Flight flight1 = airlineService.createFlight("VLC", "MAD", 1L);
		Flight flight2 = airlineService.createFlight("MAD", "BAR", 2L);
		Flight flight3 = airlineService.createFlight("MAD", "HEX", 3L);
		Ticket ticket = airlineService.purchaseTicket("Cristian", "Ayuso Ferrón", 4L, LocalDate.now(), LocalDate.now().plusDays(2));
		Ticket ticket1 = airlineService.purchaseTicket("David", "Navarro", 4L, LocalDate.now(), LocalDate.now());
		Ticket ticket2 = airlineService.purchaseTicket("Fernando", "Sotomayor", 5L, LocalDate.now(), LocalDate.now().plusDays(1));
		Ticket ticket3 = airlineService.purchaseTicket("Cristian", "Ayuso Ferrón", 7L, LocalDate.now(), LocalDate.now().plusDays(2));
		Ticket ticket4 = airlineService.purchaseTicket("David", "Navarro", 7L, LocalDate.now(), LocalDate.now());
		Ticket ticket5 = airlineService.purchaseTicket("Fernando", "Sotomayor", 7L, LocalDate.now(), LocalDate.now());
		Ticket ticket6 = airlineService.purchaseTicket("Fernando", "García", 7L, LocalDate.now(), LocalDate.now());
		Ticket ticket7 = airlineService.purchaseTicket("Raquel", "Villares", 7L, LocalDate.now(), LocalDate.now()); 
		
		
		*/
		//System.out.println(airport.toString());
		//List<Airport> aux = airlineService.listAirports();
		//List<AircraftType> aux = airlineService.listAircraftTypes();
		//List<Flight> aux = airlineService.listFlights();
		//List<Ticket> aux = flight.getTickets();
		//System.out.println(aux.size());
		//for (Ticket o : aux)System.out.println(o.toString());
		//System.out.println(airlineService.availableSeats(3L, LocalDate.now()  ));
		//System.out.println(airlineService.availableSeats(4L, LocalDate.now()  ));
		//System.out.println(airlineService.availableSeats(5L, LocalDate.now()  ));
		//Prueba3JFrame win3 = new Prueba3JFrame(airlineService);
		//Prueba4JFrame win4 = new Prueba4JFrame(airlineService);
		//AirlineGUI gui = new AirlineGUI(airlineService);
	}

}
