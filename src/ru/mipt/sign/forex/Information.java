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
            return b1.getTimestamp().compareTo(b2.getTimestamp());
        }
    });

    public Information(String input)
    {
        String[] strings = input.split("\n\r");
        Integer count = Integer.parseInt(strings[0]);
        for (int i = 1; i < count + 1; i++)
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
