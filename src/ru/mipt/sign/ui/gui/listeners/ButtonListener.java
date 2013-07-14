package ru.mipt.sign.ui.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.mipt.sign.ApplicationContext;

public class ButtonListener implements ActionListener
{

    public void actionPerformed(ActionEvent actionevent)
    {
        ApplicationContext.getInstance().getManager().start(1l);
        ApplicationContext.getInstance().getMainWindow().display();
    }

}
