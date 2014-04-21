package com.example.pruebagmaps;

import android.media.Image;

public class Ticket {
	
	//Attributes
	private int ticket_Id;
	private Image ticket_Img;
	private String ticket_Description;
	private int ticket_CreatorId;
	private boolean ticket_PublicView;
	private String ticket_CreatorName;
	private boolean ticket_Valid;
	
	//Constructor
	public Ticket(Image Img, String Description, String CreatorName) {
		ticket_Id = 0;
		ticket_Img = Img;
		ticket_Description = Description;
		ticket_CreatorId = 0;
		ticket_PublicView = false;
		ticket_CreatorName = "";
		ticket_Valid = true;
	}
	
	//Getters and Setters
	/**
	 * @return the ticket_Id
	 */
	public int getTicket_Id() {
		return ticket_Id;
	}
	/**
	 * @param ticket_Id the ticket_Id to set
	 */
	public void setTicket_Id(int ticket_Id) {
		this.ticket_Id = ticket_Id;
	}
	/**
	 * @return the ticket_Img
	 */
	public Image getTicket_Img() {
		return ticket_Img;
	}
	/**
	 * @param ticket_Img the ticket_Img to set
	 */
	public void setTicket_Img(Image ticket_Img) {
		this.ticket_Img = ticket_Img;
	}
	/**
	 * @return the ticket_Description
	 */
	public String getTicket_Description() {
		return ticket_Description;
	}
	/**
	 * @param ticket_Description the ticket_Description to set
	 */
	public void setTicket_Description(String ticket_Description) {
		this.ticket_Description = ticket_Description;
	}
	/**
	 * @return the ticket_CreatorId
	 */
	public int getTicket_CreatorId() {
		return ticket_CreatorId;
	}
	/**
	 * @param ticket_CreatorId the ticket_CreatorId to set
	 */
	public void setTicket_CreatorId(int ticket_CreatorId) {
		this.ticket_CreatorId = ticket_CreatorId;
	}
	/**
	 * @return the ticket_PublicView
	 */
	public boolean isTicket_PublicView() {
		return ticket_PublicView;
	}
	/**
	 * @param ticket_PublicView the ticket_PublicView to set
	 */
	public void setTicket_PublicView(boolean ticket_PublicView) {
		this.ticket_PublicView = ticket_PublicView;
	}
	/**
	 * @return the ticket_CreatorName
	 */
	public String getTicket_CreatorName() {
		return ticket_CreatorName;
	}
	/**
	 * @param ticket_CreatorName the ticket_CreatorName to set
	 */
	public void setTicket_CreatorName(String ticket_CreatorName) {
		this.ticket_CreatorName = ticket_CreatorName;
	}
	/**
	 * @return the ticket_Valid
	 */
	public boolean isTicket_Valid() {
		return ticket_Valid;
	}
	/**
	 * @param ticket_Valid the ticket_Valid to set
	 */
	public void setTicket_Valid(boolean ticket_Valid) {
		this.ticket_Valid = ticket_Valid;
	}
	
	
	//Methods
	
	 //Validates the ticket (so it can't be used anymore)
	public void validateTicket() {
		setTicket_Valid(false);
	}
}
