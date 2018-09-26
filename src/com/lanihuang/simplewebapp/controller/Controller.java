package com.lanihuang.simplewebapp.controller;

import com.lanihuang.simplewebapp.entity.Entity;
import com.lanihuang.simplewebapp.scrapper.Scrapper;
import com.lanihuang.simplewebapp.spreadSheet.SpreadSheetApi;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletContext;

public class Controller
{
    //The lines in the "Merrill - WPS" and "Wausau - WPS Sections
    private Scrapper scrapper = new Scrapper();
    private SpreadSheetApi spreadSheetApi =  new SpreadSheetApi();

    public void scrapWrite(ServletContext  SC,String AuthCode[]) throws Exception 
    {
        long currentTime = System.currentTimeMillis();
        
        if(spreadSheetApi.getSpreadSheetSize(SC,AuthCode)>0)
        {
            scrapAndUpdate(convertLongToDate(currentTime),SC,AuthCode);
        }
        else
        {
            long day = 24 *60 *60 *1000;
            long beginTime = currentTime - 15*day;
            for(long i=beginTime;i<currentTime;i+=day)
            {
                scrapAndUpdate(convertLongToDate(i),SC,AuthCode);
            }
        }
    }
    
    private void scrapAndUpdate(String date,ServletContext  SC,String AuthCode[]) throws Exception
    {
        List<Entity> entityList = scrapper.scrapTrainingData(date) ;
        spreadSheetApi.writeEntities(entityList,SC,AuthCode);
    }
    
    private  long getDateNumber(String dateText, String format)
    {
        if (format == null) 
        {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try 
        {
            Date date = dateFormat.parse(dateText);
            return date.getTime();
        } 
        catch (ParseException e) 
        {
            e.printStackTrace();
        }
        return 0;
    }
    private  String convertLongToDate(long time) 
    {
        //Date date = new Date(time * 1000);
        // long time = Long.parseLong(time_);
        Date date = new Date(time);
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
