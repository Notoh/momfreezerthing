package io.notoh.mom;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Item implements Serializable, Comparable<Item> {
    
    private String name;
    private UUID uuid;
    private Category category;
    private LocalDate date;
    
    public Item(String name, UUID uuid, Category category, LocalDate date) {
        this.name = name;
        this.uuid = uuid;
        this.category = category;
        this.date = date;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        StringBuilder builder = new StringBuilder();
        builder.append(name).append("-").append(uuid.toString()).append("$").append(parser.format(date));
        if(category == null) {
            return builder.toString();
        }
        builder.insert(0, category.toString());
        return builder.toString();
    }
    
    @Override
    public int compareTo(Item o) {
        return uuid.compareTo(o.uuid);
    }
}
