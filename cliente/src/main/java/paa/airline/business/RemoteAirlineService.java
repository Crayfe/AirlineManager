package paa.airline.business;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import paa.airline.model.AircraftType;
import paa.airline.model.Airport;
import paa.airline.model.Flight;
import paa.airline.model.Ticket;

public class RemoteAirlineService implements AirlineService {
	private static final String baseURL = "http://localhost:8080/p4-servidor/AirlineServer?action=";
	private ObjectMapper mapper;
	
	public RemoteAirlineService() {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		
	}
	private static String consultaURL(String url) throws AirlineServiceException {
		System.err.println(LocalDateTime.now() + ": " + url);
		try {
			HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
			int code = conn.getResponseCode();
			if(200 == code){
				String res = new String(conn.getInputStream().readAllBytes(), Charset.forName("UTF-8"));
				return res;
			}else{
				throw new AirlineServiceException("Some error in connection to " + url);
			}
		} catch (IOException e) {
			throw new AirlineServiceException("Some error in connection to " + url);
		}
	}
	@Override
	public Airport createAirport(String iataCode, String cityName, String airportName, double longitude,
			double latitude) throws AirlineServiceException {
		String url = baseURL + "createAirport" + "&iataCode="+iataCode+"&cityName="+URLEncoder.encode(cityName)+"&airportName="+URLEncoder.encode(airportName)+"&longitude="+longitude+"&latitude="+latitude;
		String response = consultaURL(url);
		//System.out.println(url);
		try {
			Airport airport = mapper.readValue(response, Airport.class);
			return airport;
		}catch (JsonParseException e){
			throw new AirlineServiceException(response);
		}catch (JsonMappingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}catch (JsonProcessingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}
	}

	@Override
	public List<Airport> listAirports() {
		try {
			String response = consultaURL(baseURL + "listAirports");
			System.out.println(response);
			List<Airport> airports = mapper.readValue(response, new TypeReference<List<Airport>>() {});
			return airports;
		}catch (Exception e) {
			return new ArrayList<Airport>();
		}
	}

	@Override
	public AircraftType createAircraft(String manufacturer, String model, int seatRows, int seatColumns)
			throws AirlineServiceException {
		String url = baseURL + "createAircraft" + "&manufacturer="+URLEncoder.encode(manufacturer)+"&model="+URLEncoder.encode(model)+"&seatRows="+seatRows+"&seatColumns="+seatColumns;
		String response = consultaURL(url);
		//System.out.println(url);
		try {
			AircraftType aircraft = mapper.readValue(response, AircraftType.class);
			return aircraft;
		}catch (JsonParseException e){
			throw new AirlineServiceException(response);
		}catch (JsonMappingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}catch (JsonProcessingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}
	}

	@Override
	public List<AircraftType> listAircraftTypes() {
		try {
			String response = consultaURL(baseURL + "listAircraftTypes");
			System.out.println(response);
			List<AircraftType> aircrafts = mapper.readValue(response, new TypeReference<List<AircraftType>>() {});
			return aircrafts;
		}catch (Exception e){
			return new ArrayList<AircraftType>();
		}
	}

	@Override
	public Flight createFlight(String originAirportCode, String destinationAirportCode, Long aircraftTypeCode)
			throws AirlineServiceException {
		String url = baseURL + "createFlight" + "&originAirportCode="+originAirportCode+"&destinationAirportCode="+destinationAirportCode+"&aircraftTypeCode="+aircraftTypeCode;
		String response = consultaURL(url);
		//System.out.println(url);
		try {
			Flight flight = mapper.readValue(response, Flight.class);
			return flight;
		}catch (JsonParseException e){
			throw new AirlineServiceException(response);
		}catch (JsonMappingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}catch (JsonProcessingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}
	}

	@Override
	public Flight findFlight(Long flightNumber) {
		String url = baseURL + "findFlight" + "&flightNumber="+flightNumber;
		//System.out.println(url);
		try {
			String response = consultaURL(url);
			Flight flight = mapper.readValue(response, Flight.class);
			return flight;
		}catch (Exception e){
			return null;
		}
	}

	@Override
	public List<Flight> listFlights() {
		try {
			String response = consultaURL(baseURL + "listFlights");
			System.out.println(response);
			List<Flight> aircrafts = mapper.readValue(response, new TypeReference<List<Flight>>() {});
			return aircrafts;
		}catch (Exception e){
			return new ArrayList<Flight>();
		}
	}

	@Override
	public Ticket purchaseTicket(String firstName, String lastName, Long flightNumber, LocalDate purchaseDate,
			LocalDate flightDate) throws AirlineServiceException {
		String url = baseURL + "purchaseTicket" + "&firstName="+URLEncoder.encode(firstName)+ "&lastName="+URLEncoder.encode(lastName)+ "&flightNumber="+flightNumber+"&purchaseDate="+purchaseDate+"&flightDate="+flightDate;
		String response = consultaURL(url);
		System.out.println(url);
		try {
			Ticket ticket = mapper.readValue(response, Ticket.class);
			return ticket;
		}catch (JsonParseException e){
			throw new AirlineServiceException(response);
		}catch (JsonMappingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}catch (JsonProcessingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}
	}

	@Override
	public int availableSeats(Long flightNumber, LocalDate flightDate) throws AirlineServiceException {
		String url = baseURL + "availableSeats"+"&flightNumber="+flightNumber+"&flightDate="+flightDate;
		String response = consultaURL(url);
		//System.out.println(url);
		try {
			int nSeats = mapper.readValue(response, Integer.class);
			return nSeats;
		}catch (JsonParseException e){
			throw new AirlineServiceException(response);
		}catch (JsonMappingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}catch (JsonProcessingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}
	}

	@Override
	public void cancelTicket(Long ticketNumber, LocalDate cancelDate) throws AirlineServiceException {
		String url = baseURL + "cancelTicket" +"&ticketNumber="+ticketNumber+"&cancelDate="+cancelDate;
		String response = consultaURL(url);
		try {
			String s = mapper.readValue(response, String.class);
			//System.out.println(s);
		}catch (JsonParseException e){
			throw new AirlineServiceException(response);
		}catch (JsonMappingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}catch (JsonProcessingException e){
			throw new AirlineServiceException("Unexpected error:" + e.getMessage());
		}
	}

}
