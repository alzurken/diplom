package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.core.exceptions.NeuronException;
import ru.mipt.sign.core.exceptions.NeuronNotFound;

public class StartCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        Long numberOfSteps;
        String[] temp = command.split(" ");
        System.out.println("Processing started");
        if (temp.length == 1)
        {
            numberOfSteps = null;
            ApplicationContext.getInstance().getManager().start();
        }
        else
        {
            numberOfSteps = Long.valueOf(temp[1]);
            ApplicationContext.getInstance().getManager().start(numberOfSteps);
        }
        System.out.println("Processing stopped");
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("start", "go"));
    }

}
