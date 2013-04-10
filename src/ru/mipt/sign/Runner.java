package ru.mipt.sign;

import ru.mipt.sign.ui.tui.Console;

public class Runner
{

    public static void main(String[] args)
    {
        ApplicationContext appCtx = ConfigurationManager.init();
        Console.start(appCtx);
        ConfigurationManager.exit();
        // SwingUtilities.invokeLater(new Runnable(){
        //
        // @Override
        // public void run()
        // {
        // MainWindow mw = new MainWindow();
        // mw.display();
        // }
        //
        // });

    }

}
