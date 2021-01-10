package ru.rsoi.OAuth2AuthorizationServer.role;

public enum ApplicationUserPermission {
    ALL_READ("all:read"),
    ALL_WRITE("all:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
