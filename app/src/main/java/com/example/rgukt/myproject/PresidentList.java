package com.example.rgukt.myproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PresidentList extends ArrayAdapter<President> {

    private Activity context;
    private List<President> presidentList;



    public PresidentList(Activity context, List<President> presidentList) {
        super(context,R.layout.list_presidents,presidentList);
        this.context=context;
        this.presidentList=presidentList;



    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View view=inflater.inflate(R.layout.list_presidents,null,true);

        TextView textViewUsername=(TextView)view.findViewById(R.id.textViewUsername);
        TextView textViewMandal=(TextView)view.findViewById(R.id.textViewMandal);
        TextView textViewVillage=(TextView)view.findViewById(R.id.textViewVillage);
        TextView textViewDistrict=(TextView)view.findViewById(R.id.textViewDistrict);




        President president=presidentList.get(position);

        textViewUsername.setText(president.getpUsername());
        textViewDistrict.setText(president.getpDistrict());
        textViewMandal.setText(president.getpMandal());
        textViewVillage.setText(president.getpVillage());



        return view;
    }

}
