package com.galaxyhells.skylake.config;

import java.io.*;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class ConfigHandler {
    private static final File configFile = new File("config/skylake.cfg");
    private static final Properties props = new Properties();
    public static boolean rarityBackground = true;
    public static boolean statOverlay = true;
    public static boolean mutantTimer = true;
    public static boolean magmaTimer = true;
    public static boolean mutantHighlight = true;

    public static boolean bossAlert = true;

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
            bossAlert = Boolean.parseBoolean(props.getProperty("bossAlert", "true"));

            // Carrega os slots trancados
            lockedSlots.clear();
            String slotsString = props.getProperty("lockedSlots", "");
            if (!slotsString.isEmpty()) {
                for (String s : slotsString.split(",")) {
                    try { lockedSlots.add(Integer.parseInt(s)); } catch (Exception ignored) {}
                }
            }

            rarityBackground = Boolean.parseBoolean(props.getProperty("rarityBackground", "true"));
            statOverlay = Boolean.parseBoolean(props.getProperty("statOverlay", "true"));
            mutantTimer = Boolean.parseBoolean(props.getProperty("mutantTimer", "true"));
            magmaTimer = Boolean.parseBoolean(props.getProperty("magmaTimer", "true"));
            magmaTimer = Boolean.parseBoolean(props.getProperty("mutantHighlight", "true"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try { input.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    public static void saveConfig() {
        OutputStream output = null;
        try {
            output = new FileOutputStream(configFile);
            props.setProperty("bossAlert", String.valueOf(bossAlert));

            // Converte a lista de slots para um texto (ex: "0,1,8,")
            StringBuilder sb = new StringBuilder();
            for (Integer slot : lockedSlots) {
                sb.append(slot).append(",");
            }
            props.setProperty("lockedSlots", sb.toString());

            props.setProperty("rarityBackground", String.valueOf(rarityBackground));

            props.store(output, "SkyLake Configuration");
            props.setProperty("statOverlay", String.valueOf(statOverlay));
            props.setProperty("mutantTimer", String.valueOf(mutantTimer));
            props.setProperty("magmaTimer", String.valueOf(magmaTimer));
            props.setProperty("mutantHighlight", String.valueOf(mutantHighlight));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try { output.close(); } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }
}