package com.deezer.sdk.harmonicfield.ui;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deezer.sdk.harmonicfield.R;

public class CustomListAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private final String[] itemname;
	private final Integer[] imgid;
	private int vi_NotasLiberadas;
	
	public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid,
							 int aiNotasLiberadas )
	{
		super(context, R.layout.mylist, itemname);
		// TODO Auto-generated constructor stub
		vi_NotasLiberadas = aiNotasLiberadas;
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
			TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);

			txtTitle.setText(itemname[position]);
			imageView.setImageResource(imgid[position]);
			if(position >= vi_NotasLiberadas)
			{
				rowView.setBackgroundColor(Color.GRAY);
				txtTitle.setTextColor(Color.WHITE);
			}
			extratxt.setText("Quantidade de músicas:4");
			return rowView;
	};
}
