package ru.mipt.sign.forex;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bar
{
    private Double open;
    private Double close;
    private Double high;
    private Double low;
    private Date timestamp;

    public Bar(Date date, Double open, Double close, Double low, Double high) {
        super();
        timestamp = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public Double getOpen()
    {
        return open;
    }

    public Double getClose()
    {
        return close;
    }

    public Double getHigh()
    {
        return high;
    }

    public Double getLow()
    {
        return low;
    }

    public Double minusOpen(Bar bar)
    {
        return (this.open - bar.open);
    }

    public Double minusClose(Bar bar)
    {
        return (this.close - bar.close);
    }

    public Double minusLow(Bar bar)
    {
        return (this.low - bar.low);
    }

    public Double minusHigh(Bar bar)
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
