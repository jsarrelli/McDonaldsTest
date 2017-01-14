package com.example.julisarrelli.mcdonaldstest.JavaClases.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julisarrelli.mcdonaldstest.JavaClases.Form;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Platform;
import com.example.julisarrelli.mcdonaldstest.JavaClases.User;
import com.example.julisarrelli.mcdonaldstest.R;

import java.util.ArrayList;

/**
 * Created by julisarrelli on 1/12/17.
 */
public class FormsListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    //int[] imagenes;
    ArrayList<String> names;
    LayoutInflater inflater;
    Platform plataform;

    public FormsListViewAdapter(Context context, ArrayList<String> names) {
        this.context = context;
        //  this.imagenes = imagenes;
        this.names=names;
        this.plataform=Platform.getInstance();

    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Form getItem(int position) {
        return plataform.getFormByName(names.get(position));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        ImageView imgImg;
        TextView txtTitle;
        TextView txtContenido;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.forms_listrow, parent, false);

        // Locate the TextViews in listview_item.xml
        imgImg = (ImageView) itemView.findViewById(R.id.imageView);
        txtTitle = (TextView) itemView.findViewById(R.id.list_names);

        // Capture position and set to the TextViews

        txtTitle.setText(names.get(position));


        return itemView;
    }
}