package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.ConfigurationManager;
import ru.mipt.sign.core.exceptions.NeuronException;
import ru.mipt.sign.core.exceptions.NextCommandException;

public class LoadCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        ApplicationContext appCtx = ApplicationContext.getInstance();
        String[] param = command.split(" ");
        String name = "";
        try
        {
            if (param.length > 1)
            {
                name = param[1];
                if ((name != null) && (!name.isEmpty()))
                {
                    ConfigurationManager.init(name);
                    out.println("Configuration loaded from " + param[1] + ".xml file");
                } else
                {
                    name = "configuration";
                    ConfigurationManager.init("");
                    out.println("NeuroNet loaded from default configuration");
                }
            }
        } catch (NextCommandException e)
        {
            out.println("It's impossible to load file " + name + ".xml");
        }
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("load"));
    }

}
