package ru.mipt.sign.ui.tui;

import java.io.IOException;

import javax.swing.SwingUtilities;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.core.exceptions.NeuronException;
import ru.mipt.sign.neurons.NeuronConst;
import ru.mipt.sign.ui.gui.MainWindow;
import ru.mipt.sign.ui.tui.command.Command;
import ru.mipt.sign.ui.tui.command.CommandFactory;
import ru.mipt.sign.ui.tui.command.Result;

public class Console
{
    
    public static void start()
    {
        byte[] input = new byte[255];
        System.out.println("Sign v" + NeuronConst.VERSION + " started");
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ApplicationContext.getInstance().getMainWindow().display();
                System.out.println("GUI started");
            }
        });
        Result result = Result.SUCCESSFUL;
        while (result != Result.EXIT)
        {

            try
            {
                System.in.read(input);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            String commandString = new String(input);
            commandString = commandString.substring(0, commandString.indexOf("\r\n"));
            commandString = commandString.trim();
            Command command = CommandFactory.INSTANCE.getCommand(commandString);

            if (command == null)
            {
                System.out.println("Wrong command");
                continue;
            }
            try
            {
                command.run(commandString);
                result = command.getResult();
                ApplicationContext.getInstance().getMainWindow().display();
            } catch (NeuronException e)
            {
                System.out.println(e.toString());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        System.out.println("Sign v" + NeuronConst.VERSION + " stopped");
    }

}
