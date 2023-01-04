package dev.mikita.rolt.entity;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    MODERATOR("ROLE_MODERATOR"),
    TENANT("ROLE_TENANT"),
    LANDLORD("ROLE_LANDLORD"),
    GUEST("ROLE_GUEST");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
