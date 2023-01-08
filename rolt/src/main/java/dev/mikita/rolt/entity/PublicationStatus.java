package dev.mikita.rolt.entity;

public enum PublicationStatus {
    PUBLISHED("PUBLISHED"),
    DELETED("DELETED"),
    MODERATION("MODERATION");

    private final String name;

    PublicationStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
