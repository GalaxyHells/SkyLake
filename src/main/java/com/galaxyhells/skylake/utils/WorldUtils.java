package com.galaxyhells.skylake.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WorldUtils {

    // 1. Lista de áreas válidas que o seu mod conhece.
    // Você pode adicionar novas áreas aqui no futuro!
    private static final List<String> KNOWN_AREAS = Arrays.asList(
            "vilarejo", "covil_das_aranhas", "nether", "grande_mina",
            "floresta_de_selva", "selva", "betula", "caminho_dos_eucaliptos",
            "floresta_de_taiga", "taiga"
    );

    public static String getCurrentArea() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.theWorld.getScoreboard() == null) return "desconhecido";

        Scoreboard sb = mc.theWorld.getScoreboard();
        ScoreObjective obj = sb.getObjectiveInDisplaySlot(1);

        if (obj == null) return "desconhecido";

        Collection<Score> scores = sb.getSortedScores(obj);

        // 2. Vasculha TODAS as linhas do scoreboard, não importa a posição
        for (Score score : scores) {
            ScorePlayerTeam team = sb.getPlayersTeam(score.getPlayerName());

            // Reconstrói o texto da linha
            String rawLine = (team != null) ? team.getColorPrefix() + score.getPlayerName() + team.getColorSuffix() : score.getPlayerName();

            // Limpa as cores (ex: §a)
            String cleanedLine = EnumChatFormatting.getTextWithoutFormattingCodes(rawLine).trim();

            // Remove símbolos esquisitos do começo (ex: "⏣ Vilarejo" -> "Vilarejo")
            cleanedLine = cleanedLine.replaceAll("^[^a-zA-Z0-9áéíóúÁÉÍÓÚçÇ]+", "").trim();

            // Normaliza para o formato das pastas (ex: "Covil das Aranhas" -> "covil_das_aranhas")
            String normalizedLine = cleanedLine.toLowerCase()
                    .replace(" ", "_")
                    .replaceAll("[^a-z0-9_]", "");

            // 3. Verifica se a linha limpa é uma das áreas que conhecemos
            if (KNOWN_AREAS.contains(normalizedLine)) {
                return formatAreaName(normalizedLine); // Achou! Retorna o nome formatado.
            }
        }

        return "desconhecido";
    }

    // 4. Mantemos o Switch separado para organizar as renomeações que você fez nos arquivos
    private static String formatAreaName(String folderName) {
        switch (folderName) {
            case "caminho_dos_eucaliptos":
                return "eucalipto";
            case "floresta_de_selva":
                return "selva";
            case "floresta_de_taiga":
                return "taiga";
            default:
                return folderName;
        }
    }
}