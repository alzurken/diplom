package ru.mipt.sign.neurons;

import java.math.BigInteger;

public interface NeuronConst
{
    public static final double VERSION = 0.1;
    public static final int STATE_INIT = 0;
    public static final int STATE_READY = 1;
    public static final int STATE_CALCULATED = 2;
    public static final int INPUT_ROLE = 0;
    public static final int NORMAL_ROLE = 1;
    public static final int OUTPUT_ROLE = 2;
    public static final String DEFAULT_CONF_PATH = "./conf/";
    public static final int LEARNING_TYPE_1 = 1;
    public static final int LEARNING_TYPE_2 = 2;
    public static final int DEFAULT_FIBER_NUMBER = 4;
    public static final BigInteger LAST_NEURON_ID = new BigInteger("100000000000");
    public static final long DELAY = 60000l;
    public static final Double ETA = 0.04;
    public static final int[] LAYER_NUMBER = {5, 3, 2, 3};
    public static final double ACCURACY = 1e-5;
    public static final int LEARN_WINDOW = 40;
}
