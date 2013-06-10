package ru.mipt.sign.neurons.factory;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.jdom.DataConversionException;
import org.jdom.Element;

import ru.mipt.sign.neurons.Neuron;
import ru.mipt.sign.neurons.functions.ActivationFunction;
import ru.mipt.sign.neurons.inner.Weights;

public class NeuronFactory
{
    private static NeuronFactory instance = new NeuronFactory();
    
    public static NeuronFactory_Complete newNeuron(BigInteger id)
    {
        return instance.new NeuronFactory_Complete(id);
    }
    
    public static NeuronFactory_Complete newNeuron(Element el)
    {
        return instance.new NeuronFactory_Complete(el);
    }
    
    public static Element getXML(Neuron neuron)
    {
        Element image = neuron.getImage();
        if (image != null)
        {
            return image;
        }
        image = new Element("neuron");
        image.setAttribute("id", neuron.getID().toString());
        int inNumber = neuron.getInNumber();
        image.setAttribute("inNumber", Integer.toString(inNumber));
        int outNumber = neuron.getOutNumber();
        image.setAttribute("outNumber", Integer.toString(outNumber));

        Element weight = new Element("weight");
        for (int i = 0; i < inNumber; i++)
        {
            Element layer = new Element("layer");
            layer.setAttribute("number", Integer.toString(i));
            Weights weights = neuron.getWeights();
            for (int j = 0; j < outNumber; j++)
            {
                Element item = new Element("item");
                item.setAttribute("number", Integer.toString(j));
                item.setAttribute("value", weights.getWeight(i, j).toString());
                layer.addContent(item);
            }
            weight.addContent(layer);
        }
        image.addContent(weight);
        neuron.setImage(image);
        return image;
    }
    
    public class NeuronFactory_Complete
    {
        private Neuron neuron;
        
        private NeuronFactory_Complete(BigInteger id)
        {
            neuron = new Neuron(id);
        }
        
        private NeuronFactory_Complete(Element el)
        {
            BigInteger id = new BigInteger(el.getAttribute("id").getValue());
            try
            {
                int inNumer = el.getAttribute("inNumber").getIntValue();
                int outNumber = el.getAttribute("outNumber").getIntValue();

                Weights weights = new Weights(inNumer, outNumber);
                Element weight = el.getChild("weight");
                @SuppressWarnings("unchecked")
                List<Element> layers = weight.getChildren();
                for (Iterator<Element> it = layers.iterator(); it.hasNext();)
                {
                    Element layer = it.next();
                    int input = layer.getAttribute("number").getIntValue();
                    @SuppressWarnings("unchecked")
                    List<Element> items = layer.getChildren();
                    for (Iterator<Element> jt = items.iterator(); jt.hasNext();)
                    {
                        Element item = jt.next();
                        int output = item.getAttribute("number").getIntValue();
                        double value = item.getAttribute("value").getDoubleValue();
                        weights.setWeight(input, output, value);
                    }
                }
                neuron = new Neuron(id, el, weights);
            } catch (DataConversionException e)
            {
                e.printStackTrace();
            }
        }
        
        public NeuronFactory_Complete activationFunction(ActivationFunction function)
        {
            neuron.setFunction(function);
            return this;
        }
        
        public Neuron create()
        {
            return neuron;
        }
    }
    
}
