package com.galaxyhells.skylake.config;

import java.io.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class ConfigHandler {
    private static final File configFile = new File("config/skylake.cfg");
    private static final Properties props = new Properties();
    private static final String CONFIG_HEADER = "SkyLake Configuration";
    
    // Valores padrão
    private static final boolean DEFAULT_RARITY_BACKGROUND = true;
    private static final boolean DEFAULT_STAT_OVERLAY = true;
    private static final boolean DEFAULT_MUTANT_TIMER = true;
    private static final boolean DEFAULT_MAGMA_TIMER = true;
    private static final boolean DEFAULT_MUTANT_HIGHLIGHT = true;
    private static final boolean DEFAULT_MUTANT_SPAWN_BOXES = true;
    private static final boolean DEFAULT_BOSS_ALERT = true;
    private static final boolean DEFAULT_AFK_FEATURE = true;
    private static final boolean DEFAULT_FANCY_HUD = true;
    private static final boolean DEFAULT_MAP_FEATURE = true;
    private static final boolean DEFAULT_FANCY_STAT_OVERLAY = true;
    private static final boolean DEFAULT_AUTO_SPRINT_FEATURE = true;
    private static final boolean DEFAULT_AUTO_LOGIN_FEATURE = true;
    
    // Configurações públicas
    public static boolean rarityBackground = DEFAULT_RARITY_BACKGROUND;
    public static boolean statOverlay = DEFAULT_STAT_OVERLAY;
    public static boolean mutantTimer = DEFAULT_MUTANT_TIMER;
    public static boolean magmaTimer = DEFAULT_MAGMA_TIMER;
    public static boolean mutantHighlight = DEFAULT_MUTANT_HIGHLIGHT;
    public static boolean mutantSpawnBoxes = DEFAULT_MUTANT_SPAWN_BOXES;
    public static boolean bossAlert = DEFAULT_BOSS_ALERT;
    public static boolean afkFeature = DEFAULT_AFK_FEATURE;
    public static boolean fancyHUD = DEFAULT_FANCY_HUD;
    public static boolean mapFeature = DEFAULT_MAP_FEATURE;
    public static boolean fancyStatOverlay = DEFAULT_FANCY_STAT_OVERLAY;
    public static boolean autoSprint = DEFAULT_AUTO_SPRINT_FEATURE;
    public static boolean autoLogin = DEFAULT_AUTO_LOGIN_FEATURE;

    // Lista global dos slots trancados
    public static final Set<Integer> lockedSlots = new HashSet<Integer>();

    public static void loadConfig() {
        if (!configFile.exists()) {
            saveConfig();
            return;
        }

        InputStream input = null;
        try {
            input = new FileInputStream(configFile);
            props.load(input);
            
            // Carrega configurações com valores padrão
            bossAlert = parseBoolean(props.getProperty("bossAlert"), DEFAULT_BOSS_ALERT);
            afkFeature = parseBoolean(props.getProperty("afkFeature"), DEFAULT_AFK_FEATURE);
            rarityBackground = parseBoolean(props.getProperty("rarityBackground"), DEFAULT_RARITY_BACKGROUND);
            statOverlay = parseBoolean(props.getProperty("statOverlay"), DEFAULT_STAT_OVERLAY);
            mutantTimer = parseBoolean(props.getProperty("mutantTimer"), DEFAULT_MUTANT_TIMER);
            magmaTimer = parseBoolean(props.getProperty("magmaTimer"), DEFAULT_MAGMA_TIMER);
            mutantHighlight = parseBoolean(props.getProperty("mutantHighlight"), DEFAULT_MUTANT_HIGHLIGHT);
            mutantSpawnBoxes = parseBoolean(props.getProperty("mutantSpawnBoxes"), DEFAULT_MUTANT_SPAWN_BOXES);
            fancyHUD = parseBoolean(props.getProperty("fancyHUD"), DEFAULT_FANCY_HUD);
            mapFeature = parseBoolean(props.getProperty("mapFeature"), DEFAULT_MAP_FEATURE);
            fancyStatOverlay = parseBoolean(props.getProperty("fancyStatOverlay"), DEFAULT_FANCY_STAT_OVERLAY);
            autoSprint = parseBoolean(props.getProperty("autoSprintFeature"), DEFAULT_AUTO_SPRINT_FEATURE);
            autoLogin = parseBoolean(props.getProperty("autoLoginFeature"), DEFAULT_AUTO_LOGIN_FEATURE);

            // Carrega os slots trancados com validação
            loadLockedSlots();
            
        } catch (IOException e) {
            System.err.println("[SkyLake] Erro ao carregar configuração: " + e.getMessage());
            // Restaura valores padrão em caso de erro
            restoreDefaults();
        } catch (Exception e) {
            System.err.println("[SkyLake] Erro inesperado ao carregar configuração: " + e.getMessage());
            restoreDefaults();
        } finally {
            closeQuietly(input);
        }
    }

    public static void saveConfig() {
        OutputStream output = null;
        try {
            // Garante que o diretório config exista
            File configDir = configFile.getParentFile();
            if (configDir != null && !configDir.exists()) {
                configDir.mkdirs();
            }
            
            output = new FileOutputStream(configFile);
            
            // Salva configurações
            props.setProperty("bossAlert", String.valueOf(bossAlert));
            props.setProperty("afkFeature", String.valueOf(afkFeature));
            props.setProperty("rarityBackground", String.valueOf(rarityBackground));
            props.setProperty("statOverlay", String.valueOf(statOverlay));
            props.setProperty("mutantTimer", String.valueOf(mutantTimer));
            props.setProperty("magmaTimer", String.valueOf(magmaTimer));
            props.setProperty("mutantHighlight", String.valueOf(mutantHighlight));
            props.setProperty("mutantSpawnBoxes", String.valueOf(mutantSpawnBoxes));
            props.setProperty("fancyHUD", String.valueOf(fancyHUD));
            props.setProperty("mapFeature", String.valueOf(mapFeature));
            props.setProperty("fancyStatOverlay", String.valueOf(fancyStatOverlay));
            props.setProperty("autoSprintFeature", String.valueOf(autoSprint));
            props.setProperty("autoLoginFeature", String.valueOf(autoLogin));

            // Salva slots trancados
            saveLockedSlots();

            props.store(output, CONFIG_HEADER);
        } catch (IOException e) {
            System.err.println("[SkyLake] Erro ao salvar configuração: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[SkyLake] Erro inesperado ao salvar configuração: " + e.getMessage());
        } finally {
            closeQuietly(output);
        }
    }
    
    // Métodos auxiliares
    
    private static boolean parseBoolean(String value, boolean defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    private static void loadLockedSlots() {
        lockedSlots.clear();
        String slotsString = props.getProperty("lockedSlots", "");
        if (!slotsString.isEmpty()) {
            for (String s : slotsString.split(",")) {
                try {
                    int slot = Integer.parseInt(s.trim());
                    if (slot >= 0 && slot <= 35) { // Validação: slots válidos de 0-35
                        lockedSlots.add(slot);
                    }
                } catch (NumberFormatException ignored) {
                    // Ignora valores inválidos
                }
            }
        }
    }
    
    private static void saveLockedSlots() {
        StringBuilder sb = new StringBuilder();
        for (Integer slot : lockedSlots) {
            sb.append(slot).append(",");
        }
        props.setProperty("lockedSlots", sb.toString());
    }
    
    private static void restoreDefaults() {
        bossAlert = DEFAULT_BOSS_ALERT;
        afkFeature = DEFAULT_AFK_FEATURE;
        rarityBackground = DEFAULT_RARITY_BACKGROUND;
        statOverlay = DEFAULT_STAT_OVERLAY;
        mutantTimer = DEFAULT_MUTANT_TIMER;
        magmaTimer = DEFAULT_MAGMA_TIMER;
        mutantHighlight = DEFAULT_MUTANT_HIGHLIGHT;
        mutantSpawnBoxes = DEFAULT_MUTANT_SPAWN_BOXES;
        fancyHUD = DEFAULT_FANCY_HUD;
        mapFeature = DEFAULT_MAP_FEATURE;
        fancyStatOverlay = DEFAULT_FANCY_STAT_OVERLAY;
        autoSprint = DEFAULT_AUTO_SPRINT_FEATURE;
        autoLogin = DEFAULT_AUTO_LOGIN_FEATURE;

        lockedSlots.clear();
    }
    
    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Silenciosamente ignorado
            }
        }
    }
}
