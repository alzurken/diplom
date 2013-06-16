package ru.mipt.sign.neurons.inner;

import java.util.Collections;
import java.util.List;

public class UnitWeights extends Weights
{
    public UnitWeights(Integer inNumber, Integer outNumber)
    {
        super(inNumber, outNumber);
    }

    @Override
    public void addInputs(Integer extraNumber)
    {
        inNumber += extraNumber;
    }
    
    @Override
    public void addOutputs(Integer extraNumber)
    {
        outNumber += extraNumber;
    }
    
    @Override
    public void removeInputs(List<Integer> inputs)
    {
        inNumber -= inputs.size();
    }
    
    @Override
    public void removeOutputs(List<Integer> outputs)
    {
        int delta = outputs.size();
        outNumber -= delta;
    }
    
    @Override
    public void setInNumber(int inNumber)
    {
        this.inNumber = inNumber;
    }
    
    @Override
    public void setOutNumber(int outNumber)
    {
        this.outNumber = outNumber;
    }
    
    public double[][] getWeightsForCalc()
    {
        double[][] result = new double[inNumber][outNumber];
        for (int i = 0; i < inNumber; i++)
        {
            for (int j = 0; j < outNumber; j++)
            {
                result[i][j] = 1d;
            }
        }
        return result;
    }
    
    @Override
    public void randomize()
    {
        
    }
    
    public double initValue()
    {
        throw new RuntimeException();
    }
}
