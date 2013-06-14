package ru.mipt.sign.forex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Synchronizer
{
    private String input = "1.txt";
    private String output = "2.txt";
    
    public Synchronizer()
    {
        
    }
    
    public Information read()
    {
        File out = new File(output);
        out.delete();
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
            return null;
        }
    }
    
    public void write(String out)
    {
        File in = new File(input);
        in.delete();
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
