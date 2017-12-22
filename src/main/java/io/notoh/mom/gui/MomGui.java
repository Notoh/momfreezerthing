package io.notoh.mom.gui;

import io.notoh.mom.Main;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MomGui {
    private JButton checkExpiry;
    private JLabel selectOption;
    private JTextField insertItemToAddTextField;
    private JButton addItemButton;
    
    public MomGui() {
        checkExpiry.addActionListener(e -> Main.getInstance().checkExpiry() );
    }
}
