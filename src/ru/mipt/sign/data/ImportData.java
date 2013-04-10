package ru.mipt.sign.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mipt.sign.forex.Bar;

public abstract class ImportData
{
    protected List<Bar> data = new ArrayList<Bar>();
    protected Map<String, List<Bar>> cache = new HashMap<String, List<Bar>>();
    // String example "03 Oct 2008 05:52:30 - 1.0000, 1.0000, 1.0000, 1.0000"
    // "DATA - OPEN, CLOSE, LOW, HIGH"
    protected final String DATA_PATTERN = "yyyy.mm.dd-HH:mm";

    public void clear()
    {
        data = null;
        data = new ArrayList<Bar>();
    }

    public List<Bar> getData(String path)
    {
        if (cache.containsKey(path))
        {
            return cache.get(path);
        }
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;
            while ((str = in.readLine()) != null)
            {
                process(str);
            }
            in.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    // String example "03 Oct 2008 05:52:30 - 1.0000, 1.0000, 1.0000, 1.0000"
    // "DATA - OPEN, CLOSE, LOW, HIGH"
    protected abstract void process(String str);
}
