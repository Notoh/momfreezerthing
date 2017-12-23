package io.notoh.mom;

import javax.swing.JOptionPane;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@SuppressWarnings("Duplicates")
public final class Backend {
    
    private static Backend instance;
    private BufferedReader reader;
    private BufferedWriter writer;
    private File exp;
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
                writer.flush();
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
    }
    
    public void addExpiry(String name, String datetxt) {
        if(name.contains("-") || datetxt.contains("-")) {
            JOptionPane.showMessageDialog(MomGui.getFrame(), "Error! - and : are illegal characters!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean isValidDate = (datetxt.chars().reduce(0,(a, c) -> a + (c=='/'?1:0)) == 2) && (datetxt.chars()
                .reduce(0, (a,c) -> a + (c==':'?1:0)) == 0);
        if(!isValidDate) {
            JOptionPane.showMessageDialog(MomGui.getFrame(), "Error! The date format must be in yyyy/MM/dd!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate date = LocalDate.from(parser.parse(datetxt));
        UUID randomUUID = UUID.randomUUID();
        try {
            StringBuilder builder = new StringBuilder();
            reader = new BufferedReader(new FileReader("./exp.txt"));
            String ln = reader.readLine();
            while(ln != null) {
                builder.append(ln).append("\n");
                ln = reader.readLine();
            }
            writer = new BufferedWriter(new FileWriter("./exp.txt"));
            System.gc();
            writer.write(builder.toString());
            writer.write(name + "-" + randomUUID.toString() + "$" + date.toString() + "\n");
            writer.flush();
            JOptionPane.showMessageDialog(MomGui.getFrame(), "Success adding item " + name + " with UUID " + randomUUID.toString() + "!", "Success",
                                          JOptionPane.INFORMATION_MESSAGE);
        } catch(IOException e) {
            File file = new File("error_" + LocalDateTime.now());
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append(e.getMessage());
                writer.flush();
                writer.close();
            } catch(IOException ignored) {}
            JOptionPane.showMessageDialog(MomGui.getFrame(), "An unexpected error occurred, stacktrace written to "
                    + file + ".", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    void checkExpiry() {
        if(exp.length() < 10) {
            JOptionPane.showMessageDialog(MomGui.getFrame(), "No data has been added so far, not possible to read.", "No data", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            StringBuilder toReAdd = new StringBuilder();
            LocalDate now = LocalDate.now();
            reader = new BufferedReader(new FileReader("./exp.txt"));
            System.gc();
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String lineRead = reader.readLine();
            boolean nonExp = true;
            while(lineRead != null) {
                if(lineRead.length() < 32) {
                    lineRead = reader.readLine();
                    if(lineRead == null) break;
                    continue;
                }
                String name = lineRead.substring(0,lineRead.indexOf("-"));
                String uuid = lineRead.substring(lineRead.indexOf("-")+1,lineRead.indexOf("$"));
                String expiry = lineRead.substring(lineRead.indexOf("$")+1,lineRead.indexOf("$")+11);
                LocalDate parsed = LocalDate.from(parser.parse(expiry));
                if(parsed.isBefore(now)) {
                    JOptionPane.showMessageDialog(MomGui.getFrame(), name+"("+uuid+") is expired as of "+parsed.toString(), "Expiry",JOptionPane.INFORMATION_MESSAGE);
                    nonExp = false;
                } else {
                    toReAdd.append(name).append("-").append(uuid).append("$").append(expiry).append("\n");
                }
                lineRead = reader.readLine();
            }
            if(nonExp) {
                JOptionPane.showMessageDialog(MomGui.getFrame(), "No expired items!", "No expiry", JOptionPane.INFORMATION_MESSAGE);
            }
            writer = new BufferedWriter(new FileWriter("./exp.txt"));
            writer.write(toReAdd.append("\n").toString());
            writer.flush();
            writer.close();
            reader.close();
            System.gc();
        } catch(IOException e) {
            File file = new File("error_" + LocalDateTime.now());
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append(e.getMessage());
                writer.flush();
                writer.close();
            } catch(IOException ignored) {}
            JOptionPane.showMessageDialog(MomGui.getFrame(), "An unexpected error occurred, stacktrace written to "
                    + file + ".", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    void remove(String name) {
        try {
            StringBuilder toReAdd = new StringBuilder();
            reader = new BufferedReader(new FileReader("./exp.txt"));
            System.gc();
            String lineRead = reader.readLine();
            boolean found = false;
            while(lineRead != null) {
                if(!lineRead.contains("-")) {
                    lineRead = reader.readLine();
                    continue;
                }
                if(lineRead.substring(0,lineRead.indexOf("-")).equalsIgnoreCase(name) && !found) {
                    found = true;
                    lineRead = reader.readLine();
                    continue;
                }
                toReAdd.append(lineRead).append("\n");
                lineRead = reader.readLine();
            }
            writer = new BufferedWriter(new FileWriter("./exp.txt"));
            writer.write(toReAdd.append("\n").toString());
            writer.flush();
            writer.close();
            reader.close();
            if(found) {
                JOptionPane.showMessageDialog(MomGui.getFrame(), "Successful removal of " + name + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(MomGui.getFrame(), "That item could not be found!", "Not found", JOptionPane.INFORMATION_MESSAGE);
            }
            System.gc();
        } catch(IOException e) {
            File file = new File("error_" + LocalDateTime.now());
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append(e.getMessage());
                writer.flush();
                writer.close();
            } catch(IOException ignored) {}
            JOptionPane.showMessageDialog(MomGui.getFrame(), "An unexpected error occurred, stacktrace written to "
                    + file + ".", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    void list() {
        try {
            StringBuilder items = new StringBuilder();
            reader = new BufferedReader(new FileReader("./exp.txt"));
            System.gc();
            String lineRead = reader.readLine();
            while(lineRead != null) {
                if(!lineRead.contains("-")) {
                    lineRead = reader.readLine();
                    continue;
                }
                items.append(lineRead.substring(0, lineRead.indexOf("-"))).append(", ").append(lineRead.substring(lineRead.indexOf("$") + 1)).append("\n");
                lineRead = reader.readLine();
            }
            JOptionPane.showMessageDialog(MomGui.getFrame(), "ITEMS: " + items.toString(), "Inventory", JOptionPane.INFORMATION_MESSAGE);
        } catch(IOException e) {
            File file = new File("error_" + LocalDateTime.now());
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append(e.getMessage());
                writer.flush();
                writer.close();
            } catch(IOException ignored) {}
            JOptionPane.showMessageDialog(MomGui.getFrame(), "An unexpected error occurred, stacktrace written to "
                    + file + ".", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
