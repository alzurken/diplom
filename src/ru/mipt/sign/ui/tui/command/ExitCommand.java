package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExitCommand extends Command
{

    @Override
    public void run(String command, PrintStream out)
    {
        result = Result.EXIT;
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("exit", "back", "ex"));
    }

}
