package ru.mipt.sign.util;

public class Log
{
    private static boolean enabled = false;

    public static void debug(String s)
    {
        if (enabled)
            System.out.println(s);
    }
}
