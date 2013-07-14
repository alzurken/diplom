package ru.mipt.sign.forex;

import java.util.Comparator;

import ru.mipt.sign.core.SortedList;

public class Information
{
    private SortedList<Bar> bars = new SortedList<Bar>(new Comparator<Bar>()
    {
        @Override
        public int compare(Bar b1, Bar b2)
        {
            return b1.getDate().compareTo(b2.getDate());
        }
    });

    public Information(String input)
    {
        String[] strings = input.split("\r\n");
        Integer count = Integer.parseInt(strings[0]);
        for (int i = 1; i < count; i++)
        {
            bars.add(new Bar(strings[i]));
        }
    }
    
    public SortedList<Bar> getBars()
    {
        return bars;
    }
    
    public int getCount()
    {
        return bars.size();
    }
}
