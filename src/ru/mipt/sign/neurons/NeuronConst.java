package ru.mipt.sign.neurons;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class NeuronConst
{
	public static final Integer STATE_INIT = 0;
	public static final Integer STATE_READY = 1;
	public static final Integer STATE_CALCULATED = 2;
	public static final Integer STATE_OUTPUT = 3;
	public static final String DEFAULT_CONF_PATH = "./conf/";
	public static final Integer LEARNING_TYPE_1 = 1;
	public static final Integer LEARNING_TYPE_2 = 2;
	public static final ArrayList<String> COMMAND_EXIT = new ArrayList<String>(Arrays.asList("exit", "back", "ex"));
	public static final ArrayList<String> COMMAND_CONFIG = new ArrayList<String>(Arrays.asList("config", "conf"));
	public static final ArrayList<String> COMMAND_HI = new ArrayList<String>(Arrays.asList("hi"));
	public static final ArrayList<String> COMMAND_ADD = new ArrayList<String>(Arrays.asList("add"));
	public static final ArrayList<String> COMMAND_CONNECT = new ArrayList<String>(Arrays.asList("connect", "conn"));
	public static final ArrayList<String> COMMAND_REMOVE = new ArrayList<String>(Arrays.asList("remove", "rem", "delete", "del"));
	public static final ArrayList<String> COMMAND_START = new ArrayList<String>(Arrays.asList("start", "go"));
	public static final ArrayList<String> PARAM_NEW = new ArrayList<String>(Arrays.asList("new"));
	public static final ArrayList<String> PARAM_DEFAULT = new ArrayList<String>(Arrays.asList("default", "def"));
	public static final ArrayList<String> COMMAND_CREATE = new ArrayList<String>(Arrays.asList("create"));
	public static final ArrayList<String> COMMAND_RANDOMIZE = new ArrayList<String>(Arrays.asList("randomize"));
	public static final ArrayList<String> COMMAND_LOAD = new ArrayList<String>(Arrays.asList("load"));
	public static final ArrayList<String> COMMAND_SAVE = new ArrayList<String>(Arrays.asList("save"));
	public static final ArrayList<String> COMMAND_SHOW = new ArrayList<String>(Arrays.asList("show"));
	public static final ArrayList<String> COMMAND_HELP = new ArrayList<String>(Arrays.asList("help", "?"));
	public static final Integer DEFAULT_FIBER_NUMBER = 4;
	public static final BigInteger LAST_NEURON_ID = new BigInteger("1000000000");
}
