package com.example.pruebagmaps;

import java.util.List;

public class contextEventsMap {
	
	//Attributes
	private List<Event> listEvents;
	
	// Constructor
	public contextEventsMap() {}

	//Getters and Setters
	
	/**
	 * @return the listEvents
	 */
	public List<Event> getListEvents() {
		return listEvents;
	}

	/**
	 * @param listEvents the listEvents to set
	 */
	public void setListEvents(List<Event> listEvents) {
		this.listEvents = listEvents;
	}
	
	//Methods
	
	public Event getEvent(int idEvent) {
		for(int i =0; i< this.listEvents.size(); i++){
			if (this.listEvents.get(i).getId() == idEvent) {
				return this.listEvents.get(i);
			}
		}
		return null;
	}
	
	public void insertEvent(Event newEvent) {
		this.listEvents.add(newEvent);
	}
	
	public void deleteEvent(Event event){
		int idEvent = (int) event.getId();
		for(int i =0; i< this.listEvents.size(); i++){
			if (this.listEvents.get(i).getId() == idEvent) {
				this.listEvents.remove(i);
				break;
			}
		}
	}

}
