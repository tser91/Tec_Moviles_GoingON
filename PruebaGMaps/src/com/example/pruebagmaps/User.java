package com.example.pruebagmaps;

import android.media.Image;

public abstract class User {
	
	//Attributes
	private String user_Name;
	private int user_Id;
	private Image user_Img;
	private String user_Description;
	
	//Constructor
	public User(String name, Image image, String description) {
		user_Name = name;
		user_Id = 0;
		user_Img = image;
		user_Description = description;
	}
	
	//Getters
	public String getName(){
		return user_Name;
	}
	
	public int getId() {
		return user_Id;
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
	
	public void setId(int id) {
		user_Id = id;
	}
	
	public void setImage(Image image) {
		user_Img = image;
	}
	
	public void setDescription(String description) {
		user_Description = description;
	}
}
