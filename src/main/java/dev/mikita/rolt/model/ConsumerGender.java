package dev.mikita.rolt.model;

/**
 * The enum Consumer gender.
 */
public enum ConsumerGender {
    /**
     * Male consumer gender.
     */
    MALE("MALE"),
    /**
     * Female consumer gender.
     */
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
