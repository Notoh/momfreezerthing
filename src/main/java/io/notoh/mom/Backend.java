package io.notoh.mom;

import javax.swing.JOptionPane;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Backend {
    
    private static Backend instance;
    private BufferedReader reader;
    private BufferedWriter writer;
    private File exp;
    private String tempBuffer;
    private MomGui gui;
    
    Backend() {
        instance = this;
        try {
            init();
        } catch(Exception e) {
            File file = new File("error_" + LocalDateTime.now());
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append(e.getMessage());
                writer.close();
            } catch(IOException ignored) {}
            JOptionPane.showMessageDialog(MomGui.getFrame(), "An unexpected error occurred, stacktrace written to "
                    + file + ".", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    static Backend getInstance() {
        return instance;
    }
    
    private void init() throws IOException {
        exp = new File("./exp.txt");
        if(!exp.exists()) {
            exp.createNewFile();
        }
        reader = new BufferedReader(new FileReader(exp));
        writer = new BufferedWriter(new FileWriter(exp));
    }
    
    public void addExpiry(String name) {
        boolean isValidDate = tempBuffer.chars().reduce(0,(a, c) -> a + (c=='/'?1:0)) == 2;
        if(!isValidDate) {
            JOptionPane.showMessageDialog(MomGui.getFrame(), "Error! The date format must be in yyyy/MM/dd!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        
    }
    
    public void checkExpiry() {
        if(exp.length() < 10) {
            JOptionPane.showMessageDialog(MomGui.getFrame(), "No data has been added so far, not possible to read.", "No data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            String toReAdd;
            String expired;
            reader = new BufferedReader(new FileReader("./exp.txt"));
            System.gc();
        } catch(IOException e) {
            File file = new File("error_" + LocalDateTime.now());
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append(e.getMessage());
                writer.close();
            } catch(IOException ignored) {}
            JOptionPane.showMessageDialog(MomGui.getFrame(), "An unexpected error occurred, stacktrace written to "
                    + file + ".", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setTempBuffer(String tempBuffer) {
        this.tempBuffer = tempBuffer;
    }
}
