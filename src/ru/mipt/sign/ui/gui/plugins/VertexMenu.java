package ru.mipt.sign.ui.gui.plugins;

import javax.swing.JPopupMenu;

import ru.mipt.sign.ui.gui.GNeuron;
import ru.mipt.sign.ui.gui.menu.ShowVertexMenuItem;

 public class VertexMenu extends JPopupMenu {
        public VertexMenu() {
            super("Vertex Menu");
            this.add(new ShowVertexMenuItem());
        }
    }