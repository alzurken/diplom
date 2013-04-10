package ru.mipt.sign.neurons;

import java.math.BigInteger;

public class NeuronConst
{
    public static final Double VERSION = 0.1;
    public static final Integer STATE_INIT = 0;
    public static final Integer STATE_READY = 1;
    public static final Integer STATE_CALCULATED = 2;
    public static final Integer STATE_OUTPUT = 3;
    public static final String DEFAULT_CONF_PATH = "./conf/";
    public static final Integer LEARNING_TYPE_1 = 1;
    public static final Integer LEARNING_TYPE_2 = 2;
    public static final Integer DEFAULT_FIBER_NUMBER = 4;
    public static final BigInteger LAST_NEURON_ID = new BigInteger("1000000000");
}
