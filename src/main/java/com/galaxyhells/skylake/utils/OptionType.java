package com.galaxyhells.skylake.utils;

import com.galaxyhells.skylake.data.OptionalData;

public enum OptionType {
    // HUD Features
    MUTANT_TIMER("Temporizador do Enderman Mutante", OptionCategoryType.HUD, true, Boolean.class),
    MAGMA_TIMER("Temporizador do Magma Boss", OptionCategoryType.HUD, true, Boolean.class),
    STAT_OVERLAY("Barras de Status", OptionCategoryType.HUD, true, Boolean.class),
    LOW_HEALTH_WARNING("Alerta de Vida Baixa", OptionCategoryType.HUD, true, Boolean.class),
    FANCY_HUD("Hotbar Bonita", OptionCategoryType.HUD, true, Boolean.class),
    FANCY_STAT_OVERLAY("Status Bonita", OptionCategoryType.HUD, true, Boolean.class),
    MASCOTE_HUD("HUD do Mascote", OptionCategoryType.HUD, true, Boolean.class),
    //MAP_FEATURE("Mapa (Tecla M)", OptionCategoryType.HUD, true, Boolean.class),
    ITEM_LOG("Log de Itens", OptionCategoryType.HUD, true, Boolean.class),

    // Render Features
    MUTANT_HIGHLIGHT("Destacar Mutante", OptionCategoryType.RENDER, true, Boolean.class),
    MUTANT_SPAWN_BOXES("Locais de Spawn do Enderman Mutante", OptionCategoryType.RENDER, true, Boolean.class),
    END_CRYSTAL_BOXES("Locais de Spawn do EndCrystal", OptionCategoryType.RENDER, true, Boolean.class),
    DRAGON_HIGHLIGHT("Destacar Dragão", OptionCategoryType.RENDER, true, Boolean.class),
    TREASURE_WAYPOINT("Waypoints Tesouros", OptionCategoryType.RENDER, true, Boolean.class),
    //TREASURE_RADAR("Radar Tesouros", OptionCategoryType.RENDER, true, Boolean.class),
    HIDE_NAMETAGS("Esconder Nametag vanilla", OptionCategoryType.RENDER, false, Boolean.class),
    CUSTOM_NAMETAGS("Nametag Customizada", OptionCategoryType.RENDER, false, Boolean.class),

    // Inventory Features
    RARITY_BACKGROUND("Fundo por Raridade", OptionCategoryType.INVENTORY, true, Boolean.class),
    SLOT_LOCK("Travar Slots", OptionCategoryType.INVENTORY, true, Boolean.class),
    LOCKED_SLOTS("Slots Travados", OptionCategoryType.INVENTORY, "", String.class),
    CENTER_INVENTORY("Centralizar Inventário", OptionCategoryType.INVENTORY, true, Boolean.class),

    // Movement Features
    AUTO_SPRINT("Correr Automático", OptionCategoryType.MOVEMENT, true, Boolean.class),
    AUTO_FISHING("Pescaria Automática", OptionCategoryType.MOVEMENT, true, Boolean.class),

    // Combat Features
    AUTO_ATTACK("Ataque Automático", OptionCategoryType.COMBAT, true, Boolean.class),

    // System Features
    AUTO_LOGIN("Login Automático", OptionCategoryType.SYSTEM, true, Boolean.class),
    BOSS_ALERT("Alerta de Boss", OptionCategoryType.SYSTEM, true, Boolean.class),
    DRAGON_DROP_ANNOUNCER("Anunciador de Drops do Dragão", OptionCategoryType.SYSTEM, true, Boolean.class),
    MAGMA_DROP_ANNOUNCER("Anunciador de Drops do Magma Boss", OptionCategoryType.SYSTEM, true, Boolean.class),

    // Keybinds
    KEY_SLOT_LOCK("Travar Slot", OptionCategoryType.KEYBINDS, 'L', Character.class),
    KEY_CONFIG_MENU("Menu Config", OptionCategoryType.KEYBINDS, 'G', Character.class),

    // UI Settings
    HUD_SCALE("Escala HUD", OptionCategoryType.UI, 1.0f, Float.class),
    TREASURE_GUI("GUI Tesouros", OptionCategoryType.UI, true, Boolean.class),
    //TREASURE_RADAR("Radar de Tesouros", OptionCategoryType.RENDER, true, Boolean.class),
    CHROMA_UI("Interface Chroma (RGB)", OptionCategoryType.UI, false, Boolean.class),
    THEME_STYLE("Tema da Interface", OptionCategoryType.UI,
            new OptionalData<>(String.class, "Neon", "Claro", "Escuro"), null);

    private final String display;
    private final OptionCategoryType categoryType;
    private final Object defaultValue;
    private final Class<?> type;

    OptionType(String display, OptionCategoryType categoryType, Object defaultValue, Class<?> type) {
        this.display = display;
        this.categoryType = categoryType;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    public String getDisplay() {
        return display;
    }

    public OptionCategoryType getCategoryType() {
        return categoryType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public Class<?> getType() {
        return type;
    }
}
