package ru.mipt.sign.learn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.mipt.sign.connect.Connection;
import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.NeuroNet;
import ru.mipt.sign.neurons.Neuron;
import ru.mipt.sign.neurons.NeuronConst;
import ru.mipt.sign.neurons.functions.ActivationFunction;

public class WeightAction extends LearningAction
{
    private Double accuracy = 1e-6;
    private Double UP_EFFORT_MAX = 1.5;
    private Double UP_EFFORT_MID = 1.2;
    private Double UP_EFFORT_MIN = 1.05;
    private Double DOWN_EFFORT_MAX = 0.3;
    private Double DOWN_EFFORT_MID = 0.5;
    private Double DOWN_EFFORT_MIN = 0.95;

    public WeightAction(NeuroNet nn, List result, List rightValue)
    {
        super(nn, result, rightValue);
    }

    // TODO Rewrite to back propagation algorithm
    @Override
    public void perform() throws NeuronNotFound
    {
        List<Neuron> nextLayer = new ArrayList<Neuron>();
        List<Double> values = result;
        List<Double> right = rightValue;
        for (int i = 0; i < 4; i++)
        {
            Neuron n = nn.getNeuron(NeuronConst.LAST_NEURON_ID.add(BigInteger.valueOf(i)));
            Double effort = getEffort(values.get(i), right.get(i));
            // n.changeAction(0, effort);
            nextLayer.add(n);
        }
        while (!nextLayer.isEmpty())
        {
            Iterator<Neuron> nIt = nextLayer.iterator();
            List<Neuron> temp = new ArrayList<Neuron>();
            while (nIt.hasNext())
            {
                Neuron cur = nIt.next();
                List<Connection> conns = nn.getConnectionsForZNeuron(cur.getID());
                Iterator<Connection> it = conns.iterator();
                while (it.hasNext())
                {
                    Connection con = it.next();
                    Neuron a = con.getANeuron();
                    Neuron z = con.getZNeuron();
                    // a.changeAction(con.getOutputNumbers(), z.getLearningEffor());
                    temp.add(a);
                }
            }
            nextLayer = temp;
        }
        this.message = "Weight action";
    }

    private Double getEffort(Double value, Double right)
    {
        Double alpha = Math.abs(right / value);
        if (alpha > UP_EFFORT_MAX)
            return Math.signum(alpha) * UP_EFFORT_MAX;
        if (alpha > UP_EFFORT_MID)
            return Math.signum(alpha) * UP_EFFORT_MID;
        if (alpha > UP_EFFORT_MIN)
            return Math.signum(alpha) * UP_EFFORT_MIN;
        if (alpha > DOWN_EFFORT_MIN)
            return Math.signum(alpha) * DOWN_EFFORT_MIN;
        if (alpha > DOWN_EFFORT_MID)
            return Math.signum(alpha) * DOWN_EFFORT_MID;
        if (alpha > DOWN_EFFORT_MAX)
            return Math.signum(alpha) * DOWN_EFFORT_MAX;
        return Math.signum(alpha) * DOWN_EFFORT_MAX * DOWN_EFFORT_MAX;
    }

    private List<Double> getInverse(ActivationFunction func, List<Double> values)
    {
        List<Double> result = new ArrayList<Double>();
        for (Integer i = 0; i < values.size(); i++)
        {
            result.add(func.getInverseValue(values.get(i)));
        }
        return result;
    }

    private boolean equals(Double x1, Double x2)
    {
        return Math.abs(x1 - x2) < accuracy;
    }
}
