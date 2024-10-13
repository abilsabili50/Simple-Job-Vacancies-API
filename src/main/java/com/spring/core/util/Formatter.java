package com.spring.core.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    public static Date stringToDate(String date){
        Date parserDate;
        try{
            parserDate = formatter.parse(date);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid date format");
        }

        return parserDate;
    }

    public static String dateToString(Date date){
        return formatter.format(date);
    }

    public static Date dateToDate(Date date){
        String todayAsString = formatter.format(date);
        try{
            return formatter.parse(todayAsString);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid date format");
        }
    }
}
