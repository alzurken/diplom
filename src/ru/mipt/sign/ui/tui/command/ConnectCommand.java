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
    public void run(ApplicationContext appCtx, String command, PrintStream out) throws NeuronException
    {
        String[] temp = command.split(" ");
        if (temp.length < 4)
        {
            out.println("Wrong number of parameters");
        }
        BigInteger id1 = new BigInteger(temp[1]);
        BigInteger id2 = null;
        if (temp[2].equalsIgnoreCase("last"))
        {
            Integer fiber = Integer.valueOf(temp[3]);
            for (int i = 0; i < 4; i++)
            {
                id2 = NeuronConst.LAST_NEURON_ID.add(BigInteger.valueOf(i));
                appCtx = ConfigurationManager.connect(id1, id2, fiber);
                out.println("Connected successfully");
            }

        } else
        {
            id2 = new BigInteger(temp[2]);
            Integer fiber = Integer.valueOf(temp[3]);
            appCtx = ConfigurationManager.connect(id1, id2, fiber);
            out.println("Connected successfully");
        }
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("connect", "conn"));
    }

}
