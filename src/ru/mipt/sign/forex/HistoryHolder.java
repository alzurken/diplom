package ru.mipt.sign.forex;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.mipt.sign.core.SortedList;

public class HistoryHolder
{
    private Map<Date, Bar> bars = new HashMap<Date, Bar>();
    private SortedList<Date> dates = new SortedList<Date>(new Comparator<Date>()
    {
        @Override
        public int compare(Date d1, Date d2)
        {
            return d1.compareTo(d2);
        }
    });

    public Bar get(Date date)
    {
        if (bars.keySet().contains(date))
        {
            return bars.get(date);
        }
        dates.add(date);
        Integer index = dates.indexOf(date);
        Date leftDate;
        Date rightDate;
        long left = -1, right = -1;
        if ((index - 1) < 0)
        {
            leftDate = null;
        }
        else
        {
            leftDate = dates.get(index - 1);
            left = Math.abs(leftDate.getTime() - date.getTime());
        }
        if ((index + 1) > dates.size())
        {
            rightDate = null;
        }
        else
        {
            rightDate = dates.get(index + 1);
            right = Math.abs(rightDate.getTime() - date.getTime());
        }
        if (left < right)
        {
            return bars.get(leftDate);
        }
        else
        {
            return bars.get(rightDate);
        }
    }

    public void add(Bar bar)
    {
        Date date = bar.getTimestamp();
        bars.put(date, bar);
        dates.add(date);
    }
}
