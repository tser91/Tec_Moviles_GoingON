package com.example.pruebagmaps;

import android.media.Image;

public class LocalUser extends User {

	public LocalUser(String name, String email, Image image, String description, int user_count_F) {
		super(name, email, image, description, user_count_F);
		// TODO Auto-generated constructor stub
	}
	
	public LocalUser(String email) {
		super(email);
		// TODO Auto-generated constructor stub
	}

}
