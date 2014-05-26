package com.example.pruebagmaps;


import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class Event implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected Drawable event_Image;
	protected String event_Name;
	protected String event_description;
	protected long event_id;
	protected double ev_Latitude;
	protected double ev_Longitude;
	protected String ev_Category;
	protected String ev_Price;
	protected String ev_Initial_Date;
	protected String ev_Final_Date;
	protected String ev_Creator;
	
	public Event(String nombreEvento, String descripcionEvento, long id,double Lat, double Long) {
        super();
        this.event_Image = null;
        this.event_Name = nombreEvento;
        this.event_description = descripcionEvento;
        this.ev_Latitude = Lat;
		this.ev_Longitude = Long;
        this.event_id = id;
        this.ev_Category = "";
        this.ev_Price = "";
        this.ev_Initial_Date = "";
        this.ev_Final_Date = "";
        this.ev_Creator = "";
    }
	
	public Event(Drawable eventPhoto, String nombreEvento, String descripcionEvento) {
        super();
        this.event_Image = eventPhoto;
        this.event_Name = nombreEvento;
        this.event_description = descripcionEvento;
        this.event_id = 0;
        this.ev_Latitude = 0;
		this.ev_Longitude = 0;
		this.ev_Category = "";
		this.ev_Price = "";
		this.ev_Initial_Date = "";
        this.ev_Final_Date = "";
    }
 
    public Event(Drawable eventPhoto, String nombreEvento, String descripcionEvento, long id,double Lat, double Long) {
        super();
        this.event_Image = eventPhoto;
        this.event_Name = nombreEvento;
        this.event_description = descripcionEvento;
        this.ev_Latitude = Lat;
		this.ev_Longitude = Long;
        this.event_id = id;
        this.ev_Category = "";
        this.ev_Price = "";
        this.ev_Initial_Date = "";
        this.ev_Final_Date = "";
    }
 
    public Drawable getFoto() {
        return event_Image;
    }
 
    public void setFoto(Drawable eventPhoto) {
        this.event_Image = eventPhoto;
    }
 
    public String getNombre() {
        return event_Name;
    }
 
    public void setNombre(String nombreEvento) {
        this.event_Name = nombreEvento;
    }
 
    public String getDescripcion() {
        return event_description;
    }
 
    public void setDescripcion(String descripcionEvento) {
        this.event_description = descripcionEvento;
    }
    
    public double getLatitude() {
		return ev_Latitude;
	}
	
	public double getLongitude() {
		return ev_Longitude;
	}
	
	public void setLatitude(double newLat) {
		ev_Latitude = newLat;
	}
	
	public void setLongitude(double newLong) {
		ev_Longitude = newLong;
	}
 
    public long getId() {
        return event_id;
    }
 
    public void setId(long id) {
        this.event_id = id;
    }
    
    public String getCategory() {
        return ev_Category;
    }
 
    public void setCategory(String category) {
        this.ev_Category = category;
    }
    
    public String getPrice() {
        return ev_Price;
    }
 
    public void setPrice(String price) {
        this.ev_Price = price;
    }
    
    public String getInitialDate() {
        return ev_Initial_Date;
    }
 
    public void setInitialDate(String initialDate) {
        this.ev_Initial_Date = initialDate;
    }
    
    public String getFinalDate() {
        return ev_Final_Date;
    }
 
    public void setFinalDate(String finalDate) {
        this.ev_Final_Date = finalDate;
    }

}

