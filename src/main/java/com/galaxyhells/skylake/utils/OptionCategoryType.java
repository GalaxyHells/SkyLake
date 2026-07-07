package com.galaxyhells.skylake.utils;

public enum OptionCategoryType {
    HUD("HUD"),
    RENDER("Renderização"),
    INVENTORY("Inventário"),
    MOVEMENT("Movimento"),
    COMBAT("Combate"),
    SYSTEM("Sistema"),
    KEYBINDS("Keybinds"),
    UI("Interface");

    private final String display;

    OptionCategoryType(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
