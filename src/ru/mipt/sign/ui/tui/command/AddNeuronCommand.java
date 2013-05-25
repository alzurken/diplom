package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.ConfigurationManager;
import ru.mipt.sign.core.exceptions.NeuronException;

public class AddNeuronCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        ConfigurationManager.addNeuron();
        out.println("Neuron created, id = " + ApplicationContext.getInstance().getLast_added_id());
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("add"));
    }

}
