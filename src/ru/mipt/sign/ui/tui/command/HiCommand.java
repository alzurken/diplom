package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HiCommand extends Command
{

    @Override
    public void run(String command, PrintStream out)
    {
        out.println("Ye, ye, i'm working, everything ok");
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("hi"));
    }

}
