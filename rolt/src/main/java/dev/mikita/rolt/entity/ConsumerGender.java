package dev.mikita.rolt.entity;

public enum ConsumerGender {
    MALE("MALE"),
    FEMALE("FEMALE");

    private final String name;

    ConsumerGender(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
