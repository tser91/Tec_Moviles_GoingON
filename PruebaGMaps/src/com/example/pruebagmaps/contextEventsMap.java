package com.example.pruebagmaps;

import java.util.List;

public class contextEventsMap {
	
	//Attributes
	private List<Evento> listEvents;
	
	// Constructor
	public contextEventsMap() {}

	//Getters and Setters
	
	/**
	 * @return the listEvents
	 */
	public List<Evento> getListEvents() {
		return listEvents;
	}

	/**
	 * @param listEvents the listEvents to set
	 */
	public void setListEvents(List<Evento> listEvents) {
		this.listEvents = listEvents;
	}
	
	//Methods
	
	public Evento getEvent(int idEvent) {
		for(int i =0; i< this.listEvents.size(); i++){
			if (this.listEvents.get(i).getId() == idEvent) {
				return this.listEvents.get(i);
			}
		}
		return null;
	}
	
	public void insertEvent(Evento newEvent) {
		this.listEvents.add(newEvent);
	}
	
	public void deleteEvent(Evento event){
		int idEvent = (int) event.getId();
		for(int i =0; i< this.listEvents.size(); i++){
			if (this.listEvents.get(i).getId() == idEvent) {
				this.listEvents.remove(i);
				break;
			}
		}
	}

}
