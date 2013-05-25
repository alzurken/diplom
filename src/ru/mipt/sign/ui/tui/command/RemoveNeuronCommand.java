package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.ConfigurationManager;
import ru.mipt.sign.core.exceptions.NeuronException;

public class RemoveNeuronCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        String[] temp = command.split(" ");
        BigInteger id = new BigInteger(temp[1]);

        ConfigurationManager.removeNeuron(id);
        System.out.println("Neuron successfully removed, id = " + ApplicationContext.getInstance().getLast_removed_id());
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("remove", "rem", "delete", "del"));
    }

}
