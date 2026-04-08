package com.galaxyhells.skylake.data;

import com.galaxyhells.skylake.utils.WorldUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TreasureLogger {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Caminho atualizado - usar caminho relativo como ConfigHandler
    private static final String BASE_PATH = "config/skylake/treasures/";

    private static final Map<String, Set<String>> fileCache = new HashMap<>();

    /**
     * NOVO MÉTODO: Garante que a pasta raiz do mod sempre exista.
     * Retorna o objeto File da pasta 'treasures'.
     */
    private static File getTreasuresDir() {
        // Mudança: usar caminho relativo como ConfigHandler
        File dir = new File(BASE_PATH);
        if (!dir.exists()) {
            dir.mkdirs(); // Cria as pastas config -> skylake -> treasures se não existirem
        }
        return dir;
    }

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

    public static void log(BlockPos pos, String fileName) {
        String path = fileName;
        String coordKey = pos.getX() + "," + pos.getY() + "," + pos.getZ();

        Set<String> currentCoords = fileCache.computeIfAbsent(path, k -> loadFileSet(path));

        if (!currentCoords.contains(coordKey)) {
            currentCoords.add(coordKey);
            saveToFile(currentCoords, path);
        }
    }

    public static void log(BlockPos pos) {
        log(pos, "skylake_treasures.json");
    }

    private static Set<String> loadFileSet(String path) {
        Set<String> resultSet = new HashSet<>();

        // Usa o getTreasuresDir() para garantir que a pasta existe antes de ler
        File configFile = new File(getTreasuresDir(), path);

        if (configFile.exists()) {
            try (Reader reader = new FileReader(configFile)) {
                Set<String> loaded = gson.fromJson(reader, new TypeToken<HashSet<String>>(){}.getType());
                if (loaded != null) resultSet.addAll(loaded);
            } catch (IOException e) { e.printStackTrace(); }
        }

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
        File configFile = new File(getTreasuresDir(), path);
        try {
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

    public static void clearLog(String fileName) {
        fileCache.put(fileName, new HashSet<>());
        saveToFile(new HashSet<>(), fileName);
    }

    public static void markAsCollected(String fileName, BlockPos pos) {
        String collectedFile = fileName.replace(".json", "_coletados.json");
        System.out.println("[SkyLake DEBUG] Tentando marcar como coletado: " + pos + " no arquivo: " + collectedFile);
        
        File treasuresDir = getTreasuresDir();
        System.out.println("[SkyLake DEBUG] Diretório de tesouros: " + treasuresDir.getAbsolutePath());
        System.out.println("[SkyLake DEBUG] Diretório existe: " + treasuresDir.exists());
        
        List<BlockPos> collected = getCoordsFromFile(collectedFile);
        System.out.println("[SkyLake DEBUG] Coletados antes: " + collected.size());

        if (!collected.contains(pos)) {
            collected.add(pos);
            saveCoordsToFile(collectedFile, collected);
            System.out.println("[SkyLake DEBUG] Tesouro adicionado e salvo!");
            
            // Verificar se o arquivo foi criado
            File savedFile = new File(treasuresDir, collectedFile);
            System.out.println("[SkyLake DEBUG] Arquivo salvo existe: " + savedFile.exists());
            System.out.println("[SkyLake DEBUG] Caminho completo: " + savedFile.getAbsolutePath());
        } else {
            System.out.println("[SkyLake DEBUG] Tesouro já estava na lista de coletados");
        }
    }

    public static void resetCollected(String fileName) {
        String collectedFile = fileName.replace(".json", "_coletados.json");
        // Usa o getTreasuresDir()
        File file = new File(getTreasuresDir(), collectedFile);
        if (file.exists()) file.delete();
    }

    private static void saveCoordsToFile(String fileName, List<BlockPos> coords) {
        // Usa o getTreasuresDir()
        File file = new File(getTreasuresDir(), fileName);
        try (FileWriter writer = new FileWriter(file)) {
            List<String> stringCoords = new ArrayList<>();
            for (BlockPos p : coords) stringCoords.add(p.getX() + "," + p.getY() + "," + p.getZ());
            gson.toJson(stringCoords, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
