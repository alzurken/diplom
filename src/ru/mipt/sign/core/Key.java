package ru.mipt.sign.core;

import org.json.simple.JSONObject;

import com.google.gson.JsonObject;

public class Key implements JSONable
{
    private Integer key1;
    private Integer key2;

    public Key(JsonObject obj)
    {
        key1 = obj.get("key1").getAsInt();
        key2 = obj.get("key2").getAsInt();
    }
    
    public Key(Integer key1, Integer key2)
    {
        this.key1 = key1;
        this.key2 = key2;
    }

    public Integer getKey1()
    {
        return key1;
    }

    public Integer getKey2()
    {
        return key2;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key1 == null) ? 0 : key1.hashCode());
        result = prime * result + ((key2 == null) ? 0 : key2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Key other = (Key) obj;
        if (key1 == null)
        {
            if (other.key1 != null)
                return false;
        }
        else if (!key1.getClass().equals(other.key1.getClass()))
            return false;
        else if (!key1.equals(other.key1))
            return false;
        if (key2 == null)
        {
            if (other.key2 != null)
                return false;
        }
        else if (!key2.getClass().equals(other.key2.getClass()))
            return false;
        else if (!key2.equals(other.key2))
            return false;
        return true;
    }

    @Override
    public JsonObject getJSON()
    {
        JsonObject result = new JsonObject();
        result.addProperty("key1", key1);
        result.addProperty("key2", key2);
        return result;
    }

}
