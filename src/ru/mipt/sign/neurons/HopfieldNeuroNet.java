package ru.mipt.sign.neurons;

import java.math.BigInteger;
import java.util.*;

import ru.mipt.sign.core.exceptions.NeuronNotFound;
import ru.mipt.sign.neurons.functions.UnitFunction;
import ru.mipt.sign.neurons.inner.UnitWeights;

public class HopfieldNeuroNet extends NeuroNet
{
    private BigInteger outputID;
    private Map<BigInteger, BigInteger> neuron2out = new HashMap<BigInteger, BigInteger>();
    private Map<BigInteger, BigInteger> neuron2in = new HashMap<BigInteger, BigInteger>();

    public HopfieldNeuroNet(Integer inNumber)
    {
        outputID = LAST_NEURON_ID;
        this.inputNumber = inNumber;
        for (int i = 0; i < inputNumber; i++)
        {
            BigInteger inID = getNextId();
            Neuron inputNeuron = new Neuron(inID);
            neuroPool.put(inID, inputNeuron);
            BigInteger id = getNextId();
            neuroPool.put(id, new Neuron(id));
            BigInteger outID = getNextOutID();
            neuron2out.put(id, outID);
            neuron2in.put(id, inID);
            Neuron outputNeuron = new Neuron(outID);
            outputNeuron.setWeights(new UnitWeights(0, 1));
            outputNeuron.setFunction(new UnitFunction());
            neuroPool.put(outID, outputNeuron);
            try
            {
                setInputNeuron(inputNeuron);
                setOutputNeuron(outputNeuron, false);
                connectNeuron(inID, id, 1);
            } catch (NeuronNotFound e)
            {
                System.out.println("There is no neuron with id = " + e.getId());
            }
        }

    }

    public double getEnergy()
    {
        double energy = 0d;
        List<Double> input = getCurrentInput();
        try
        {
            List<Neuron> neurons = getNeurons(getRealNeurons());
            for (Neuron n : neurons)
            {
                double inValue = input.get(getNeuron(neuron2in.get(n.getID())).getOrder());
                double sum = 0d;
                for (Connection c : getConnectionsForZNeuron(neuron2out.get(n.getID())))
                {
                    Neuron neuron = c.getANeuron();
                    double weight = neuron.getWeight(0, c.getA2zMapping().keySet().iterator().next());
                    Neuron inputNeuron = getNeuron(neuron2in.get(neuron.getID()));
                    double inValue2 = input.get(inputNeuron.getOrder());
                    sum += weight * inValue2;
                }
                energy += sum * inValue;
            }
        } catch (NeuronNotFound e)
        {
            e.printStackTrace();
        }
        energy /= inputNumber*inputNumber;
        return energy;
    }

    @Override
    public void connectNeuron(BigInteger id1, BigInteger id2, int fiber) throws NeuronNotFound
    {
        Neuron first = getNeuron(id1);
        Neuron second = getNeuron(id2);
        if ((first.getRole() == INPUT_ROLE) || (second.getRole() == OUTPUT_ROLE))
        {
            super.connectNeuron(id1, id2, fiber);
            return;
        }
        super.connectNeuron(id1, neuron2out.get(id2), fiber);
    }

    public List<BigInteger> getRealNeurons()
    {
        return new ArrayList<BigInteger>(neuron2out.keySet());
    }

    private BigInteger getNextOutID()
    {
        outputID = outputID.add(BigInteger.valueOf(1));
        return outputID;
    }

    @Override
    public void calc()
    {
        currentInput = new ArrayList<Double>();
        for (Iterator<Neuron> it = this.inputNeurons.iterator(); it.hasNext();)
        {
            Neuron n = it.next();
            List<Double> temp = inputProvider.getNextInput();
            currentInput.addAll(temp);
            n.setInput(temp);
        }
        HashMap<BigInteger, Neuron> cache = new HashMap<BigInteger, Neuron>();
        for (BigInteger k : neuroPool.keySet())
        {
            Neuron so = neuroPool.get(k);
            Neuron n = (Neuron) so;
            if (n.getState() == STATE_READY)
            {
                n.calc();
                n.emit();
            }
            if (n.getState() == STATE_INIT)
            {
                cache.put(n.getID(), n);
            }
        }
        boolean onlyOutNeurons = false;
        while ((!cache.isEmpty()) && (!onlyOutNeurons))
        {
            onlyOutNeurons = true;
            HashMap<BigInteger, Neuron> bufferCache = new HashMap<BigInteger, Neuron>();
            Iterator<Neuron> it = cache.values().iterator();
            while (it.hasNext())
            {
                Neuron n = it.next();
                if (n.getRole() != OUTPUT_ROLE)
                {
                    onlyOutNeurons = false;
                }
                if (n.getState() == STATE_READY)
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
