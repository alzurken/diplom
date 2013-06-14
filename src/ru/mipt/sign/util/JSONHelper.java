package ru.mipt.sign.util;

import java.util.*;
import java.util.Map.Entry;

import ru.mipt.sign.core.JSONable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JSONHelper
{
    public static <Key, Type extends JSONable> JsonArray getJSONList(Map<Key, Type> input)
    {
        JsonArray result = new JsonArray();
        for (JSONable obj : input.values())
        {
            result.add(obj.getJSON());
        }
        return result;
    }

    public static <Type extends JSONable> JsonArray getJSONList(Set<Type> input)
    {
        JsonArray result = new JsonArray();
        for (JSONable obj : input)
        {
            result.add(obj.getJSON());
        }
        return result;
    }

    public static <Key extends JSONable, Type extends Number> JsonArray getJSONArray(Map<Key, Type> input)
    {
        JsonArray result = new JsonArray();
        for (Entry<Key, Type> entry : input.entrySet())
        {
            JsonObject object = new JsonObject();
            object.add("key", entry.getKey().getJSON());
            object.addProperty("value", entry.getValue());
            result.add(object);
        }
        return result;
    }
}
