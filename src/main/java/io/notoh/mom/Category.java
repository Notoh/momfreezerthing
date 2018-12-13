package io.notoh.mom;

import java.io.Serializable;

public class Category implements Serializable, Comparable<Category> {
    
    private Category parent;
    private String name;
    
    public Category(Category parent, String name) {
        this.parent = parent;
        this.name = name;
    }
    
    @Override
    public String toString() {
        if(parent == null)
            return name + ":";
        return parent + "\n|-" + name + ":";
    }
    
    @Override
    public int compareTo(Category o) {
        return name.compareTo(o.name);
    }
}
