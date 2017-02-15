package com.example.android.quakereport;

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilli;
    private String mUrl;

   public Earthquake(double magnitude,String location,long timeInMilli,String url){
       mMagnitude=magnitude;
       mLocation= location;
       mTimeInMilli = timeInMilli;
       mUrl=url;
   }

    public double getMagnitude(){
        return mMagnitude;
    }
    public String getLocation(){
        return mLocation;
    }
    public long getTimeInMilli(){
        return mTimeInMilli;
    }
    public String getUrl(){return mUrl;}

}
