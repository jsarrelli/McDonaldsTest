package com.example.julisarrelli.mcdonaldstest.JavaClases.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.julisarrelli.mcdonaldstest.CompletedForms;
import com.example.julisarrelli.mcdonaldstest.JavaClases.Local;
import com.example.julisarrelli.mcdonaldstest.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by julisarrelli on 1/12/17.
 */
public class CompletedFormsListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;
    //int[] imagenes;
    ArrayList<String> names=new ArrayList<String>();
    ArrayList<String> dates=new ArrayList<String>();
    ArrayList<String> users=new ArrayList<String>();

    ArrayList<Integer> ids=new ArrayList<Integer>();
    ArrayList<String> locals=new ArrayList<String >();
    LayoutInflater inflater;

    public CompletedFormsListViewAdapter(Context context,ArrayList<Integer>ids, ArrayList<String> names,
                                         ArrayList<String>users,ArrayList<String>dates,ArrayList<String>locals ) {
        this.context = context;
        //  this.imagenes = imagenes;

       this.names=names;
        this.ids=ids;
        this.dates=dates;
        this.users=users;
        this.locals=locals;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public HashMap<String,String> getItem(int position) {

        String name=names.get(position);
        String user=users.get(position);
        String date=dates.get(position);
        String local=locals.get(position);

        int id=ids.get(position);

        HashMap<String,String>answer=new HashMap<>();
        answer.put("id", String.valueOf(id));
        answer.put("name",name);
        answer.put("user",user);
        answer.put("date",date);
        answer.put("local",local);


        return answer;
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
        TextView txtContenido2;
        TextView txtContenido3;


        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.completedforms_listrow, parent, false);

        // Locate the TextViews in listview_item.xml
        //imgImg = (ImageView) itemView.findViewById(R.id.imageView);
        txtTitle = (TextView) itemView.findViewById(R.id.list_name);
        txtContenido = (TextView) itemView.findViewById(R.id.list_user);
        txtContenido2 = (TextView) itemView.findViewById(R.id.list_date);
        txtContenido3 = (TextView) itemView.findViewById(R.id.list_local);

        // Capture position and set to the TextViews

        txtTitle.setText(names.get(position));
        txtContenido.setText(users.get(position));
        txtContenido2.setText(dates.get(position));
        txtContenido3.setText(locals.get(position));

        return itemView;
    }
}