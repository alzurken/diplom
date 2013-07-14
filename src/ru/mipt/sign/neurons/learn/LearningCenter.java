package ru.mipt.sign.neurons.learn;

import java.util.List;
import java.util.Random;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.NeuronConst;

public class LearningCenter implements NeuronConst
{
    private Random random = new Random(System.currentTimeMillis());
    private Integer learnType;
    private double eta;
    public static final int TYPE_CREATE = 0;
    public static final int TYPE_WEIGHT = 1;
    public static final int TYPE_CONNECT = 2;
    public static final int TYPE_DISCONNECT = 3;
    public static final int TYPE_ALL = 4;

    public LearningCenter()
    {
        this(TYPE_ALL);
        this.eta = ETA;
    }

    public LearningCenter(int type)
    {
        this.learnType = type;
    }
    
    public void refresh()
    {
        this.eta = ETA;
    }

    public void learn(NeuroNet nn, List result, List rightValue, Double eta) throws NeuronNotFound
    {
        LearningAction action = null;
        Integer type;
        this.eta *= eta;
        if (learnType == TYPE_ALL)
        {
            Integer rnd = random.nextInt(50);
            if (rnd < 70)
            {
                type = LearningCenter.TYPE_WEIGHT;
            }
            else if (rnd < 80)
            {
                type = LearningCenter.TYPE_DISCONNECT;
            }
            else if (rnd < 90)
            {
                type = LearningCenter.TYPE_CONNECT;
            }
            else
            {
                type = LearningCenter.TYPE_CREATE;
            }
        }
        else
        {
            type = learnType;
        }
        switch (type)
        {
        case TYPE_CREATE:
            action = new CreateAction(nn, result, rightValue);
            break;
        case TYPE_WEIGHT:
            action = new WeightAction(nn, result, rightValue, this.eta);
            break;
        case TYPE_CONNECT:
            action = new ConnectAction(nn, result, rightValue);
            break;
        case TYPE_DISCONNECT:
            action = new DisconnectAction(nn, result, rightValue);
            break;
        }
        action.perform();
//        System.out.println(action.getMessage());
    }
}
