package com.example.android.quakereport;

import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.ListMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake>{

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes){
        super(context,0,earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView =convertView;

        if (listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent,false);
        }

        Earthquake cuurentEarthquake = getItem(position);

        TextView magnitudeView = (TextView)listItemView.findViewById(R.id.magnitude);
        double magnitude = cuurentEarthquake.getMagnitude();
        DecimalFormat formatter = new DecimalFormat("0.0");
        magnitudeView.setText(formatter.format(magnitude));

        GradientDrawable magnitudeCircle = (GradientDrawable)magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(magnitude);
        magnitudeCircle.setColor(magnitudeColor);






        String originalLocation = cuurentEarthquake.getLocation();
        String primaryLocation;
        String locationOffset;
        if (originalLocation.contains(LOCATION_SEPARATOR)){
            String[] parts = originalLocation.split("of");
            primaryLocation = parts[1];
            locationOffset = parts[0]+LOCATION_SEPARATOR;
        }
        else {
            primaryLocation = originalLocation;
            locationOffset = getContext().getString(R.string.near_the);
        }

        TextView locationOffsetView = (TextView)listItemView.findViewById(R.id.location_offset);
        locationOffsetView.setText(locationOffset);

        TextView primaryLocationOffset = (TextView)listItemView.findViewById(R.id.primary_location);
        primaryLocationOffset.setText(primaryLocation);

        Date dateObject = new Date(cuurentEarthquake.getTimeInMilli());

        TextView dateView = (TextView)listItemView.findViewById(R.id.date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        String dateToDisplay = dateFormat.format(dateObject);

        dateView.setText(dateToDisplay);

        TextView timeView = (TextView)listItemView.findViewById(R.id.time);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String timeToDisplay = timeFormat.format(dateObject);

        timeView.setText(timeToDisplay);

        return listItemView;

    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeColorResourseId ;
        int magnitudeFloor = (int)Math.floor(magnitude);

        switch (magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourseId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourseId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourseId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourseId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourseId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourseId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourseId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourseId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourseId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourseId = R.color.magnitude10plus;
                break;
             }
return ContextCompat.getColor(getContext(),magnitudeColorResourseId);
    }
}
