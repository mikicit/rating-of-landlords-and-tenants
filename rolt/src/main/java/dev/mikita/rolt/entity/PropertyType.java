package dev.mikita.rolt.entity;

public enum PropertyType {
    APARTMENT("APARTMENT"),
    HOUSE("HOUSE"),
    ROOM("ROOM");

    private final String name;

    PropertyType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
