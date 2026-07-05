package com.galaxyhells.skylake.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.BlockPos;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MutantSpawnDataManager {
    private static final Gson gson = new GsonBuilder().create();
    private static List<BlockPos> spawnLocations = null;
    
    /**
     * Carrega as coordenadas de spawn do Enderman Mutante do arquivo JSON
     */
    public static List<BlockPos> getSpawnLocations() {
        if (spawnLocations != null) {
            return spawnLocations;
        }
        
        spawnLocations = new ArrayList<>();
        
        try {
            InputStream is = MutantSpawnDataManager.class.getResourceAsStream("/assets/skylake/data/mutant_spawn_locations.json");
            if (is != null) {
                try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                    List<LocationData> locations = gson.fromJson(reader, new TypeToken<ArrayList<LocationData>>(){}.getType());
                    if (locations != null) {
                        for (LocationData loc : locations) {
                            spawnLocations.add(new BlockPos(
                                Math.round(loc.x),
                                Math.round(loc.y),
                                Math.round(loc.z)
                            ));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[SkyLake] Erro ao carregar localizações de spawn do mutante: " + e.getMessage());
        }
        
        return spawnLocations;
    }
    
    private static class LocationData {
        double x;
        double y;
        double z;
    }
}
