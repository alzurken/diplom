package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.ConfigurationManager;
import ru.mipt.sign.core.exceptions.NeuronException;

public class RandomizeCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        ConfigurationManager.randomize();
        out.println("All weights randomized");
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("random"));
    }

}
