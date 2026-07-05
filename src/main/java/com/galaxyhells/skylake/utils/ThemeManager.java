package com.galaxyhells.skylake.utils;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.data.OptionalData;

import java.awt.Color;

/**
 * Gerenciador de temas e escala para a interface do SkyLake.
 * Altamente otimizado para ser chamado no render loop do Minecraft (60-1000+ FPS).
 */
public class ThemeManager {

    // CACHE PARA PERFORMANCE (Evita ler das opções e instanciar toda hora)
    private static Theme cachedTheme = Theme.ESCURO;
    private static float cachedScale = 1.0f;
    private static boolean isChroma = false;

    public enum Theme {
        NEON("Neon", 0x00C8C8, 0x00A0A0, 0x121212, 0xFFFFFF, 0xFF4C4C),
        CLARO("Claro", 0x646464, 0xC8C8C8, 0xF0F0F0, 0x1A1A1A, 0xD32F2F),
        ESCURO("Escuro", 0x323232, 0x1E1E1E, 0x141414, 0xE0E0E0, 0xFF5252),
        CUSTOM("Custom", 0x00C8C8, 0x1E1E1E, 0x141414, 0xFFFFFF, 0xFF5252); // Modificado pelo usuário no menu

        private final String name;
        private int primary, secondary, background, text, error;

        Theme(String name, int primary, int secondary, int background, int text, int error) {
            this.name = name;
            this.primary = primary;
            this.secondary = secondary;
            this.background = background;
            this.text = text;
            this.error = error;
        }

        public String getName() { return name; }
        public int getPrimary() { return primary; }
        public int getSecondary() { return secondary; }
        public int getBackground() { return background; }
        public int getText() { return text; }
        public int getError() { return error; }

        // Permite alterar o tema customizado em tempo real
        public void setCustomColors(int p, int s, int b, int t) {
            if (this == CUSTOM) {
                this.primary = p; this.secondary = s; this.background = b; this.text = t;
            }
        }
    }

    /**
     * DEVE SER CHAMADO QUANDO AS OPÇÕES SÃO SALVAS/FECHADAS.
     * Atualiza os valores em cache para evitar operações caras no loop de renderização.
     */
    public static void updateCache() {
        if (SkyLake.optionsService == null) return;

        // 1. Atualiza Escala
        Float scale = SkyLake.optionsService.get(OptionType.HUD_SCALE);
        cachedScale = scale != null ? Math.max(0.5f, Math.min(2.0f, scale)) : 1.0f;

        // 2. Atualiza Chroma
        Boolean chromaOpt = SkyLake.optionsService.get(OptionType.CHROMA_UI);
        isChroma = chromaOpt != null && chromaOpt;

        // 3. Atualiza Tema
        int index = SkyLake.optionsService.getOptionalIndex(OptionType.THEME_STYLE);
        Object themeDataObj = SkyLake.optionsService.get(OptionType.THEME_STYLE);

        if (themeDataObj instanceof OptionalData) {
            OptionalData<?> themeData = (OptionalData<?>) themeDataObj;
            if (index >= 0 && index < themeData.size()) {
                String themeName = String.valueOf(themeData.get(index));
                for (Theme theme : Theme.values()) {
                    if (theme.getName().equals(themeName)) {
                        cachedTheme = theme;
                        return;
                    }
                }
            }
        }
        cachedTheme = Theme.ESCURO;
    }

    // ==========================================
    // ESCALA
    // ==========================================
    public static float getHUDScale() { return cachedScale; }
    public static int scale(int value) { return (int) (value * cachedScale); }
    public static float scale(float value) { return value * cachedScale; }

    // ==========================================
    // CORES PRINCIPAIS (Retorna HEX com Alpha 255 padrão)
    // ==========================================
    public static int getPrimaryColor() {
        return isChroma ? getChromaColor() : (0xFF000000 | cachedTheme.getPrimary());
    }
    public static int getSecondaryColor() { return 0xFF000000 | cachedTheme.getSecondary(); }
    public static int getBackgroundColor() { return 0xFF000000 | cachedTheme.getBackground(); }
    public static int getTextColor() { return 0xFF000000 | cachedTheme.getText(); }
    public static int getErrorColor() { return 0xFF000000 | cachedTheme.getError(); }

    // ==========================================
    // CORES COM TRANSPARÊNCIA (Operação Bitwise ultra-rápida, zero lag)
    // ==========================================
    public static int getPrimaryColor(int alpha) {
        int color = isChroma ? getChromaColor() : cachedTheme.getPrimary();
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    public static int getSecondaryColor(int alpha) {
        return (cachedTheme.getSecondary() & 0x00FFFFFF) | (alpha << 24);
    }

    public static int getBackgroundColor(int alpha) {
        return (cachedTheme.getBackground() & 0x00FFFFFF) | (alpha << 24);
    }

    public static int getErrorColor(int alpha) {
        return (cachedTheme.getError() & 0x00FFFFFF) | (alpha << 24);
    }

    // ==========================================
    // UTILITÁRIOS VISUAIS
    // ==========================================

    /**
     * Gera uma cor de arco-íris dinâmica baseada no tempo.
     */
    public static int getChromaColor() {
        long time = System.currentTimeMillis();
        float hue = (time % 3000L) / 3000f; // Tempo para dar uma volta completa no arco-íris
        return Color.HSBtoRGB(hue, 0.8f, 1.0f);
    }
}