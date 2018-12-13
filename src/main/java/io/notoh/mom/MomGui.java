package io.notoh.mom;

import javax.swing.*;
import java.io.IOException;

public class MomGui {
    private JButton checkExpiry;
    private JLabel selectOption;
    private JTextField insertItem;
    private JButton addItemButton;
    private JTextField expiryDate;
    private JPanel LabelText;
    private JButton removeItemButton;
    private JTextField itemToRemoveTextField;
    private JButton listInventoryButton;
    private static JFrame frame;
    
    static JFrame getFrame() {
        return frame;
    }
    
    private MomGui() throws IOException {
        checkExpiry.addActionListener(e -> Backend.getInstance().checkExpiry());
        addItemButton.addActionListener(e -> Backend.getInstance().addExpiry(insertItem.getText(), expiryDate.getText()));
        removeItemButton.addActionListener(e -> Backend.getInstance().remove(itemToRemoveTextField.getText()));
        listInventoryButton.addActionListener(e -> Backend.getInstance().list());
    }
    
    public static void main(String[] args) throws IOException {
        new Backend();
        MomGui gui = new MomGui();
        frame = new JFrame("Freezer Management");
        frame.setContentPane(gui.LabelText);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    
    
}
