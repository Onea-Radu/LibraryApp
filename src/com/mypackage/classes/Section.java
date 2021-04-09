package com.mypackage.classes;

public class Section implements Comparable<Section> {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Section(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Section{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Section o) {
        return this.getName().compareTo(o.getName());
    }
}
