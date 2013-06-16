package ru.mipt.sign.forex;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bar
{
    private double open;
    private double close;
    private double high;
    private double low;
    private Date timestamp;

    public Bar(Date date, double open, double close, double low, double high)
    {
        super();
        timestamp = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

    public Bar(String input)
    {
        String [] temp = input.split(";");
        this.timestamp = new Date(Long.parseLong(temp[0]));
        open = Double.parseDouble(temp[1]);
        close = Double.parseDouble(temp[2]);
        high = Double.parseDouble(temp[3]);
        low = Double.parseDouble(temp[4]);
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public double getOpen()
    {
        return open;
    }

    public double getClose()
    {
        return close;
    }

    public double getHigh()
    {
        return high;
    }

    public double getLow()
    {
        return low;
    }

    public double minusOpen(Bar bar)
    {
        return (this.open - bar.open);
    }

    public double minusClose(Bar bar)
    {
        return (this.close - bar.close);
    }

    public double minusLow(Bar bar)
    {
        return (this.low - bar.low);
    }

    public double minusHigh(Bar bar)
    {
        return (this.high - bar.high);
    }

    public List<Double> getList()
    {
        List<Double> bar = new ArrayList<Double>();
        bar.add(open);
        bar.add(close);
        bar.add(high);
        bar.add(low);
        return bar;
    }

    @Override
    public String toString()
    {
        DecimalFormat dform = new DecimalFormat("###.0000");
        return "Date: " + timestamp.toString() + " - O/C/H/L - " + dform.format(open) + "  " + dform.format(close)
                + "  " + dform.format(high) + "  " + dform.format(low);
    }
}
