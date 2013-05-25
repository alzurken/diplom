package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import ru.mipt.sign.core.exceptions.NeuronException;

public abstract class Command
{
    private List<String> commandList = new ArrayList<String>();
    protected Result result = Result.SUCCESSFUL;

    public Command()
    {
        this.commandList = initCommandList();
    }

    public boolean check(String command)
    {
        for (String temp : commandList)
        {
            if (command.startsWith(temp))
            {
                return true;
            }
        }
        return false;
    }

    public void run( String command) throws NeuronException
    {
        run(command, System.out);
    }

    public Result getResult()
    {
        return result;
    }

    public abstract void run(String command, PrintStream out) throws NeuronException;

    public abstract List<String> initCommandList();
}
