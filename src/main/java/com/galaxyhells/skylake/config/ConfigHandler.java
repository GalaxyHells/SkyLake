package com.galaxyhells.skylake.config;

import java.io.*;
import java.util.Properties;

public class ConfigHandler {
    private static final File configFile = new File("config/skylake.cfg");
    private static final Properties props = new Properties();

    // Valores padrão
    public static boolean bossAlert = true;

    public static void loadConfig() {
        if (!configFile.exists()) {
            saveConfig(); // Cria o arquivo se não existir
            return;
        }

        try (InputStream input = new FileInputStream(configFile)) {
            props.load(input);
            bossAlert = Boolean.parseBoolean(props.getProperty("bossAlert", "true"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try (OutputStream output = new FileOutputStream(configFile)) {
            props.setProperty("bossAlert", String.valueOf(bossAlert));
            props.store(output, "SkyLake Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}