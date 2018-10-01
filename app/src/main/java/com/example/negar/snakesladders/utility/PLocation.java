package com.example.negar.snakesladders.utility;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import 	java.lang.Math;

public class PLocation {
    private double boardCenterLat;
    private double boardCenterLong;

    private double mWorldWidth;
    private double xCenter;
    private double yCenter;
    public double MeterPerPixel;


    public PLocation(double centerLat,double centerLong,double mWorldWidth,double MeterPerPixel){
        boardCenterLat=centerLat;
        boardCenterLong=centerLong;
        this.mWorldWidth=mWorldWidth;
        this.MeterPerPixel=MeterPerPixel;

        xCenter = (centerLong / 360 + .5)* mWorldWidth;
        final double siny = Math.sin(Math.toRadians(centerLat));
        yCenter = mWorldWidth*(0.5 * Math.log((1 + siny) / (1 - siny)) / -(2 * Math.PI) + .5);
    }

    public Point toPoint(Location location) {
        final double x = (location.getLongitude() / 360 + .5)* mWorldWidth;
        final double siny = Math.sin(Math.toRadians(location.getLatitude()));
        final double y = mWorldWidth*(0.5 * Math.log((1 + siny) / (1 - siny)) / -(2 * Math.PI) + .5);
        return new Point( (int) (Math.abs(x-xCenter)/MeterPerPixel) , (int) (Math.abs(y-yCenter)/MeterPerPixel) );
    }

    public String toLatLng(Point point,int accuracy) {
        double x=point.x*MeterPerPixel +xCenter;
        final double lng = (x / mWorldWidth - 0.5)*360;

        double y=0.5-(point.y*MeterPerPixel+yCenter)/mWorldWidth;
        final double lat = 90 - Math.toDegrees(Math.atan(Math.exp(-y * 2 * Math.PI)) * 2);

        return "lat=\""+String.format("%."+accuracy+"f", lat)+"\" lon=\""+String.format("%."+accuracy+"f", lng)+"\"";
    }

    public void writeToFile(int sqWidth,int sqHeight,int boardSize,int delay) {
        String beginning= "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n" +
                "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.16.1\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-\n" +
                "\n" +
                "instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">\n" +
                "\n" +
                "<trk>\n" +
                "<name>emulate</name>\n" +
                "<trkseg>\n" ;
        String ending= "\n</trkseg>\n" +
                "</trk>\n" +
                "</gpx>";
        String locations="";
        for (int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){

                locations+="<trkpt "+toLatLng(new Point(i*sqWidth+50,j*sqHeight+50),4)+">\n";
                locations+="<ele>0.000000</ele><time>"+"2014-03-05T20:00:"+delay*(i*boardSize+j)+"z</time></trkpt>\n";
            }

        }
        Log.d("gpx data","\n"+beginning+locations+ending);

    }
}
