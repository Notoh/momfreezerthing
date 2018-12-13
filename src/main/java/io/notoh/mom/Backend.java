package io.notoh.mom;

import javax.swing.JOptionPane;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

@SuppressWarnings("Duplicates")
public final class Backend {
    
    private static Backend instance;
    private BufferedReader reader;
    private BufferedWriter writer;
    private File exp;
    private boolean categorized;
    
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
        boolean exists = true;
        if(!exp.exists()) {
            exists = exp.createNewFile();
        }
        if(exists)
            categorize();
        else
            throw new IOException("File inoperable");
        
    }
    
    private void categorize() throws IOException {
        reader = new BufferedReader(new FileReader(exp));
        String line = reader.readLine();
        while(line != null) {
            
            line = reader.readLine();
        }
    }
    
    private void subCategory(String line) {
        if(line.startsWith("|-")) { //item has a category
            String item = line.substring(line.lastIndexOf(":")+1);
            String itemName = item.substring(0, item.indexOf("-"));
            String id = item.substring(item.indexOf("-")+1, item.lastIndexOf("$"));
            UUID uuid = UUID.fromString(id);
            String dateText = item.substring(item.lastIndexOf("$")+1);
            LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            String lowestCategory = line.substring(line.lastIndexOf("|-")+2, line.lastIndexOf(":"));
            //todo parse more categories
        }
    }
    
    void writeAll() {
    
    }
    
    void addExpiry(String name, String datetxt, String category) {

    }
    
    void checkExpiry(String category) {

    }
    
    void remove(String name, String category) {

    }
    
    void list(String category) {
    }
    
    void search(String text, String category) {
    }
    
    
    
    public static final class Categorized implements Serializable {
        private static ArrayList<Category> rootCategories = new ArrayList<>();
        private static ArrayList<Item> items = new ArrayList<>();
    }
    
}
