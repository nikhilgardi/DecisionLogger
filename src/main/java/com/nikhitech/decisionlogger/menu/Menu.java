package com.nikhitech.decisionlogger.menu;

/*
 * ===============================================================
 * MENU REGISTRY
 * ===============================================================
 *
 * Purpose
 * ---------------------------------------------------------------
 * Central registry of sidebar menus.
 *
 * OCP
 * ---------------------------------------------------------------
 * New menus are added here without modifying
 * interceptor logic.
 */

public enum Menu {

    ADMIN("/admin", "admin"),
    DECISION("/decision", "decision");

    private final String uri;
    private final String menuName;

    Menu(String uri, String menuName) {
        this.uri = uri;
        this.menuName = menuName;
    }

    public String getUri() {
        return uri;
    }

    public String getMenuName() {
        return menuName;
    }
}