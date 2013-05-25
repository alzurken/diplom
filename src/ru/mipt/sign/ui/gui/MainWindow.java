package ru.mipt.sign.ui.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ru.mipt.sign.ui.gui.transformers.LabelTransformer;
import ru.mipt.sign.ui.gui.transformers.NeuronTransformer;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

public class MainWindow
{
    private JFrame window;
    private JPanel graphPanel;

    public MainWindow()
    {
        window = new JFrame("Sign");
        window.setSize(500, 500);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel jlab = new JLabel("View Net");
        window.add(jlab, BorderLayout.LINE_START);
        graphPanel = new JPanel();
        graphPanel.setPreferredSize(new Dimension(400, 400));
        graphPanel.setLayout(new BorderLayout());
        window.add(graphPanel, BorderLayout.CENTER);
        window.setVisible(true);
    }

    public void display()
    {
        GNeuronNet graph = new GNeuronNet();
        
        Dimension size = new Dimension(graphPanel.getWidth(), graphPanel.getHeight());
        
        Layout<GNeuron, GConnection> layout = new StaticLayout<GNeuron, GConnection>(graph.getGraph());
//        layout.setSize(size);
        VisualizationViewer<GNeuron, GConnection> vv = new VisualizationViewer<GNeuron, GConnection>(layout);
//        vv.setPreferredSize(size);

        DefaultModalGraphMouse<GNeuron, GConnection> mouse = new DefaultModalGraphMouse<GNeuron, GConnection>(); 
        
        mouse.setMode(ModalGraphMouse.Mode.PICKING);
        
        vv.setGraphMouse(mouse);
        
        vv.getRenderContext().setVertexFillPaintTransformer(new NeuronTransformer());
        vv.getRenderContext().setVertexLabelTransformer(new LabelTransformer());
        
        graphPanel.removeAll();
        graphPanel.add(vv, BorderLayout.CENTER);
        window.validate();
    }
}
