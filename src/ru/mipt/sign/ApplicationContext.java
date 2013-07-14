package ru.mipt.sign;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Timer;

import ru.mipt.sign.facade.NeuroManager;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.news.NewsContainer;
import ru.mipt.sign.ui.gui.MainWindow;

public class ApplicationContext
{
    private BigInteger last_added_id;
    private BigInteger last_removed_id;
    private NeuroNet net;
    private NeuroManager manager;
    private PrintStream out;
    private static ApplicationContext instance = new ApplicationContext();
    private MainWindow mainWindow;
    private Timer timer = new Timer();
    private NewsContainer newsContainer;
    
    public NewsContainer getNewsContainer()
    {
        return newsContainer;
    }

    public void setNewsContainer(NewsContainer newsContainer)
    {
        this.newsContainer = newsContainer;
    }

    public static ApplicationContext getInstance()
    {
        return instance;
    }
    
    public Timer getTimer()
    {
        return timer;
    }

    private ApplicationContext()
    {
//        mainWindow = new MainWindow();
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

    public void setNet(NeuroNet net)
    {
        this.net = net;
    }

    public NeuroNet getNeuroNet()
    {
        return net;
    }

}
