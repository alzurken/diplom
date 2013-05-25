package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.core.exceptions.NeuronException;

public class HelpCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<== HELP ==>\n");
        sb.append("Type <start> for start net calculation\n");
        sb.append("Type <config> for configuring net structure\n");
        sb.append("Type <save>/<load> for saving/loading net configuration\n");
        out.println(sb.toString());
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("help", "?"));
    }

}
