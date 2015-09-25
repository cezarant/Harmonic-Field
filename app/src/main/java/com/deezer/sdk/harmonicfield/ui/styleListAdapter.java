package com.deezer.sdk.harmonicfield.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deezer.sdk.harmonicfield.R;

/**
 * Created by cezar on 07/09/2015.
 */
public class styleListAdapter extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public styleListAdapter(Activity context, String[] itemname, Integer[] imgid,
                             int aiNotasLiberadas )
    {
        super(context, R.layout.mylist, itemname);
        // TODO Auto-generated constructor stub
        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent)
    {
        View rowView = null;
        LayoutInflater inflater=context.getLayoutInflater();
        rowView =inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;
    };
}

