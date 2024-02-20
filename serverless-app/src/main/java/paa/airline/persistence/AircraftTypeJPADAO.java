package paa.airline.persistence;

import javax.persistence.EntityManager;

import paa.airline.model.AircraftType;

public class AircraftTypeJPADAO extends JPADAO<AircraftType, Long> {

	public AircraftTypeJPADAO(EntityManager em) {
		super(em, AircraftType.class);
	}

}
