package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.ConfigurationManager;
import ru.mipt.sign.core.exceptions.NeuronException;

public class CreateCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        String[] temp = command.split(" ");
        Integer inNumber = 1;
        Integer outNumber = 1;
        if (temp.length > 2)
        {
            inNumber = Integer.valueOf(temp[1]);
            outNumber = Integer.valueOf(temp[2]);
        }
        
        ConfigurationManager.init(inNumber, outNumber);
        out.println("New net created. Input id = " + inNumber + ", Output number = " + outNumber);
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("create"));
    }

}
