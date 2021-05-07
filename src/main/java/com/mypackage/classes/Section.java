package com.mypackage.classes;

import com.opencsv.bean.CsvBindByName;

public class Section implements Comparable<Section> {
    @CsvBindByName
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Section(String name) {
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
