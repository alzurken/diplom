package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.ApplicationContext;

public class ExitCommand extends Command
{

    @Override
    public ApplicationContext run(ApplicationContext appCtx, String command, PrintStream out)
    {
        result = Result.EXIT;
        return appCtx;
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("exit", "back", "ex"));
    }

}
