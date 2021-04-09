package com.mypackage.classes;

public class Redaction implements Comparable<Redaction> {
    private String name;

    Redaction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Redaction{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Redaction o) {
        return this.getName().compareTo(o.getName());
    }
}
