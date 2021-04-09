package com.mypackage.classes;


public class Genre implements Comparable<Genre> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Genre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Genre o) {
        return this.getName().compareTo(o.getName());
    }
}
