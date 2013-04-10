package ru.mipt.sign.ui;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ru.mipt.sign.ApplicationContext;
import ru.mipt.sign.ConfigurationManager;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.core.exceptions.NextCommandException;
import ru.mipt.sign.neurons.NeuronConst;

public class Console
{
	private static String command = "";

	public static void start(ApplicationContext app)
	{
		byte[] input = new byte[255];
		ApplicationContext appCtx = app;
		boolean FLAG_CONFIG = false;
		String comm;
		for (;;)
		{
			try
			{
				System.in.read(input);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			comm = new String(input);
			comm = comm.substring(0, comm.indexOf("\r\n"));
			command = comm;
			// Parse command
			if (isCommand(NeuronConst.COMMAND_EXIT))
			{
				if (FLAG_CONFIG)
				{
					FLAG_CONFIG = false;
				} else
				{
					break;
				}
			}

			if (isCommand(NeuronConst.COMMAND_HI))
			{
				System.out.println("Ye, ye, i'm working, everything ok");
			}
			if (isCommand(NeuronConst.COMMAND_HELP))
			{
				StringBuilder sb = new StringBuilder();
				sb.append("<== HELP ==>\n");
				sb.append("Type <start> for start net calculation\n");
				sb.append("Type <config> for configuring net structure\n");
				sb.append("Type <save>/<load> for saving/loading net configuration\n");
				System.out.println(sb.toString());
			}
			try
			{
				if (isCommand(NeuronConst.COMMAND_START))
				{
					appCtx.getManager().start(appCtx);
				}

				if (isCommand(NeuronConst.COMMAND_CONFIG))
				{
					FLAG_CONFIG = true;
					System.out.println("Configuration: ");
				}

				if (isCommand(NeuronConst.COMMAND_SAVE))
				{
					String[] param = comm.split(" ");
					if (param.length > 1)
					{
						if ((param[1] != null) && (!param[1].isEmpty()))
						{
							ConfigurationManager.saveConfiguration(param[1]);
							System.out.println("Configuration saved to /config/" + param[1] + ".xml");
						} else
						{
							System.out.println("Bad name for configuration");
						}
					} else
					{
						DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd - HH.mm.ss");
						Calendar cal = Calendar.getInstance();
						String name = "Net - " + dateFormat.format(cal.getTime());
						ConfigurationManager.saveConfiguration(name);
						System.out.println("Configuration saved to /config/" + name + ".xml");
					}
				}

				if (isCommand(NeuronConst.COMMAND_LOAD))
				{
					String[] param = comm.split(" ");
					String name = "";
					try
					{
						if (param.length > 1)
						{
							name = param[1];
							if ((name != null) && (!name.isEmpty()))
							{
								appCtx = ConfigurationManager.init(name);
								System.out.println("Configuration loaded from " + param[1] + ".xml file");
							} else
							{
								name = "configuration";
								appCtx = ConfigurationManager.init("");
								System.out.println("NeuroNet loaded from default configuration");
							}
						}
					} catch (NextCommandException e)
					{
						System.out.println("It's impossible to load file " + name + ".xml");
					}

				}

				if (FLAG_CONFIG)
				{

					if (isCommand(NeuronConst.COMMAND_RANDOMIZE))
					{
						appCtx = ConfigurationManager.randomize();
						System.out.println("All weights randomized");
					}

					if (isCommand(NeuronConst.COMMAND_SHOW))
					{
						System.out.println("Not supported yet");
					}

					if (isCommand(NeuronConst.COMMAND_CREATE))
					{
						if (isParam(NeuronConst.PARAM_NEW))
						{
							appCtx = ConfigurationManager.init();
							System.out.println("New net created. Input id = 1, Output id = " + NeuronConst.LAST_NEURON_ID);
							// continue;
						}
					}
					if (isCommand(NeuronConst.COMMAND_CONNECT))
					{
						String[] temp = comm.split(" ");
						if (temp.length < 4)
						{
							System.out.println("Wrong number of parameters");
							continue;
						}
						BigInteger id1 = new BigInteger(temp[1]);
						BigInteger id2 = null;
						if (temp[2].equalsIgnoreCase("last"))
						{
							id2 = NeuronConst.LAST_NEURON_ID;
						} else
						{
							id2 = new BigInteger(temp[2]);
						}
						Integer fiber = Integer.valueOf(temp[3]);
						appCtx = ConfigurationManager.connect(id1, id2, fiber);
						System.out.println("Connected successfully");
					}
					if (isCommand(NeuronConst.COMMAND_ADD))
					{
						appCtx = ConfigurationManager.addNeuron();
						System.out.println("Neuron created, id = " + appCtx.getLast_added_id());
					}
					if (isCommand(NeuronConst.COMMAND_REMOVE))
					{
						String[] temp = comm.split(" ");
						BigInteger id = new BigInteger(temp[1]);

						appCtx = ConfigurationManager.removeNeuron(id);
						System.out.println("Neuron successfully removed, id = " + appCtx.getLast_removed_id());

					}
				}
				if (FLAG_CONFIG)
				{
					System.out.println("<= config =>");
				}
			} catch (NeuronNotFound e)
			{
				System.out.println("There is no neuron with id = " + e.getId());
			}
		}
	}

	private static boolean isCommand(List<String> template)
	{
		boolean flag = false;
		for (String temp : template)
		{
			if (command.startsWith(temp))
			{
				flag = true;
			}
		}
		return flag;
	}

	private static boolean isParam(List<String> template)
	{
		boolean flag = false;
		for (String temp : template)
		{
			flag = command.contains(temp) ? true : false;
		}
		return flag;

	}

}
