package com.mypackage.classes;

import com.opencsv.bean.CsvBindByName;

public abstract class Person implements Comparable<Person> {
    @CsvBindByName
    protected String name;

    Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Person o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return " name=" + name;

    }
}
