package com.mypackage.classes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Status {
    NEW,
    PRISTINE,
    USED,
    DAMAGED,
    UNUSABLE;
    private static final List<Status> values = Arrays.asList(values());

    public static List<Status> getValues() {
        return Collections.unmodifiableList(values);
    }

    public static Status getLast() {
        return values.get(values.size() - 1);
    }

    public Status next() {
        return values.get(this.ordinal() + 1);
    }
}
