package io.notoh.mom;

import io.notoh.mom.gui.MomGui;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.io.*;
import java.time.LocalDateTime;

public final class Main {
    
    private static Main instance;
    private BufferedReader reader;
    private BufferedWriter writer;
    private File exp;
    
    public static void main(String[] args) {
        try {
            new Main().init();
        } catch(Exception e) {
            File file = new File("error_" + LocalDateTime.now());
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append(e.getMessage());
                writer.close();
            } catch(IOException ignored) {}
            JOptionPane optionPane = new JOptionPane("An error occured. Error stacktrace written to " + file.getName(), JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Error!");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
        }
        new MomGui();
    }
    
    private Main() {
        instance = this;
    }
    
    public static Main getInstance() {
        return instance;
    }
    
    private void init() throws IOException {
        exp = new File("./exp.txt");
        if(!exp.exists()) {
            exp.createNewFile();
        }
        reader = new BufferedReader(new FileReader(exp));
        writer = new BufferedWriter(new FileWriter(exp));
        checkExpiry();
    }
    
    public void addExpiry(String name, String date) {
    }
    
    public void checkExpiry() {
        if(exp.length() < 10)
            return;
        String toReAdd;
        String expired;
    }
    
}
