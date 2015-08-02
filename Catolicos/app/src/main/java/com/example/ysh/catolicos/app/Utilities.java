package com.example.ysh.catolicos.app;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.String.*;

/*
 * Created by YSH on 09/07/2015.
 */
public class Utilities {


    public Date myhour(String inhour){

        Date dateObj = new Date();

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

            case "Segunda-Feira":
                ret = "segunda";
                break;

            case "Terça-Feira":
                ret = "terca";
                break;

            case "Quarta-Feira":
                ret = "quarta";
                break;

            case "Quinta-Feira":
                ret = "quinta";
                break;

            case "Sexta-Feira":
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
}
