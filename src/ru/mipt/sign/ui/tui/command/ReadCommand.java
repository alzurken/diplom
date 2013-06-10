package ru.mipt.sign.ui.tui.command;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.core.exceptions.NeuronException;
import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.neurons.NeuronConst;

public class ReadCommand extends Command
{

    @Override
    public void run(String command, PrintStream out) throws NeuronException
    {
        String[] param = command.split(" ");
        if (param.length > 1)
        {
            if ((param[1] != null) && (!param[1].isEmpty()))
            {
                String path = NeuronConst.DEFAULT_CONF_PATH + param[1] + ".cl";
                File file = new File(path);
                try
                {
                    FileInputStream fis = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    String commandString;
                    while ((commandString = br.readLine()) != null) {
                        try
                        {
                            commandString = commandString.trim();
                            int endLine = commandString.indexOf("\r\n");
                            commandString = commandString.substring(0, endLine == -1 ? commandString.length() : endLine);

                            Command c = CommandFactory.INSTANCE.getCommand(commandString);
                            c.run(commandString);
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    br.close();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                out.println("\nCommand list runned successful");
            } else
            {
                out.println("Bad name for command list");
            }
        }
    }

    @Override
    public List<String> initCommandList()
    {
        return new ArrayList<String>(Arrays.asList("read"));
    }

}
