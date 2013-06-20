package ru.mipt.sign.neurons;

import java.math.BigInteger;

public interface NeuronConst
{
    public static final Double VERSION = 0.1;
    public static final Integer STATE_INIT = 0;
    public static final Integer STATE_READY = 1;
    public static final Integer STATE_CALCULATED = 2;
    public static final Integer INPUT_ROLE = 0;
    public static final Integer NORMAL_ROLE = 1;
    public static final Integer OUTPUT_ROLE = 2;
    public static final String DEFAULT_CONF_PATH = "./conf/";
    public static final Integer LEARNING_TYPE_1 = 1;
    public static final Integer LEARNING_TYPE_2 = 2;
    public static final Integer DEFAULT_FIBER_NUMBER = 4;
    public static final BigInteger LAST_NEURON_ID = new BigInteger("100000000000");
    public static final long DELAY = 60000l;
    public static final Double ETA = 0.04;
    public static final Integer INPUT_NUMBER = 1000;
    public static final Integer FIRST_NUMBER = 100;
    public static final Integer SECOND_NUMBER = 30;
}
