package dev.mikita.rolt.entity;

public enum ConsumerStatus {
    BANNED("BANNED"),
    ACTIVE("ACTIVE"),
    DELETED("DELETED");

    private final String name;

    ConsumerStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
