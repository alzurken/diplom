package ru.mipt.sign.ui.gui;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainWindow
{
    public void display()
    {
        JFrame jfrm = new JFrame("Sign");
        jfrm.setSize(200, 200);
        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfrm.setLayout(new FlowLayout());
        jfrm.setVisible(true);
        JLabel jlab = new JLabel("for start");
        jfrm.add(jlab);

    }
}
