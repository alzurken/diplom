package ru.mipt.sign.ui.tui.command;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.ConfigurationManager;
import ru.mipt.sign.core.exceptions.NeuronException;

public class SaveCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        String[] param = command.split(" ");
        if (param.length > 1)
        {
            if ((param[1] != null) && (!param[1].isEmpty()))
            {
                ConfigurationManager.saveConfiguration(param[1]);
                out.println("Configuration saved to /config/" + param[1] + ".xml");
            } else
            {
                out.println("Bad name for configuration");
            }
        } else
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd - HH.mm.ss");
            Calendar cal = Calendar.getInstance();
            String name = "Net - " + dateFormat.format(cal.getTime());
            ConfigurationManager.saveConfiguration(name);
            out.println("Configuration saved to /config/" + name + ".xml");
        }
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("save"));
    }

}
