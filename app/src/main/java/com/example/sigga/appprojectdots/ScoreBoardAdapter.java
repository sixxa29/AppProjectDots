package com.example.sigga.appprojectdots;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kristin on 11/09/15.
 */
public class ScoreBoardAdapter extends ArrayAdapter<Score> {

    private final Context context;
    private final List<Score> values;

    public ScoreBoardAdapter(Context context, List<Score> objects) {
        super(context, -1, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent,false);

        TextView nameView = (TextView) rowView.findViewById(R.id.row_name);
        nameView.setText( values.get(position).getName() );

        TextView pointView = (TextView) rowView.findViewById(R.id.row_points);
        String points = String.valueOf(values.get(position).getPoints());
        pointView.setText(points);


        return rowView;
    }

}
