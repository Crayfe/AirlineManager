package paa.airline.persistence;

import java.time.LocalDate;

import javax.persistence.EntityManager;

import paa.airline.model.Flight;

public class FlightJPADAO extends JPADAO<Flight, Long> {

	public FlightJPADAO(EntityManager em) {
		super(em, Flight.class);

	}
}
