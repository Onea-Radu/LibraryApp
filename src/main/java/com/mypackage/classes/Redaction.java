package com.mypackage.classes;

import com.opencsv.bean.CsvBindByName;

public class Redaction implements Comparable<Redaction> {
    @CsvBindByName
    private String name;

    public Redaction(String name) {
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
