package ru.mipt.sign;

import ru.mipt.sign.ui.tui.Console;

public class Runner
{

    public static void main(String[] args)
    {
        ConfigurationManager.init();
        Console.start();
        ConfigurationManager.exit();
    }

}
