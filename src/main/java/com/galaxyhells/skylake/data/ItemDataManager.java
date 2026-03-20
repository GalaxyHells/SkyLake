package com.galaxyhells.skylake.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ItemDataManager {

    // A chave será o displayName do item (em minúsculo) e o valor será o sellPrice
    private static final Map<String, Double> sellPrices = new HashMap<>();

    /**
     * Carrega os preços do arquivo items.json localizado em src/main/resources/assets/skylake/data/items.json
     */
    public static void loadLocalPrices() {
        try {
            // Caminho para o arquivo dentro do seu mod
            ResourceLocation loc = new ResourceLocation("skylake", "data/items.json");

            // Abre e lê o arquivo usando o sistema de resources do Minecraft
            InputStreamReader reader = new InputStreamReader(
                    Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream(),
                    "UTF-8"
            );

            // Transforma o texto em um objeto JSON manipulável
            JsonObject json = new JsonParser().parse(reader).getAsJsonObject();

            // Limpa o cache caso o método seja chamado mais de uma vez (ex: comando de reload)
            sellPrices.clear();

            // Percorre todos os itens dentro do JSON
            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                JsonObject itemData = entry.getValue().getAsJsonObject();

                // Verifica se o item possui preço de venda E o nome de exibição ("displayName")
                if (itemData.has("sellPrice") && itemData.has("displayName")) {
                    double price = itemData.get("sellPrice").getAsDouble();
                    String displayName = itemData.get("displayName").getAsString();

                    // Salvamos o nome de exibição em minúsculo como a chave de busca
                    sellPrices.put(displayName.toLowerCase().trim(), price);
                }
            }

            reader.close();
            System.out.println("[SkyLake] Sucesso: " + sellPrices.size() + " preços carregados localmente.");
        } catch (Exception e) {
            System.err.println("[SkyLake] Erro ao ler items.json local: " + e.getMessage());
        }
    }

    /**
     * Limpa o nome do item removendo cores e prefixos (como quantidades no leilão)
     */
    private static String cleanItemName(String rawName) {
        if (rawName == null) return "";

        // 1. Remove códigos de cor do Minecraft (ex: §a, §c, §l)
        String clean = StringUtils.stripControlCodes(rawName);

        // 2. Remove o prefixo de quantidade usando Regex (ex: "1x ", "64x ")
        clean = clean.replaceAll("^\\d+x\\s+", "");

        // 3. Retorna tudo em minúsculo e sem espaços em branco nas pontas
        return clean.toLowerCase().trim();
    }

    /**
     * Encontra o preço baseado no nome de exibição "sujo" fazendo correspondência da maior substring
     */
    public static double getPriceByName(String rawDisplayName) {
        String cleanedName = cleanItemName(rawDisplayName);

        double bestPrice = 0.0;
        int longestMatchLength = 0;

        // Itera por todos os itens carregados na memória
        for (Map.Entry<String, Double> entry : sellPrices.entrySet()) {
            String apiItemName = entry.getKey(); // Nome original do JSON (já limpo e em minúsculo)

            // Verifica se o nome do item no jogo (já limpo) CONTÉM o nome do banco de dados
            // Isso garante que se o nome for "Espada de Diamante Profunda", ele encontre "Espada de Diamante"
            if (cleanedName.contains(apiItemName)) {

                // Se encontrarmos mais de um match, pegamos sempre o mais específico (maior texto)
                if (apiItemName.length() > longestMatchLength) {
                    longestMatchLength = apiItemName.length();
                    bestPrice = entry.getValue();
                }
            }
        }

        return bestPrice;
    }
}