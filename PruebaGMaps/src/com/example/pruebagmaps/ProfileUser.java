package com.example.pruebagmaps;


import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ProfileUser extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil_usuario);
		
		ListView lista = (ListView) findViewById(R.id.listaeventos);
        ArrayList<Evento> arraydir = new ArrayList<Evento>();
        Evento evento;
 
        // Introduzco los datos
        
        evento = new Evento(getResources().getDrawable(R.drawable.ic_concierto), "DJ Tiesto", "Costa Rica presenta al mejor DJ del mundo");
        arraydir.add(evento);
        evento = new Evento(getResources().getDrawable(R.drawable.ic_cafeteria), "Capuccinos 2x1", "Disfrute de un delicioso capuccino con la mejor compania");
        arraydir.add(evento);
        evento = new Evento(getResources().getDrawable(R.drawable.ic_discoteca), "Black Night", "Venga y viva la mejor noche vistiendo de negro");
        arraydir.add(evento);
        evento = new Evento(getResources().getDrawable(R.drawable.ic_restaurante), "Cena de Lujo", "Le ofrecemos una lujosa cena con Louis");
        arraydir.add(evento);
        evento = new Evento(getResources().getDrawable(R.drawable.ic_teatro), "Andres Lopez", "Presenta la pelota de letras");
        arraydir.add(evento);
 
        // Creo el adapter personalizado
        AdapterEventos adapter = new AdapterEventos(this, arraydir);
 
        // Lo aplico
        lista.setAdapter(adapter);
	}
	

	
}
