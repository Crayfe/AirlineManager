package paa.airline.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import paa.airline.business.AirlineServiceException;
import paa.airline.business.AirlineServiceJPA;
import paa.airline.model.AircraftType;
import paa.airline.model.Airport;
import paa.airline.model.Flight;
import paa.airline.model.Ticket;

/**
 * Servlet implementation class AirlineServer
 */
@WebServlet("/AirlineServer")
public class AirlineServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	AirlineServiceJPA as;
	ObjectMapper mapper;
    
    public AirlineServer() {
        super();
        mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String absoluteDiskPath = getServletContext().getRealPath("./bd_ejemplo");
		try {
			this.as = new AirlineServiceJPA("AirlineServiceDB", absoluteDiskPath);
			System.out.println("Se carga el servlet nuevo AirlineServiceJPA");
		} catch(Exception e) {
			System.out.println("Error al instanciar AirlineServiceJPA");
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	@Override
    public void destroy() {
    	as.close();
    	as = null;
    	super.destroy();
    	System.out.println("Se destruye el servlet");
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action == null) {
			response.sendError(400, "Incorrect URL format");
			return;
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		switch (action) {
			case "createAirport":
				createAirport(request, response);
				break;
			case "listAirports":
				listAirports(request, response);
				break;
			case "createAircraft":
				createAircraft(request, response);
				break;
			case "listAircraftTypes":
				listAircraftTypes(request, response);
				break;
			case "createFlight":
				createFlight(request, response);
				break;
			case "findFlight":
				findFlight(request, response);
				break;
			case "listFlights":
				listFlights(request, response);
				break;
			case "purchaseTicket":
				purchaseTicket(request, response);
				break;
			case "availableSeats":
				availableSeats(request, response);
				break;
			case "cancelTicket":
				cancelTicket(request, response);
				break;
			default:
				response.sendError(400, "Unsupported action");
		}
		
		/*
		response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
		out.println();
		out.println("Son las " + LocalDateTime.now());
		*/
		
	}

	private void createAirport(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		//Parámetros
		String iataCode = request.getParameter("iataCode");
		String cityName = request.getParameter("cityName");
		String airportName = request.getParameter("airportName");
		String longitude = request.getParameter("longitude");
		String latitude = request.getParameter("latitude");
		if (iataCode == null) {
			response.sendError(400, "Invalid URL: missing parameter 'iataCode'");
			return;
		}
		if (cityName == null) {
			response.sendError(400, "Invalid URL: missing parameter 'cityName'");
			return;
		}
		if (airportName == null) {
			response.sendError(400, "Invalid URL: missing parameter 'airportName'");
			return;
		}
		if (longitude == null) {
			response.sendError(400, "Invalid URL: missing parameter 'longitude'");
			return;
		}
		if (latitude == null) {
			response.sendError(400, "Invalid URL: missing parameter 'latitude'");
			return;
		}
		try {
			Airport airport = this.as.createAirport(iataCode, cityName, airportName, Double.parseDouble(longitude), Double.parseDouble(latitude));
			if (airport != null)
				mapper.writeValue(response.getWriter(), airport);
		} catch (AirlineServiceException e) {
			response.getWriter().print(e.getMessage());
		}
		
	}
	
	private void listAirports(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// No tiene parámetros
		try {
			List<Airport> airports = this.as.listAirports();
			mapper.writeValue(response.getWriter(), airports);
		} catch (Exception e) {
			response.getWriter().print(e.getMessage());
		}
	}
	
	private void createAircraft(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		//Parámetros
		String manufacturer = request.getParameter("manufacturer");
		String model = request.getParameter("model");
		String seatRows = request.getParameter("seatRows");
		String seatColumns = request.getParameter("seatColumns");
		if (manufacturer == null) {
			response.sendError(400, "Invalid URL: missing parameter 'manufacturer'");
			return;
		}
		if (model == null) {
			response.sendError(400, "Invalid URL: missing parameter 'model'");
			return;
		}
		if (seatRows == null) {
			response.sendError(400, "Invalid URL: missing parameter 'seatRows'");
			return;
		}
		if (seatColumns == null) {
			response.sendError(400, "Invalid URL: missing parameter 'seatColumns'");
			return;
		}
		try {
			AircraftType aircraft = this.as.createAircraft(manufacturer, model, Integer.parseInt(seatRows), Integer.parseInt(seatColumns));
			mapper.writeValue(response.getWriter(), aircraft);
		} catch (AirlineServiceException e) {
			response.getWriter().print(e.getMessage());
		}
		
	}
	
	private void listAircraftTypes(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException  {
		// No tiene parámetros
		try {
			List<AircraftType> aircrafts = this.as.listAircraftTypes();
			mapper.writeValue(response.getWriter(), aircrafts);
		} catch (Exception e) {
			response.getWriter().print(e.getMessage());
		}
	}
	private void createFlight(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Parámetros
		String originAirportCode = request.getParameter("originAirportCode");
		String destinationAirportCode = request.getParameter("destinationAirportCode");
		String aircraftTypeCode = request.getParameter("aircraftTypeCode");
		if (originAirportCode == null) {
			response.sendError(400, "Invalid URL: missing parameter 'originAirportCode'");
			return;
		}
		if (destinationAirportCode == null) {
			response.sendError(400, "Invalid URL: missing parameter 'destinationAirportCode'");
			return;
		}
		if (aircraftTypeCode == null) {
			response.sendError(400, "Invalid URL: missing parameter 'aircraftTypeCode'");
			return;
		}
		try {
			Flight flight = this.as.createFlight(originAirportCode, destinationAirportCode, Long.parseLong(aircraftTypeCode));
			mapper.writeValue(response.getWriter(), flight);
		} catch (AirlineServiceException e) {
			response.getWriter().print(e.getMessage());
		}
	}
	
	private void findFlight(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Parámetros
		String flightNumber = request.getParameter("flightNumber");
		if (flightNumber == null) {
			response.sendError(400, "Invalid URL: missing parameter 'flightNumber'");
			return;
		}
		try {
			Flight flight = this.as.findFlight(Long.parseLong(flightNumber));
			mapper.writeValue(response.getWriter(), flight);
		} catch (Exception e) {
			response.getWriter().print(e.getMessage());
		}
	}
	
	private void listFlights(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// No tiene parámetros
		try {
			List<Flight> flights = this.as.listFlights();
			mapper.writeValue(response.getWriter(), flights);
		} catch (Exception e) {
			response.getWriter().print(e.getMessage());
		}
	}
	
	private void purchaseTicket(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Parámetros
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String flightNumber = request.getParameter("flightNumber");
		String purchaseDate = request.getParameter("purchaseDate");
		String flightDate = request.getParameter("flightDate");
		if (firstName == null) {
			response.sendError(400, "Invalid URL: missing parameter 'firstName'");
			return;
		}
		if (lastName == null) {
			response.sendError(400, "Invalid URL: missing parameter 'lastName'");
			return;
		}
		if (flightNumber == null) {
			response.sendError(400, "Invalid URL: missing parameter 'flightNumber'");
			return;
		}
		if (purchaseDate == null) {
			response.sendError(400, "Invalid URL: missing parameter 'purchaseDate'");
			return;
		}
		if (flightDate == null) {
			response.sendError(400, "Invalid URL: missing parameter 'flightDate'");
			return;
		}
		try {
			Ticket ticket = this.as.purchaseTicket(firstName, lastName, Long.parseLong(flightNumber), LocalDate.parse(purchaseDate),  LocalDate.parse(flightDate));
			mapper.writeValue(response.getWriter(), ticket);
		} catch (AirlineServiceException e) {
			response.getWriter().print(e.getMessage());
		}
		
	}
	
	private void availableSeats(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Parámetros
		String flightNumber = request.getParameter("flightNumber");
		String flightDate = request.getParameter("flightDate");
		if (flightNumber == null) {
			response.sendError(400, "Invalid URL: missing parameter 'flightNumber'");
			return;
		}
		if (flightDate == null) {
			response.sendError(400, "Invalid URL: missing parameter 'flightDate'");
			return;
		}
		try {
			int i = this.as.availableSeats(Long.parseLong(flightNumber),  LocalDate.parse(flightDate));
			mapper.writeValue(response.getWriter(), i);
		} catch (AirlineServiceException e) {
			response.getWriter().print(e.getMessage());
		}
	}
	
	private void cancelTicket(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Parámetros
		String ticketNumber = request.getParameter("ticketNumber");
		String cancelDate = request.getParameter("cancelDate");
		if (ticketNumber == null) {
			response.sendError(400, "Invalid URL: missing parameter 'ticketNumber'");
			return;
		}
		if (cancelDate == null) {
			response.sendError(400, "Invalid URL: missing parameter 'cancelDate'");
			return;
		}
		try {
			this.as.cancelTicket(Long.parseLong(ticketNumber), LocalDate.parse(cancelDate));
			mapper.writeValue(response.getWriter(), "Done");
		} catch (AirlineServiceException e) {
			response.getWriter().print(e.getMessage());
		}
		
	}

}
