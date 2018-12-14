package io.notoh.mom;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

@SuppressWarnings("Duplicates")
public final class Backend {
    
    private static Backend instance;
    private BufferedReader reader;
    private BufferedWriter writer;
    private File exp;
    private boolean categorized;
    private ArrayList<Item> items = new ArrayList<>();


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
            subCategory(line);
            line = reader.readLine();
        }
    }
    
    private void subCategory(String line) {
        if(line.startsWith(", ")) { //item has tags
            String item = line.substring(line.lastIndexOf(":")+1);
            String itemName = item.substring(0, item.indexOf("-"));
            String id = item.substring(item.indexOf("-")+1, item.lastIndexOf("$"));
            UUID uuid = UUID.fromString(id);
            String dateText = item.substring(item.lastIndexOf("$")+1);
            LocalDate date = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String match = ", ";
            ArrayList<String> itemTags = new ArrayList<>();
            int[] indexes = new int[line.length() - item.length()];
            int j = 0;
            for(int i = -1; (i = line.indexOf(match, i + 1)) != -1; i++) {
                indexes[j] = i;
                j++;
            }
            for(int i = 0; i < indexes.length; i++) {
                if(i == indexes.length - 1) {
                    itemTags.add(line.substring(indexes[i]+2, line.indexOf(":")).toUpperCase());
                    continue;
                }
                itemTags.add(line.substring(indexes[i]+2, indexes[i+1]).toUpperCase());
            }
            items.add(new Item(itemName, uuid, date, itemTags));
        }
    }
    
    void writeAll() {
        try {
            writer = new BufferedWriter(new FileWriter(exp));
            for(Item item : items) {
                writer.write(item.toString());
                writer.newLine();
            }
            writer.flush();
            writer.close();
            reader.close();
        } catch(IOException ignored) {}
    }
    
    void addExpiry(String name, String datetxt, String tags) {
        tags = tags.toUpperCase();
        LocalDate date = LocalDate.parse(datetxt, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        items.add(new Item(name.toUpperCase(), UUID.randomUUID(), date, Arrays.asList(tags.split(", "))));
        JOptionPane.showMessageDialog(null, "Added item " + name + " expiring" + datetxt + ".", "Success!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    void checkExpiry(String tags) {
        tags = tags.toUpperCase();
        String[] arrTags = tags.split(", ");
        ArrayList<Item> expired = new ArrayList<>();
        for(Item item : items) {
            if(item.hasTags(arrTags) && item.isExpired())
                expired.add(item);
        }
        JOptionPane panel = new JOptionPane();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        StringBuilder builder = new StringBuilder();
        for(Item item : expired) {
            builder.append(item.getName()).append("expires ").append(item.getExpirationDate()).append("\n");
        }
        panel.setMessage(builder);
        panel.setVisible(true);

    }
    
    void remove(String name, String tags) {
        String[] arrTags = tags.split(", ");
        for(Item item : items) {
            if(item.getName().equalsIgnoreCase(name) && item.hasTags(arrTags)) {
                JOptionPane.showMessageDialog(null, "Item was successfully removed.");
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Not found.");
    }
    
    void list(String tags) {
        String[] arrTags = tags.split(", ");
        ArrayList<Item> validItems = new ArrayList<>();
        for(Item item : items) {
            if(item.hasTags(arrTags))
                validItems.add(item);
        }
        JOptionPane panel = new JOptionPane();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        StringBuilder builder = new StringBuilder();
        for(Item item : validItems) {
            builder.append(item.getName()).append("expires ").append(item.getExpirationDate()).append("\n");
        }
        panel.setMessage(builder);
        panel.setVisible(true);
    }
    
    void search(String text, String tags) {
        String[] arrTags = tags.split(", ");
        ArrayList<Item> validItems = new ArrayList<>();
        for(Item item : items) {
            if(item.hasTags(arrTags) && item.getName().contains(text.toUpperCase()))
                validItems.add(item);
        }
        JOptionPane panel = new JOptionPane();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        StringBuilder builder = new StringBuilder();
        for(Item item : validItems) {
            builder.append(item.getName()).append("expires ").append(item.getExpirationDate()).append("\n");
        }
        panel.setMessage(builder);
        panel.setVisible(true);
    }
    
}
