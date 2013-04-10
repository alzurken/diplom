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
    public void run(ApplicationContext appCtx, String command, PrintStream out) throws NeuronException
    {
        String[] param = command.split(" ");
        String name = "";
        try
        {
            if (param.length > 1)
            {
                name = param[1];
                if ((name != null) && (!name.isEmpty()))
                {
                    appCtx = ConfigurationManager.init(name);
                    out.println("Configuration loaded from " + param[1] + ".xml file");
                } else
                {
                    name = "configuration";
                    appCtx = ConfigurationManager.init("");
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
