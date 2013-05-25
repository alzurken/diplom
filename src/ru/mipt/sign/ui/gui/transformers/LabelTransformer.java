package ru.mipt.sign.ui.gui.transformers;

import org.apache.commons.collections15.Transformer;

import ru.mipt.sign.ui.gui.GNeuron;

public class LabelTransformer implements Transformer<GNeuron, String>
{

    @Override
    public String transform(GNeuron object)
    {
        return object.getID().toString();
    }

}
