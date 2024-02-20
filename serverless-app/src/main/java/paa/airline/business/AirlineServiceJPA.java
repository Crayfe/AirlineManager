package paa.airline.business;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import paa.airline.model.AircraftType;
import paa.airline.model.Airport;
import paa.airline.model.Flight;
import paa.airline.model.Ticket;
import paa.airline.persistence.AircraftTypeJPADAO;
import paa.airline.persistence.AirportJPADAO;
import paa.airline.persistence.FlightJPADAO;
import paa.airline.persistence.TicketJPADAO;
import paa.airline.util.AirportQuery;

public class AirlineServiceJPA implements AirlineService {
	private String persistenceUnit;
	private EntityManagerFactory emf;
	private EntityManager em;
	static final double tMax = 500;
	static final double tRush = 50;
	static final double oMax = 90;
	
	public AirlineServiceJPA(String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
	}

	private void ensureActiveManager() {
		if (this.emf == null || !this.emf.isOpen()) {
			this.emf = Persistence.createEntityManagerFactory(persistenceUnit);
		}
		if (this.em == null || !this.em.isOpen()) {
			this.em = this.emf.createEntityManager();
		}
	}
	
	public void close() {
		this.em.close();
		this.emf.close();
	}
	
	@Override
	public Airport createAirport(String iataCode, String cityName, String airportName, double longitude, double latitude) throws AirlineServiceException {
		
		if (iataCode == null || iataCode.isBlank()) {
			throw new AirlineServiceException("Error: el iataCode del aeropuerto no puede ser nulo ni vacío.");
		}
		if (iataCode.length() != 3 || !iataCode.equals(iataCode.toUpperCase())) {
			throw new AirlineServiceException("Error: el iataCode del aeropuerto debe de ser un código de tres letras mayúsculas.");
		}
		if (cityName == null || cityName.isBlank()) {
			throw new AirlineServiceException("Error: el cityName del aeropuerto no puede ser nulo ni vacío.");
		}
		if (airportName == null || airportName.isBlank()) {
			throw new AirlineServiceException("Error: el cityName del aeropuerto no puede ser nulo ni vacío.");
		}
		if(longitude < -180 || longitude > 180 || latitude < -90 || latitude > 90) {
			throw new AirlineServiceException("Error: los paŕametros de longitud y/o latidud están fuera de su rango.");
		}
		ensureActiveManager();
		Airport airport= new Airport(iataCode, cityName, airportName, longitude, latitude);
		AirportJPADAO dao = new AirportJPADAO(em);
		if(airport.equals(dao.find(airport.getIataCode()))) {
			throw new AirlineServiceException("Error: el aeropuerto ya existe.");
		}
		EntityTransaction et = this.em.getTransaction();
		try {
			et.begin();
			airport = dao.create(airport);
			et.commit();
			return airport;
		} catch (Exception e) {
			if (et.isActive()) {
				et.rollback();
			}
			throw new AirlineServiceException("Error: ha ocurrido algo al acceder a la base de datos del aeropuerto.");
		} 
	}

	@Override
	public List<Airport> listAirports() {
		ensureActiveManager();
		AirportJPADAO dao = new AirportJPADAO(em);
		return dao.findAll();
	}

	@Override
	public AircraftType createAircraft(String manufacturer, String model, int seatRows, int seatColumns) throws AirlineServiceException {
		
		if (manufacturer == null || manufacturer.isBlank()) {
			throw new AirlineServiceException("Error: el fabricante del avión no puede ser nulo ni vacío.");
		}
		if (model == null || model.isBlank()) {
			throw new AirlineServiceException("Error: el modelo del avión no puede ser nulo ni vacío.");
		}
		if (seatRows <= 0 || seatColumns <= 0) {
			throw new AirlineServiceException("Error: el número de filas o de columnas de asientos del avión no pueden ser menor o igual a cero.");
		}
		if(seatRows <= seatColumns) {
			throw new AirlineServiceException("Error: el número de filas debe de ser mayor al número de columnas.");
		}
		ensureActiveManager();
		AircraftType aType = new AircraftType(manufacturer, model, seatRows, seatColumns);
		AircraftTypeJPADAO dao = new AircraftTypeJPADAO(em);
		EntityTransaction et = this.em.getTransaction();
		try {
			et.begin();
			aType = dao.create(aType);
			et.commit();
			return aType;
		} catch (Exception e) {
			if (et.isActive()) {
				et.rollback();
			}
			throw new AirlineServiceException("Error: ha ocurrido algo al acceder a la base de datos del aeropuerto.");
		} 
	}

	@Override
	public List<AircraftType> listAircraftTypes() {
		ensureActiveManager();
		AircraftTypeJPADAO dao = new AircraftTypeJPADAO(em);
		return dao.findAll();
	}

	@Override
	public Flight createFlight(String originAirportCode, String destinationAirportCode, Long aircraftTypeCode) throws AirlineServiceException {
		if (originAirportCode == null || originAirportCode.isBlank()) {
			throw new AirlineServiceException("Error: el originAirportCode del vuelo no puede ser nulo ni vacío.");
		}
		if (destinationAirportCode == null || destinationAirportCode.isBlank()) {
			throw new AirlineServiceException("Error: el destinationAirportCode del vuelo no puede ser nulo ni vacío.");
		}
		if(originAirportCode.equals(destinationAirportCode)) {
			throw new AirlineServiceException("Error: el origen y destino no pueden ser el mismo.");
		}
		ensureActiveManager();
		AirportJPADAO daoA = new AirportJPADAO(em);
		Airport airport1 = daoA.find(originAirportCode);
		Airport airport2 = daoA.find(destinationAirportCode);
		if (airport1 == null || airport2 == null) {
			throw new AirlineServiceException("Error: Se han introducido uno o varios códigos de aeropuerto no válidos.");
		}
		AircraftTypeJPADAO daoAT = new AircraftTypeJPADAO(em);
		AircraftType aType = daoAT.find(aircraftTypeCode); 
		if(aType == null) {
			throw new AirlineServiceException("Error: el tipo de avión intoducido no aparece en la base de datos.");
		}
		Flight flight= new Flight(airport1, airport2, aType);
		FlightJPADAO daoF = new FlightJPADAO(em);
		EntityTransaction et = this.em.getTransaction();
		try {
			et.begin();
			flight = daoF.create(flight);
			et.commit();
			return flight;
		} catch (Exception e) {
			if (et.isActive()) {
				et.rollback();
			}
			throw new AirlineServiceException("Error: ha ocurrido algo al acceder a la base de datos del aeropuerto.");
		} 
	}

	@Override
	public Flight findFlight(Long flightNumber) {
		ensureActiveManager();
		FlightJPADAO daoF = new FlightJPADAO(em);
		return daoF.find(flightNumber);
	}

	@Override
	public List<Flight> listFlights() {
		ensureActiveManager();
		FlightJPADAO daoF = new FlightJPADAO(em);
		return daoF.findAll();
	}

	@Override
	public Ticket purchaseTicket(String firstName, String lastName, Long flightNumber, LocalDate purchaseDate, LocalDate flightDate) throws AirlineServiceException {
		if (firstName == null || firstName.isBlank()) {
			throw new AirlineServiceException("Error: el nombre del pasajero no puede ser nulo ni vacío.");
		}
		if (lastName == null || lastName.isBlank()) {
			throw new AirlineServiceException("Error: los apellidos del pasajero no puede ser nulo ni vacío.");
		}
		if(purchaseDate == null || flightDate == null) {
			throw new AirlineServiceException("Error: las variables de fechas no puende ser null.");
		}
		LocalDate today = LocalDate.now();
		if(purchaseDate.isBefore(today) || flightDate.isBefore(today)) {
			throw new AirlineServiceException("Error: No eres Marty McFly.");
		}
		Flight vuelo = findFlight(flightNumber);
		if(vuelo == null) {
			throw new AirlineServiceException("Error: No se ha encontrado el número de vuelo en la base de datos.");
		}
		int availableSeats = availableSeats(flightNumber, flightDate);
		if(availableSeats <= 0) {
			throw new AirlineServiceException("Error: No quedan asientos disponibles para el vuelo seleccionado.");
		}
		//Cálculo del precio del ticket en céntimos
		double t = ChronoUnit.DAYS.between(purchaseDate, flightDate);//Days remaining from purchaseDate
		double nMax = vuelo.getAircraft().getSeatColumns() * vuelo.getAircraft().getSeatRows();//number of total seats in the aircraft
		double n = nMax - availableSeats ;//number of tickets already sold	
		double T = ((tMax-1)*7*Math.exp((1/2)-((49*(t*t))/8*(tRush*tRush)))/(2*tRush))+1;
		double O = ((1 - oMax)*Math.cos((n*Math.PI)/(nMax)) + oMax + 1)/2;
		double d = AirportQuery.geodesicDistance(vuelo.getOrigin().getLongitude(),vuelo.getOrigin().getLatitude(),vuelo.getDestination().getLongitude(),vuelo.getDestination().getLatitude());//distance in kilometers
		double B = 1000+10*d;
		int price = (int) Math.round(T*O*B);
		//System.out.println(price);
		
		ensureActiveManager();
		TicketJPADAO daoT = new TicketJPADAO(em);
		List<Integer> asiento = daoT.getFreeSeat(flightNumber, flightDate, vuelo.getAircraft());
		if(asiento == null || asiento.size() != 2) {
			throw new AirlineServiceException("Error: No se ha podido encontrar un asiento para el pasajero.");
		}
		Ticket ticket = new Ticket(firstName, lastName, asiento.get(0), asiento.get(1), flightDate, price, vuelo);
		EntityTransaction et = this.em.getTransaction();
		try {
			et.begin();
			ticket = daoT.create(ticket);
			et.commit();
			return ticket;
		} catch (Exception e) {
			if (et.isActive()) {
				et.rollback();
			}
			throw new AirlineServiceException("Error: ha ocurrido algo al acceder a la base de datos del aeropuerto.");
		} 
	}

	@Override
	public int availableSeats(Long flightNumber, LocalDate flightDate) throws AirlineServiceException {
		if(flightNumber == null) {
			throw new AirlineServiceException("Error: el número de vuelo no puede ser null.");
		}
		if(flightDate == null) {
			throw new AirlineServiceException("Error: las variables de fechas no puende ser null.");
		}
		Flight f = findFlight(flightNumber);
		if(f == null) {
			throw new AirlineServiceException("Error: no se ha encontrado el vuelo buscado.");
		}
		AircraftType aircraft = f.getAircraft();
		int totalSeats = aircraft.getSeatColumns() * aircraft.getSeatRows();
		TicketJPADAO daoT = new TicketJPADAO(em);
		List<Ticket> tickets = daoT.findSoldTicketsByDate(flightNumber, flightDate);
		if(tickets == null){
			return totalSeats;
		}else {
			return totalSeats-tickets.size();
		}
	}

	@Override
	public void cancelTicket(Long ticketNumber, LocalDate cancelDate) throws AirlineServiceException {
		if(ticketNumber == null) {
			throw new AirlineServiceException("Error: el número de ticket no puede ser null.");
		}
		if(cancelDate == null) {
			throw new AirlineServiceException("Error: las variables de fechas no puende ser null.");
		}
		LocalDate today = LocalDate.now();
		if(cancelDate.isBefore(today)) {
			throw new AirlineServiceException("Error: No se puede anular un billete pasado de fecha.");
		}
		TicketJPADAO daoT = new TicketJPADAO(em);
		Ticket ticket = daoT.find(ticketNumber);
		if(ticket == null) {
			throw new AirlineServiceException("Error: No se ha encontrado ningún ticket para eliminar.");
		}
		EntityTransaction et = this.em.getTransaction();
		try {
			et.begin();
			daoT.delete(ticket);
			et.commit();
		} catch (Exception e) {
			if (et.isActive()) {
				et.rollback();
			}
			throw new AirlineServiceException("Error: ha ocurrido algo al acceder a la base de datos del aeropuerto.");
		} 

	}

}
