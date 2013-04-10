package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.ConfigurationManager;
import ru.mipt.sign.core.exceptions.NeuronException;

public class CreateCommand extends Command
{

    @Override
    public void run(ApplicationContext appCtx, String command, PrintStream out) throws NeuronException
    {
        appCtx = ConfigurationManager.init();
        out.println("New net created. Input id = 1, Output number = 4");
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("create"));
    }

}
