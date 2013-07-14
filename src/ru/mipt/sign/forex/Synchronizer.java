package ru.mipt.sign.forex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Synchronizer
{
    private static final String path = "C:\\Program Files (x86)\\MetaTrader - E-Global Trade & Finance Group\\experts\\files\\";
    private static final String input  = path + "1.txt";
    private static final String output = path + "2.txt";
    
    public static Information read()
    {
        File file = new File(input);
        int ch;
        StringBuffer strContent = new StringBuffer("");
        FileInputStream fin = null;
        try
        {
            fin = new FileInputStream(file);
            while ((ch = fin.read()) != -1)
                strContent.append((char) ch);
            fin.close();
            return new Information(strContent.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
//        file.delete();
        return null;
    }
    
    public static void write(String out)
    {
        File f = new File(output);
        f.setWritable(true);
        try
        {
            FileWriter fw = new FileWriter(f);
            fw.write(out);
            fw.close();
        } catch (IOException e)
        {
        }
    }
}
