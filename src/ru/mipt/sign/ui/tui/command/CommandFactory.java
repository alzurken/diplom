package ru.mipt.sign.ui.tui.command;

import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;

public enum CommandFactory
{
    INSTANCE;

    private Set<Command> commands = new HashSet<Command>();

    private CommandFactory()
    {
        Reflections reflection = new Reflections("ru.mipt.sign.ui.tui.command");
        Set<Class<? extends Command>> commandsClasses = reflection.getSubTypesOf(Command.class);
        for (Class<? extends Command> clazz : commandsClasses)
        {
            try
            {
                commands.add(clazz.newInstance());
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Command getCommand(String commandName)
    {
        for (Command c : commands)
        {
            if (c.check(commandName))
                return c;
        }
        return null;
    }
}
