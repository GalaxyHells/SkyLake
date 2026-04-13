package com.galaxyhells.skylake.utils;

/**
 * Constantes centralizadas para valores mágicos usados em todo o mod
 */
public final class ModConstants {
    
    // Tempos em milissegundos
    public static final long SECOND_IN_MS = 1000L;
    public static final long MINUTE_IN_MS = 60 * SECOND_IN_MS;
    public static final long HOUR_IN_MS = 60 * MINUTE_IN_MS;
    
    // Configurações AFK
    public static final long AFK_THRESHOLD_DEFAULT = 1 * MINUTE_IN_MS;
    public static final long AFK_COMMAND_COOLDOWN = 5 * SECOND_IN_MS;
    public static final double MOVEMENT_THRESHOLD = 0.1;
    public static final float ROTATION_THRESHOLD = 1.0f;
    
    // Configurações Mutant
    public static final int MUTANT_SPAWN_INTERVAL_MINUTES = 12;
    public static final long MUTANT_SPAWN_INTERVAL_MS = MUTANT_SPAWN_INTERVAL_MINUTES * MINUTE_IN_MS;
    public static final float MUTANT_HEIGHT = 3.0f;
    public static final float MUTANT_WIDTH = 1.0f;
    public static final float MUTANT_Y_OFFSET = 2.5f;
    public static final int MUTANT_HIGHLIGHT_COLOR = 0xFFFF00FF;
    public static final long MUTANT_CHECK_INTERVAL = 100; // Verificar a cada 100ms
    
    // Configurações Magma Boss
    public static final int[] MAGMA_SPAWN_HOURS = {6, 14, 22};
    public static final long MAGMA_ALERT_THRESHOLD_MINUTES = 30;
    public static final long MAGMA_WARNING_THRESHOLD_MINUTES = 10;
    
    // Configurações de Renderização
    public static final float DEFAULT_ALPHA = 0.4f;
    public static final float LINE_WIDTH_DEFAULT = 2.0f;
    public static final int WAYPOINT_COLOR_CYAN = 0xFF00FFFF;
    public static final int SPAWN_BOX_COLOR_GREEN = 0x5500FF00;
    
    // Configurações de Inventário
    public static final int HOTBAR_SIZE = 9;
    public static final int INVENTORY_SIZE = 36;
    public static final int SLOT_SIZE = 16;
    
    // Configurações de Distância
    public static final double TREASURE_COLLECTION_DISTANCE = 4.0; // Raio de 4 blocos
    public static final double WAYPOINT_INTERACTION_DISTANCE = 2.0; // Raio de 2 blocos
    
    // Configurações de Chat
    public static final long BOSS_ALERT_DURATION = 3 * SECOND_IN_MS;
    public static final String MUTANT_SPAWN_MESSAGE = "Enderman Mutante foi visto nas proximidades";
    public static final String HEADLESS_SPAWN_MESSAGE = "Headless Horseman";
    public static final String MUTANT_NAME_TAG = "Mutante";
    
    // Cores Básicas
    public static final int COLOR_GREEN = 0xFF55FF55;
    public static final int COLOR_YELLOW = 0xFFFFFF55;
    public static final int COLOR_RED = 0xFFFF5555;
    public static final int COLOR_PURPLE = 0xFFAA00AA;
    public static final int COLOR_ORANGE = 0xFFFFAA00;
    public static final int COLOR_WHITE = 0xFFFFFF;
    public static final int COLOR_BLACK = 0xFF000000;
    public static final int COLOR_BLUE = 0xFF5555FF;
    public static final int COLOR_CYAN = 0xFF55FFFF;
    public static final int COLOR_GRAY = 0xFF888888;
    public static final int COLOR_DARK_GRAY = 0xFF444444;
    public static final int COLOR_LIGHT_GRAY = 0xFFAAAAAA;
    public static final int COLOR_PINK = 0xFFFF55FF;
    public static final int COLOR_LIME = 0xFF55FF00;
    public static final int COLOR_BROWN = 0xFF8B4513;
    
    // Cores para HUD e UI
    public static final int HUD_BACKGROUND = 0x90000000;// 0x80000000; // Semi-transparente preto

    public static final int HUD_BORDER = 0xFFFFFFFF;     // Branco
    public static final int HUD_TEXT_PRIMARY = 0xFFFFFFFF; // Branco
    public static final int HUD_TEXT_SECONDARY = 0xFFAAAAAA; // Cinza claro
    public static final int HUD_TEXT_SUCCESS = 0xFF55FF55;  // Verde
    public static final int HUD_TEXT_WARNING = 0xFFFFFF55;  // Amarelo
    public static final int HUD_TEXT_DANGER = 0xFFFF5555;   // Vermelho
    
    // Cores para Renderização 3D
    public static final int MUTANT_HIGHLIGHT_NEON = 0xFFFF00FF;     // Roxo neon
    public static final int SPAWN_BOX_FILL = 0x5500FF00;           // Verde semi-transparente
    public static final int SPAWN_BOX_OUTLINE = 0xFF00FF00;        // Verde sólido
    public static final int TREASURE_WAYPOINT = 0xFF00FFFF;         // Ciano
    public static final int TREASURE_COLLECTED = 0xFF55FF55;        // Verde
    public static final int WAYPOINT_REACHED = 0xFF888888;          // Cinza
    
    // Cores para Raridade de Itens
    public static final int RARITY_COMMON = 0xFFFFFFFF;      // Branco
    public static final int RARITY_UNCOMMON = 0xFF55FF55;    // Verde
    public static final int RARITY_RARE = 0xFF5555FF;         // Azul
    public static final int RARITY_EPIC = 0xFFAA00AA;         // Roxo
    public static final int RARITY_LEGENDARY = 0xFFFFAA00;   // Laranja
    public static final int RARITY_MYTHIC = 0xFFFF5555;      // Vermelho
    
    // Cores para Timer e Alertas
    public static final int TIMER_NORMAL = 0xFF55FF55;       // Verde
    public static final int TIMER_WARNING = 0xFFFFFF55;      // Amarelo
    public static final int TIMER_CRITICAL = 0xFFFF5555;     // Vermelho
    public static final int ALERT_FLASH = 0xFFFFFFFF;        // Branco
    
    // Cores para Debug
    public static final int DEBUG_BOX = 0xFFFF0000;          // Vermelho
    public static final int DEBUG_TEXT = 0xFFFFFF00;         // Amarelo
    public static final int DEBUG_LINE = 0xFF00FF00;         // Verde
    
    /**
     * Converte uma cor ARGB (0xAARRGGBB) para valores RGBA individuais
     * @param argbColor Cor no formato 0xAARRGGBB
     * @return Array com [red, green, blue, alpha] (valores 0-255)
     */
    public static int[] argbToRgba(int argbColor) {
        return new int[] {
            (argbColor >> 16) & 0xFF,  // Red
            (argbColor >> 8) & 0xFF,   // Green
            argbColor & 0xFF,          // Blue
            (argbColor >> 24) & 0xFF   // Alpha
        };
    }
    
    /**
     * Converte uma cor ARGB (0xAARRGGBB) para valores RGBA normalizados (0.0-1.0)
     * @param argbColor Cor no formato 0xAARRGGBB
     * @return Array com [red, green, blue, alpha] (valores 0.0-1.0)
     */
    public static float[] argbToRgbaNormalized(int argbColor) {
        int[] rgba = argbToRgba(argbColor);
        return new float[] {
            rgba[0] / 255.0f,  // Red
            rgba[1] / 255.0f,  // Green
            rgba[2] / 255.0f,  // Blue
            rgba[3] / 255.0f   // Alpha
        };
    }
    
    /**
     * Obtém o componente alpha de uma cor ARGB
     * @param argbColor Cor no formato 0xAARRGGBB
     * @return Valor alpha (0-255)
     */
    public static int getAlpha(int argbColor) {
        return (argbColor >> 24) & 0xFF;
    }
    
    /**
     * Obtém o componente red de uma cor ARGB
     * @param argbColor Cor no formato 0xAARRGGBB
     * @return Valor red (0-255)
     */
    public static int getRed(int argbColor) {
        return (argbColor >> 16) & 0xFF;
    }
    
    /**
     * Obtém o componente green de uma cor ARGB
     * @param argbColor Cor no formato 0xAARRGGBB
     * @return Valor green (0-255)
     */
    public static int getGreen(int argbColor) {
        return (argbColor >> 8) & 0xFF;
    }
    
    /**
     * Obtém o componente blue de uma cor ARGB
     * @param argbColor Cor no formato 0xAARRGGBB
     * @return Valor blue (0-255)
     */
    public static int getBlue(int argbColor) {
        return argbColor & 0xFF;
    }
    
    /**
     * Converte uma cor ARGB (0xAARRGGBB) para java.awt.Color
     * @param argbColor Cor no formato 0xAARRGGBB
     * @return objeto Color com alpha
     */
    public static java.awt.Color argbToColor(int argbColor) {
        int[] rgba = argbToRgba(argbColor);
        return new java.awt.Color(rgba[0], rgba[1], rgba[2], rgba[3]);
    }
    
    /**
     * Converte uma cor ARGB (0xAARRGGBB) para java.awt.Color sem alpha
     * @param argbColor Cor no formato 0xAARRGGBB
     * @return objeto Color sem alpha (sempre opaco)
     */
    public static java.awt.Color argbToColorOpaque(int argbColor) {
        int[] rgba = argbToRgba(argbColor);
        return new java.awt.Color(rgba[0], rgba[1], rgba[2]);
    }
    
    /**
     * Converte uma cor ARGB (0xAARRGGBB) para java.awt.Color com alpha normalizado
     * @param argbColor Cor no formato 0xAARRGGBB
     * @return objeto Color com alpha float (0.0-1.0)
     */
    public static java.awt.Color argbToColorFloatAlpha(int argbColor) {
        int[] rgba = argbToRgba(argbColor);
        return new java.awt.Color(rgba[0] / 255.0f, rgba[1] / 255.0f, rgba[2] / 255.0f, rgba[3] / 255.0f);
    }
    
    // Privado para evitar instanciação
    private ModConstants() {
        throw new UnsupportedOperationException("Classe de constantes não pode ser instanciada");
    }
}
