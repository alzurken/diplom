package ru.mipt.sign.neurons.inner;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;

import ru.mipt.sign.neurons.Neuron;
import ru.mipt.sign.neurons.NeuronConst;

public class DefaultCalculator implements NeuroNetCalculator
{

    public void calc(HashMap<BigInteger, Neuron> neuroPool)
    {
        HashMap<BigInteger, Neuron> cache = new HashMap<BigInteger, Neuron>();
        for (BigInteger k : neuroPool.keySet())
        {
            Neuron so = neuroPool.get(k);
            Neuron n = (Neuron) so;
            if (n.getState() == NeuronConst.STATE_READY)
            {
                n.calc();
                n.emit();
            }
            if (n.getState() == NeuronConst.STATE_INIT)
            {
                cache.put(n.getID(), n);
            }
        }
        while (!cache.isEmpty())
        {
            HashMap<BigInteger, Neuron> bufferCache = new HashMap<BigInteger, Neuron>();
            Iterator<Neuron> it = cache.values().iterator();
            while (it.hasNext())
            {
                Neuron n = it.next();
                if (n.getState() == NeuronConst.STATE_READY)
                {
                    n.calc();
                    n.emit();
                }
                else
                {
                    bufferCache.put(n.getID(), n);
                }
            }
            cache = bufferCache;
        }
    }

}
