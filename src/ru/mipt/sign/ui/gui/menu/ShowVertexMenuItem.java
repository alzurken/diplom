/*
 * DeleteVertexMenuItem.java
 *
 * Created on March 21, 2007, 2:03 PM; Updated May 29, 2007
 *
 * Copyright March 21, 2007 Grotto Networking
 *
 */

package ru.mipt.sign.ui.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import ru.mipt.sign.neurons.Neuron;
import ru.mipt.sign.ui.gui.GNeuron;
import ru.mipt.sign.ui.gui.plugins.VertexMenuListener;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * A class to implement the deletion of a vertex from within a
 * PopupVertexEdgeMenuMousePlugin.
 * 
 * @author Dr. Greg M. Bernstein
 */
public class ShowVertexMenuItem extends JMenuItem implements VertexMenuListener<GNeuron>
{
    
    private GNeuron vertex;
    private VisualizationViewer visComp;

    /** Creates a new instance of DeleteVertexMenuItem */
    public ShowVertexMenuItem()
    {
        super("Show weight");
        this.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                WeightsTable table = (WeightsTable) visComp.getParent().getParent().getComponentAt(1, 1).getComponentAt(1, 1).getComponentAt(10, 20)
                        .getComponentAt(1, 1);
                Neuron neuron = vertex.getNeuron();
               
                table.updateWeights(neuron);
                
                table.validate();
                visComp.getParent().getParent().validate();
            }
        });
    }

    /**
     * Implements the VertexMenuListener interface.
     * 
     * @param v
     * @param visComp
     */
    public void setVertexAndView(GNeuron v, VisualizationViewer visComp)
    {
        this.vertex = v;
        this.visComp = visComp;
        this.setText("Show weight");
    }

}
