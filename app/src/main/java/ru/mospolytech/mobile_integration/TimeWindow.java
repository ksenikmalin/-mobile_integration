package ru.mospolytech.mobile_integration;

public class TimeWindow {

    public String id;
    public String name;

    public TimeWindow(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
