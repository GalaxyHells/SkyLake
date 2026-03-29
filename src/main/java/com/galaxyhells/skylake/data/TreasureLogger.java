package com.galaxyhells.skylake.data;

import com.galaxyhells.skylake.utils.WorldUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.BlockPos;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TreasureLogger {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Centralizamos os dados em uma pasta própria para não bagunçar a raiz da /config
    private static final String BASE_PATH = "config/skylake/";

    // Cache para evitar IO desnecessário e duplicatas na mesma sessão
    private static final Map<String, Set<String>> fileCache = new HashMap<>();

    /**
     * Carrega coordenadas de um arquivo.
     * O 'path' deve ser "nome_do_mundo/arquivo.json" (ex: "hub/skylake_treasures.json")
     */
    public static List<BlockPos> getCoordsFromFile(String path) {
        List<BlockPos> positions = new ArrayList<>();
        Set<String> coords = loadFileSet(path);

        for (String s : coords) {
            try {
                String[] parts = s.split(",");
                if (parts.length == 3) {
                    positions.add(new BlockPos(
                            Integer.parseInt(parts[0].trim()),
                            Integer.parseInt(parts[1].trim()),
                            Integer.parseInt(parts[2].trim())
                    ));
                }
            } catch (Exception ignored) {}
        }
        return positions;
    }

    /**
     * Salva uma nova coordenada detectando automaticamente o mundo atual.
     */
    public static void log(BlockPos pos, String fileName) {
        // Detecta o mundo atual (ex: "hub", "the_end", "vilarejo")
        String world = WorldUtils.getCurrentArea();
        String path = world + "/" + fileName;

        String coordKey = pos.getX() + "," + pos.getY() + "," + pos.getZ();

        // O cache agora diferencia arquivos pelo caminho completo (mundo + nome)
        Set<String> currentCoords = fileCache.computeIfAbsent(path, k -> loadFileSet(path));

        if (!currentCoords.contains(coordKey)) {
            currentCoords.add(coordKey);
            saveToFile(currentCoords, path);
        }
    }

    /**
     * Sobrecarga padrão que usa o mundo atual e o nome de arquivo básico.
     */
    public static void log(BlockPos pos) {
        log(pos, "skylake_treasures.json");
    }

    private static Set<String> loadFileSet(String path) {
        Set<String> resultSet = new HashSet<>();

        // 1. Tenta carregar do Config (Externo: config/skylake/mundo/arquivo.json)
        File configFile = new File(BASE_PATH + path);
        if (configFile.exists()) {
            try (Reader reader = new FileReader(configFile)) {
                Set<String> loaded = gson.fromJson(reader, new TypeToken<HashSet<String>>(){}.getType());
                if (loaded != null) resultSet.addAll(loaded);
            } catch (IOException e) { e.printStackTrace(); }
        }

        // 2. Tenta carregar do JAR (Interno: assets/skylake/treasures/mundo/arquivo.json)
        try {
            InputStream is = TreasureLogger.class.getResourceAsStream("/assets/skylake/treasures/" + path);
            if (is != null) {
                try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    Set<String> defaults = gson.fromJson(reader, new TypeToken<HashSet<String>>(){}.getType());
                    if (defaults != null) resultSet.addAll(defaults);
                }
            }
        } catch (Exception ignored) {}

        return resultSet;
    }

    private static void saveToFile(Set<String> coords, String path) {
        File configFile = new File(BASE_PATH + path);
        try {
            // Cria a árvore de diretórios (ex: cria a pasta 'hub' se não existir)
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }
            try (Writer writer = new FileWriter(configFile)) {
                gson.toJson(coords, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Limpa o arquivo de log do mundo atual
     */
    public static void clearLog(String fileName) {
        String world = WorldUtils.getCurrentArea();
        String path = world + "/" + fileName;

        fileCache.put(path, new HashSet<>());
        saveToFile(new HashSet<>(), path);
    }
}