package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.core.exceptions.NeuronException;

public class StartCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        Long numberOfSteps;
        String[] temp = command.split(" ");
        if (temp.length == 1)
        {
            numberOfSteps = 1000l;
        }
        else
        {
            numberOfSteps = Long.valueOf(temp[1]);
        }
        ApplicationContext.getInstance().getTimer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                ApplicationContext.getInstance().getManager().start();
            }
        }, 0, DELAY);
        System.out.println("Processing started");
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("start", "go"));
    }

}
