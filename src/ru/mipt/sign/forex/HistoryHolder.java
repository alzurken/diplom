package ru.mipt.sign.forex;

import java.util.*;

import ru.mipt.sign.core.SortedList;
import ru.mipt.sign.core.exceptions.NextBarException;
import ru.mipt.sign.util.comparator.DateComparator;

public class HistoryHolder
{
    private Map<Date, Bar> bars = new HashMap<Date, Bar>();
    private SortedList<Date> dates = new SortedList<Date>(new DateComparator());

    public HistoryHolder()
    {
        update();
    }

    public void update()
    {
        Information info = Synchronizer.read();
        for (Bar bar : info.getBars())
        {
            Date date = bar.getDate();
            dates.add(date);
            bars.put(date, bar);
        }
    }

    public Bar getNextBar(Bar bar)
    {

        return getNextBar(bar, 1);
    }
    
    public Bar getNextBar(Bar bar, int step)
    {

        int index = dates.indexOf(bar.getDate()) + step;
        if (index >= dates.size())
            throw new NextBarException();
        Date nextDate = dates.get(index);
        return bars.get(nextDate);
    }

    public Bar getPrevBar(Bar bar) throws NextBarException
    {

        int index = dates.indexOf(bar.getDate()) - 1;
        if (index < 0)
            throw new NextBarException();
        Date nextDate = dates.get(index);
        return bars.get(nextDate);
    }

    public Bar getFirstBar() throws NextBarException
    {
        if (dates.size() == 0)
            throw new NextBarException();
        return bars.get(dates.get(0));
    }

    public Bar get(Date date)
    {
        if (bars.keySet().contains(date))
        {
            return bars.get(date);
        }
        List<Date> temp = new ArrayList<Date>(dates);
        temp.add(date);
        Collections.sort(temp, new Comparator<Date>()
        {
            @Override
            public int compare(Date d1, Date d2)
            {
                return d1.compareTo(d2);
            }
        });
        Integer index = temp.indexOf(date);
        Date leftDate;
        Date rightDate;
        long left = -1, right = -1;
        if ((index - 1) < 0)
        {
            leftDate = null;
        }
        else
        {
            leftDate = temp.get(index - 1);
            left = Math.abs(leftDate.getTime() - date.getTime());
        }
        if ((index + 1) >= temp.size())
        {
            rightDate = null;
        }
        else
        {
            rightDate = temp.get(index + 1);
            right = Math.abs(rightDate.getTime() - date.getTime());
        }
        if (left < right)
        {
            if (leftDate == null)
                return null;
            return bars.get(leftDate);
        }
        else
        {
            if (rightDate == null)
                return null;
            return bars.get(rightDate);
        }
    }

    public void add(Bar bar)
    {
        Date date = bar.getDate();
        bars.put(date, bar);
        dates.add(date);
    }
}
