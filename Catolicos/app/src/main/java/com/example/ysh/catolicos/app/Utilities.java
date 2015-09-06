package com.example.ysh.catolicos.app;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.String.*;

/*
 * Created by YSH on 09/07/2015.
 */
public class Utilities {


    public Date myhour(String inhour){

        Date dateObj = new Date();

        String string_date = "20:00";

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        try {

            Date d = format.parse(string_date);
            int i = d.getHours();
            long milliseconds = d.getTime();



        } catch (final ParseException e) {
            e.printStackTrace();
        }





        String[] formats = new String[] {
                "yyyy-MM-dd",
                "yyyy-MM-dd HH:mm",
                "yyyy-MM-dd HH:mmZ",
                "yyyy-MM-dd HH:mm:ss.SSSZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        };

        String myHour = "HH:mm";
        SimpleDateFormat hour = new SimpleDateFormat(inhour, Locale.US);
        String time = "22:35";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(inhour, Locale.US);
            dateObj = sdf.parse(time);
            System.out.println(dateObj);
            System.out.println(new SimpleDateFormat("K:mm").format(dateObj));
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return dateObj;
    }

    public String Rawdayofweek(String dayweek){

        String ret;

        switch(dayweek) {
            case "Domingo":
                ret = "domingo";
                break;

            case "Segunda-feira":
                ret = "segunda";
                break;

            case "Terça-feira":
                ret = "terca";
                break;

            case "Quarta-feira":
                ret = "quarta";
                break;

            case "Quinta-feira":
                ret = "quinta";
                break;

            case "Sexta-feira":
                ret = "sexta";
                break;

            case "Sábado":
                ret = "sabado";
                break;

            default:
                ret = "segunda";
        }

        return ret;
    }


    public String myCurrentDayofWeek(){

        //If current   day is
        //------------------------------
        // Sunday,     day = 1. (Domingo)
        // Monday,     day = 2. (Segunda-Feira)
        // Tuesday,    day = 3. (Terça-Feira)
        // Wednesday,  day = 4. (Quarta-Feira)
        // Thursday,   day = 5. (Quinta-Feira)
        // Friday,     day = 6. (Sexta-Feira)
        // Saturday,   day = 7. (Sábado)

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String ret;

        switch(day) {
            case 1:
                ret = "Domingo";
                break;

            case 2:
                ret = "Segunda";
                break;

            case 3:
                ret = "Terça";
                break;

            case 4:
                ret = "Quarta";
                break;

            case 5:
                ret = "Quinta";
                break;

            case 6:
                ret = "Sexta";
                break;

            case 7:
                ret = "Sábado";
                break;

            default:
                ret = "Segunda";
        }

        return ret;
    }


    public int DayOfweek(String dayweek){

        int ret;

        switch(dayweek) {
            case "Domingo":
                ret = 1;
                break;

            case "Segunda-feira":
                ret = 2;
                break;

            case "Terça-feira":
                ret = 3;
                break;

            case "Quarta-feira":
                ret = 4;
                break;

            case "Quinta-feira":
                ret = 5;
                break;

            case "Sexta-feira":
                ret = 6;
                break;

            case "Sábado":
                ret = 7;
                break;

            default:
                ret = 1;
        }

        return ret;
    }


    public String dayOfid(int id_day){

        String ret;

        switch(id_day) {
            case 1:
                ret = "Domingo";
                break;

            case 2:
                ret = "Segunda-feira";
                break;

            case 3:
                ret = "Terça-feira";
                break;

            case 4:
                ret = "Quarta-feira";
                break;

            case 5:
                ret = "Quinta-feira";
                break;

            case 6:
                ret = "Sexta-feira";
                break;

            case 7:
                ret = "Sábado";
                break;

            default:
                ret = "Domingo";
        }

        return ret;
    }



    public void myDays(){
        String mydays = new String("Casa");

        for(int days = 0; days < 10; days++){
            mydays = "a" + "," + mydays;
        }

        String str = "...";
        List<String> elephantList = Arrays.asList(mydays.split(","));
    }


    /*
        recebe horarios converte pra array, tira a ultima virgula e volta pra string que sera gravado no DB
    */
    public String SplitComa(String mydays){
        List<String> hourList = Arrays.asList(mydays.split(","));
        String[] array = hourList.toArray(new String[hourList.size()]);
        return Arrays.toString(array);

    }

}
