package paa.airline.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import paa.airline.model.AircraftType;
import paa.airline.model.Airport;
import paa.airline.model.Flight;
public class airlineServiceTest {

	public airlineServiceTest() {
		// TODO Auto-generated constructor stub
	}
	
	@DisplayName("Pruebas del JPA")
	@Test
	void testJPA() throws AirlineServiceException{
		AirlineServiceJPA airlineService = new AirlineServiceJPA("AirlineServiceDB");
		Airport airport1 = new Airport("MAD","Madrid", "Barajas", 0, 0);
		Airport airport2= airlineService.createAirport("MAD","Madrid", "Barajas", 0, 0);
		assertTrue(airport1.equals(airport2));
		assertThrows(AirlineServiceException.class, () -> airlineService.createAirport("MAD","Madrid", "Barajas", 0, 0));
		//Si ya existe un aeropuerto no se puede añadir otra vez
		airlineService.createAirport("BAR","Barcelona", "El Prat", 10, 10);
		airlineService.createAirport("VLC","Valencia", "Cuart de Poblet", 13, 13);
		airlineService.createAirport("GAL","Galicia", "Santiago de compostela", 80, 50);
		List<Airport> aux1 = airlineService.listAirports();
		assertEquals(aux1.size(), 5);
		AircraftType aircra = airlineService.createAircraft("Airbus", "A319", 8, 16);
		AircraftType aircra1 = airlineService.createAircraft("Boeing", "737", 10, 13);
		List<AircraftType> aux2 = airlineService.listAircraftTypes();
		assertEquals(aux2.size(), 2);
		Flight flight = airlineService.createFlight("MAD", "VLC", 1L);
		Flight flight1 = airlineService.createFlight("VLC", "MAD", 1L);
		Flight flight2 = airlineService.createFlight("MAD", "BAR", 2L);
		List<Flight> aux3 = airlineService.listFlights();
		assertEquals(aux3.size(), 3);

	}
	

}
