package ru.mipt.sign.ui.tui.command;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.core.exceptions.NeuronException;

public class StartCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {

        ApplicationContext.getInstance().getManager().start();

    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("start", "go"));
    }

}
