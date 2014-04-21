package com.example.pruebagmaps;

import android.media.Image;

public abstract class User {
	
	//Attributes
	private String user_Name;
	private String user_Email;
	private Image user_Img;
	private String user_Description;
	private int user_count_F;
	
	//Constructor
	public User(String name, String email, Image image, String description, int countF) {
		user_Name = name;
		user_Email = email;
		user_Img = image;
		user_Description = description;
		user_count_F = countF;
	}
	
	public User(String email) {
		user_Name = "";
		user_Email = email;
		user_Img = null;
		user_Description = "";
		user_count_F = 0;
	}
	
	//Getters
	public String getName(){
		return user_Name;
	}
	
	public String getEmail() {
		return user_Email;
	}
	
	public Image getImage() {
		return user_Img;
	}
	
	public String getDescription() {
		return user_Description;
	}
	
	//Setters
	public void setName(String name) {
		user_Name = name;
	}
	
	public void setEmail(String mail) {
		user_Email = mail;
	}
	
	public void setImage(Image image) {
		user_Img = image;
	}
	
	public void setDescription(String description) {
		user_Description = description;
	}

	/**
	 * @return the user_count_F
	 */
	public int getUser_count_F() {
		return user_count_F;
	}

	/**
	 * @param user_count_F the user_count_F to set
	 */
	public void setUser_count_F(int user_count_F) {
		this.user_count_F = user_count_F;
	}
}
