package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.ApplicationContext;

public class HiCommand extends Command
{

    @Override
    public ApplicationContext run(ApplicationContext appCtx, String command, PrintStream out)
    {
        out.println("Ye, ye, i'm working, everything ok");
        return appCtx;
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("hi"));
    }

}
