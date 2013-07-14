package ru.mipt.sign.facade;

import java.util.Arrays;
import java.util.List;

import ru.mipt.sign.neurons.NeuronConst;

public class DifferenceFinder implements NeuronConst
{
    private int[] diff;
    
    public Integer getDifference(List<Double> actual, List<Double> right)
    {
        if ((diff == null) || (diff.length != actual.size()))
        {
            diff = new int[actual.size()];
            Arrays.fill(diff, 1);
        }
        int result = 1;
        for (int i = 0; i < actual.size(); i++)
        {
            Integer difference = getDifference(actual.get(i), right.get(i));
            if (diff[i] != difference)
                result = -1;
            diff[i] = difference;
        }
        return result;
    }
    
    private Integer getDifference(double actual, double right)
    {
        double value = right - actual;
        if (value < ACCURACY)
            return 0;
        if (value > 0)
            return 1;
        else
            return -1;
    }
}
