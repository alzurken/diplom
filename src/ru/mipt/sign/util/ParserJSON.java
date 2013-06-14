package ru.mipt.sign.util;

import java.io.File;
import java.io.FileInputStream;

import ru.mipt.sign.core.exceptions.NextCommandException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParserJSON
{
    
    public static JsonObject getJsonObject(String path) throws NextCommandException
    {
        if (!path.isEmpty())
        {
            File file = new File(path);
            int ch;
            StringBuffer strContent = new StringBuffer("");
            FileInputStream fin = null;
            try
            {
                fin = new FileInputStream(file);
                while ((ch = fin.read()) != -1)
                    strContent.append((char) ch);
                fin.close();
            } catch (Exception e)
            {
                System.out.println("File '" + path + "' not found");
                throw new NextCommandException();
            }

            JsonParser parser = new JsonParser();
            JsonObject o = (JsonObject)parser.parse(strContent.toString());
            return o;
        }
        else
        {
            System.out.println("Path for configuration is empty!");
            return null;
        }
    }
}
