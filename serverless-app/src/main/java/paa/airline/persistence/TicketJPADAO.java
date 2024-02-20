package paa.airline.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import paa.airline.model.AircraftType;
import paa.airline.model.Ticket;

public class TicketJPADAO extends JPADAO<Ticket, Long> {

	public TicketJPADAO(EntityManager em) {
		super(em, Ticket.class);

	}
	public List<Integer> getFreeSeat(Long flightNumber, LocalDate date, AircraftType aircraft){
		int columnas = aircraft.getSeatColumns();
		int filas = aircraft.getSeatRows();
		int capacidad = columnas * filas;
		List<Ticket> tickets = findSoldTicketsByDate(flightNumber, date);
		if(tickets == null) {
			return List.of(1,1);
		}else {
			
			List<Integer> indicesOcupados = new ArrayList<>();
			for (Ticket b: tickets) {
				int indice = columnas * (b.getSeatRow() - 1) + b.getSeatColumn() - 1;
				indicesOcupados.add(indice);
			}
			List<Integer> indicesPosibles = new ArrayList<Integer>();
			for (int i = 0; i < capacidad; i++) {
				indicesPosibles.add(i);
			}
			Set<Integer> indicesLibres = new HashSet<Integer>(indicesPosibles);
			indicesLibres.removeAll(indicesOcupados);
			int index = indicesLibres.iterator().next();
			return List.of((index/columnas) + 1, (index%columnas) + 1);
			
		}
		
	}
	//SELECT * FROM TICKETS WHERE FLIGHTDATE  = '2023-04-10'AND FLIGHT_FLIGHTNUMBER = 2
	public List<Ticket> findSoldTicketsByDate(Long flightNumber, LocalDate date){
		TypedQuery<Ticket> q = em.createQuery("select t from " + super.clazz.getName() + " t where t.flightDate = :fecha and FLIGHT_FLIGHTNUMBER = :flightNumber", clazz);
		
		q.setParameter("fecha", date);
		q.setParameter("flightNumber", flightNumber);
    	List<Ticket> results = q.getResultList();
    	return results;

		
	}

}
