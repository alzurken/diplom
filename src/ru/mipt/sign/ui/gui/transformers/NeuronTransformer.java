package ru.mipt.sign.ui.gui.transformers;

import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import ru.mipt.sign.neurons.NeuronConst;
import ru.mipt.sign.ui.gui.GNeuron;
import ru.mipt.sign.ui.gui.GUIConst;

public class NeuronTransformer implements Transformer<GNeuron, Paint>, NeuronConst
{

    @Override
    public Paint transform(GNeuron gNeuron)
    {
        Integer role = gNeuron.getRole();
        if (role == INPUT_ROLE)
        {
            return GUIConst.INPUT_NEURON_COLOR;
        }
        if (role == NORMAL_ROLE)
        {
            return GUIConst.NORMAL_NEURON_COLOR;
        }
        if (role == OUTPUT_ROLE)
        {
            return GUIConst.OUTPUT_NEURON_COLOR;
        }
        return GUIConst.DEFAULT_COLOR;
    }

}
