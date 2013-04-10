package ru.mipt.sign;

import ru.mipt.sign.ui.Console;

public class Runner
{

	public static void main(String[] args)
	{
		ApplicationContext appCtx = ConfigurationManager.init();
		Console.start(appCtx);
		ConfigurationManager.exit();
	}

}
