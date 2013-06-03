package ru.mipt.sign;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ru.mipt.sign.facade.NeuroManager;
import ru.mipt.sign.forex.Bar;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.ui.gui.MainWindow;

public class ApplicationContext
{
    private BigInteger current_id;
    private BigInteger last_added_id;
    private BigInteger last_removed_id;
    private NeuroNet net;
    private List<Bar> data = new ArrayList<Bar>();
    private NeuroManager manager;
    private PrintStream out;
    private static ApplicationContext instance = new ApplicationContext();
    private final MainWindow mainWindow;
    
    public static ApplicationContext getInstance()
    {
        return instance;
    }
    
    private ApplicationContext()
    {
        mainWindow = new MainWindow();
    }
    
    
    public MainWindow getMainWindow()
    {
        return mainWindow;
    }

    public PrintStream getOut()
    {
        return out;
    }

    public void setOut(PrintStream out)
    {
        this.out = out;
    }

    public BigInteger getLast_removed_id()
    {
        return last_removed_id;
    }

    public void setLast_removed_id(BigInteger lastRemovedId)
    {
        last_removed_id = lastRemovedId;
    }

    public BigInteger getLast_added_id()
    {
        return last_added_id;
    }

    public void setLast_added_id(BigInteger lastAddedId)
    {
        last_added_id = lastAddedId;
    }

    public NeuroManager getManager()
    {
        return manager;
    }

    public void setManager(NeuroManager manager)
    {
        this.manager = manager;
    }

    public void setCurrentID(BigInteger current_id)
    {
        this.current_id = current_id;
    }

    public void setNet(NeuroNet net)
    {
        this.net = net;
    }

    public NeuroNet getNeuroNet()
    {
        return net;
    }

    public List<Bar> getData()
    {
        return data;
    }

    public void setData(List<Bar> data)
    {
        this.data = data;
    }

    public BigInteger getNextId()
    {
        current_id = current_id.add(new BigInteger("1"));
        return current_id;
    }

}
