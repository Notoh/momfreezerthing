package io.notoh.mom;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Item implements Comparable<Item> {
    
    private String name;
    private UUID uuid;
    private List<String> tags;
    private LocalDate date;
    
    public Item(String name, UUID uuid, LocalDate date, List<String> tags) {
        this.name = name;
        this.uuid = uuid;
        this.date = date;
        this.tags = tags;
    }

    public boolean hasTags(String... tags) {
        for(String tag : tags) {
            if(!this.tags.contains(tag.toUpperCase()))
                return false;
        }
        return true;
    }

    public boolean isExpired() {
        return date.isBefore(LocalDate.now());
    }

    public LocalDate getExpirationDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        StringBuilder builder = new StringBuilder();
        builder.append(name).append("-").append(uuid.toString()).append("$").append(parser.format(date));
        if(tags == null || tags.size() == 0) {
            return builder.toString();
        }
        builder.insert(0, breakDownTags());
        return builder.toString();
    }
    
    @Override
    public int compareTo(Item o) {
        return uuid.compareTo(o.uuid);
    }

    private String breakDownTags() {
        StringBuilder builder = new StringBuilder(", ");
        for(Iterator<String> i = tags.iterator(); i.hasNext();) {
            String tag = i.next();
            if(!i.hasNext())
                builder.append(tag).append(":");
            else
                builder.append(tag).append(", ");
        }
        return builder.toString();

    }
}
