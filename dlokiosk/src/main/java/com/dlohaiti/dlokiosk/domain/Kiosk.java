package com.dlohaiti.dlokiosk.domain;

public class Kiosk {
    private final String id;
    private final String password;

    public Kiosk(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
