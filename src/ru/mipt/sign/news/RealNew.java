package ru.mipt.sign.news;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections15.map.HashedMap;

public class RealNew
{
    private Map<String, Double> vector = new HashedMap<String, Double>();
    private Date date;
    private static final DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
    
    public RealNew(Map<String, Double> vector, String string)
    {
        this.vector = vector;
        Date date;
        try
        {
            date = format.parse(string);
            this.date = date;
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public Map<String, Double> getVector()
    {
        return vector;
    }

    public Date getDate()
    {
        return date;
    }
}
