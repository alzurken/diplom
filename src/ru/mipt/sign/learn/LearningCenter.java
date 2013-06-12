package ru.mipt.sign.learn;

import java.util.List;
import java.util.Random;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;

public class LearningCenter
{
    private Random random = new Random(System.currentTimeMillis());
    private Integer learnType;
    public static final int TYPE_CREATE = 0;
    public static final int TYPE_WEIGHT = 1;
    public static final int TYPE_CONNECT = 2;
    public static final int TYPE_DISCONNECT = 3;
    public static final int TYPE_ALL = 4;

    public LearningCenter()
    {
        this(TYPE_ALL);
    }

    public LearningCenter(int type)
    {
        this.learnType = type;
    }

    public void learn(NeuroNet nn, List result, List rightValue, Double eta) throws NeuronNotFound
    {
        LearningAction action = null;
        Integer type;
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
            action = new WeightAction(nn, result, rightValue);
            ((WeightAction) action).setEta(eta);
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
