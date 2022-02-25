package me.invertmc.utils;

import java.util.ArrayList;

public class OnlineTime {

    int hours[] = new int[24];

    public int getHour(int i) {
        i--;
        if(i < 0 || i > 23){
            return 0;
        }
        return hours[i];
    }

    public void setHour(int i, int int1) {
        i--;
        if(i < 0 || i > 23){
            return;
        }
        hours[i] = int1;
    }

    public ArrayList<String> getPlayerOutput(int offset) {
        double[] percents = getPercents();
        String[] colors = getColors(percents);
        ArrayList<String> str = new ArrayList<String>();
        String b = "";
        for(int i = 1; i <= 24; i++){

            int hour = i - offset;
            if(hour < 1){
                hour+=24;
            }else if(hour > 24){
                hour -= 24;
            }

            if(!b.equals("")){
                b += "\u00a77 ~ ";
            }
            boolean pm = (hour) > 12;
            b +=  colors[i-1] + to12Hour(hour) + " " + (pm ? "AM" : "PM") + " " + percents[i-1] + "%";

            if((i) % 4 == 0){
                str.add(b);
                b = "";
            }
        }
        return str;
    }

    private int to12Hour(int i) {
        if(i > 12){
            return i-12;
        }else{
            return i;
        }
    }

    private String[] getColors(double[] percents) {
        String[] colors = new String[24];
        for(int i = 0; i < 24; i++){
            if(percents[i] > 30){
                colors[i] = "\u00a7b";
            }else if(percents[i] >= 15){
                colors[i] = "\u00a7a";
            }else if(percents[i] >= 10){
                colors[i] = "\u00a72";
            }else if(percents[i] >= 5){
                colors[i] = "\u00a7e";
            }else if(percents[i] >= 2){
                colors[i] = "\u00a76";
            }else if(percents[i] > 0){
                colors[i] = "\u00a7c";
            }else if(percents[i] == 0){
                colors[i] = "\u00a74";
            }else{
                colors[i] = "\u00a70";
            }
        }
        return colors;
    }

    private int getTotal(){
        int total = 0;
        for(int i = 0; i < 24; i++){
            total += hours[i];
        }
        return total;
    }

    private double[] getPercents() {
        double total = getTotal();
        double[] per = new double[24];
        for(int i = 0; i < 24; i++){
            per[i] = Math.round((((double)hours[i]) / total) * 100);
        }
        return per;
    }

    public double getOnlinePercent(long firstJoinTime) {
        double total = getTotal();
        if(total > ((double)((System.currentTimeMillis() - firstJoinTime) / 3600000))){
            return -1;
        }
        return Math.round((getTotal() / ((double)((System.currentTimeMillis() - firstJoinTime) / 3600000))) * 10000) / 100;
    }

    public void add(OnlineTime t) {
        for(int i = 0; i < 24; i++){
            hours[i] += t.hours[i];
        }
    }

}