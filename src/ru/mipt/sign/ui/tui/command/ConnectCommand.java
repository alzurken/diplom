package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.ConfigurationManager;
import ru.mipt.sign.core.exceptions.NeuronException;
import ru.mipt.sign.neurons.NeuronConst;

public class ConnectCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        ApplicationContext appCtx = ApplicationContext.getInstance();
        String[] temp = command.split(" ");
        if (temp.length < 3)
        {
            out.println("Wrong number of parameters");
            return;
        }
        BigInteger id1 = new BigInteger(temp[1]);
        BigInteger id2 = null;
        Integer fiber;
        if (temp.length == 4)
        {
            fiber = Integer.valueOf(temp[3]);
        }
        else
        {
            fiber = 1;
        }
        if (temp[2].equalsIgnoreCase("last"))
        {
            for (int i = 0; i < appCtx.getNeuroNet().getOutputNumber(); i++)
            {
                id2 = LAST_NEURON_ID.add(BigInteger.valueOf(i));
                ConfigurationManager.connect(id1, id2, fiber);
                out.println("Connected successfully: " + id1 + " -> " + id2 + " : " + fiber);
            }

        }
        else
        {
            id2 = new BigInteger(temp[2]);
            ConfigurationManager.connect(id1, id2, fiber);
            out.println("Connected successfully: " + id1 + " -> " + id2 + " : " + fiber);
        }
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("connect", "conn"));
    }

}
