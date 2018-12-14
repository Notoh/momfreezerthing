package io.notoh.mom;

import javax.swing.*;
import java.io.IOException;

public class MomGui {
    private JButton checkExpiry;
    @SuppressWarnings("unused")
    private JLabel selectOption;
    private JTextField insertItem;
    private JButton addItemButton;
    private JTextField expiryDate;
    private JPanel LabelText;
    private JButton removeItemButton;
    private JButton listInventoryButton;
    private JButton searchButton;
    private JTextField categoryField;
    private JButton aboutHelpButton;
    private static JFrame frame;
    
    static JFrame getFrame() {
        return frame;
    }
    
    private MomGui() {
        Backend instance = Backend.getInstance();
        checkExpiry.addActionListener(e -> instance.checkExpiry(categoryField.getText()));
        addItemButton.addActionListener(e -> instance.addExpiry(insertItem.getText(), expiryDate.getText(), categoryField.getText() != null ? categoryField.getText() : ""));
        removeItemButton.addActionListener(e -> instance.remove(insertItem.getText(), categoryField.getText() != null ? categoryField.getText() : ""));
        listInventoryButton.addActionListener(e -> instance.list(categoryField.getText() != null ? categoryField.getText() : ""));
        searchButton.addActionListener(e -> instance.search(insertItem.getText(), categoryField.getText() != null ? categoryField.getText() : ""));
        aboutHelpButton.addActionListener(e -> {
            JEditorPane editorPane = new JEditorPane("text/html", "<html><body>This software was made by Alex " +
                    "Pawelko for the best mom ever, source code is viewable <a href=\"https://github" +
                    ".com/notoh/momfreezerthing/\">here</a>. <br> To use, enter an item text in the item field, any possible tags in the tags field. " +
                    "The tags must be in the format TAG1, TAG2, TAGETC, with a comma and space separating them.</body></html>");
            editorPane.setEditable(false);
            JOptionPane.showMessageDialog(null, editorPane);
        });
        Runtime.getRuntime().addShutdownHook(new Thread(instance::writeAll));
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
