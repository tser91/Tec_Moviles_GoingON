package com.example.pruebagmaps;

import android.media.Image;

public class LocalUser extends User {

	private double latitude;
	private double longitude;
	
	public LocalUser(String name, String email, Image image, String description, int user_count_F, double lati, double longi) {
		super(name, email, image, description, user_count_F);
		setLatitude(lati);
		setLongitude(longi);
		// TODO Auto-generated constructor stub
	}
	
	public LocalUser(String email) {
		super(email);
		// TODO Auto-generated constructor stub
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}
