package com.example.pruebagmaps;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterEventos extends BaseAdapter{

	protected Activity activity;
    protected ArrayList<Event> items;
    
    public AdapterEventos(Activity activity, ArrayList<Event> items) {
	    this.activity = activity;
	    this.items = items;
	  }
    
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Generamos una convertView por motivos de eficiencia
				View v = convertView;

				//Asociamos el layout de la lista que hemos creado
				if(convertView == null){
					LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = inf.inflate(R.layout.item_evento, null);
				}

				// Creamos un objeto directivo
				Event ev = items.get(position);
				//Rellenamos la fotografía
				ImageView foto = (ImageView) v.findViewById(R.id.image_Evento);
				foto.setImageDrawable(ev.getFoto());
				//Rellenamos el nombre
				TextView nombre = (TextView) v.findViewById(R.id.nombre_evento);
				nombre.setText(ev.getNombre());
				//Rellenamos la descripcion
				TextView cargo = (TextView) v.findViewById(R.id.descripcion_evento);
				cargo.setText(ev.getDescripcion());

				// Retornamos la vista
				return v;
	}
	
}
