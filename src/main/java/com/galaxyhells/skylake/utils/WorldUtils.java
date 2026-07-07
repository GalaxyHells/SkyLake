package com.galaxyhells.skylake.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class WorldUtils {

    private static final List<String> KNOWN_AREAS = Arrays.asList(
            "vilarejo", "covil_das_aranhas", "nether", "grande_mina",
            "floresta_de_selva", "selva", "betula", "caminho_dos_eucaliptos",
            "floresta_de_taiga", "taiga"
    );

    // Agora precisamos de apenas 2 variáveis para o Mascote
    private static String mascotLevel = "";
    private static String mascotExp = "";
    private static long lastMascoteUpdate = 0;
    private static final long MASCOTE_UPDATE_INTERVAL = 1000; // Update every 1 second

    public static String getCurrentArea() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.theWorld.getScoreboard() == null) return "desconhecido";

        Scoreboard sb = mc.theWorld.getScoreboard();
        ScoreObjective obj = sb.getObjectiveInDisplaySlot(1);

        if (obj == null) return "desconhecido";

        Collection<Score> scores = sb.getSortedScores(obj);

        for (Score score : scores) {
            ScorePlayerTeam team = sb.getPlayersTeam(score.getPlayerName());
            String rawLine = (team != null) ? team.getColorPrefix() + score.getPlayerName() + team.getColorSuffix() : score.getPlayerName();
            String cleanedLine = EnumChatFormatting.getTextWithoutFormattingCodes(rawLine).trim();
            cleanedLine = cleanedLine.replaceAll("^[^a-zA-Z0-9áéíóúÁÉÍÓÚçÇ]+", "").trim();

            String normalizedLine = cleanedLine.toLowerCase()
                    .replace(" ", "_")
                    .replaceAll("[^a-z0-9_]", "");

            if (KNOWN_AREAS.contains(normalizedLine)) {
                return formatAreaName(normalizedLine);
            }
        }
        return "desconhecido";
    }

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

    public static String getCoinsFromScoreboard() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.theWorld.getScoreboard() == null) return "0";

        Scoreboard sb = mc.theWorld.getScoreboard();
        ScoreObjective obj = sb.getObjectiveInDisplaySlot(1);

        if (obj == null) return "0";

        Collection<Score> scores = sb.getSortedScores(obj);

        for (Score score : scores) {
            ScorePlayerTeam team = sb.getPlayersTeam(score.getPlayerName());
            String rawLine = (team != null) ? team.getColorPrefix() + score.getPlayerName() + team.getColorSuffix() : score.getPlayerName();
            String cleanedLine = EnumChatFormatting.getTextWithoutFormattingCodes(rawLine).trim();

            if (cleanedLine.startsWith("Carteira:")) {
                String rawValue = cleanedLine.substring("Carteira:".length()).trim();

                // Divide na vírgula e retorna apenas a primeira parte (o número com pontos, sem decimais)
                return rawValue.split(",")[0];
            }
        }
        return "0";
    }

    public static String getCubosFromScoreboard() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.theWorld.getScoreboard() == null) return "0";

        Scoreboard sb = mc.theWorld.getScoreboard();
        ScoreObjective obj = sb.getObjectiveInDisplaySlot(1);

        if (obj == null) return "0";

        Collection<Score> scores = sb.getSortedScores(obj);

        for (Score score : scores) {
            ScorePlayerTeam team = sb.getPlayersTeam(score.getPlayerName());
            String rawLine = (team != null) ? team.getColorPrefix() + score.getPlayerName() + team.getColorSuffix() : score.getPlayerName();
            String cleanedLine = EnumChatFormatting.getTextWithoutFormattingCodes(rawLine).trim();

            if (cleanedLine.startsWith("Cubos:")) {
                return cleanedLine.substring("Cubos:".length()).trim();
            }
        }
        return "0";
    }

    public static void updateMascoteData() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMascoteUpdate < MASCOTE_UPDATE_INTERVAL) {
            return;
        }
        lastMascoteUpdate = currentTime;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) {
            return;
        }

        try {
            Collection<NetworkPlayerInfo> players = mc.getNetHandler().getPlayerInfoMap();
            java.util.TreeMap<String, String> sortedTablist = new java.util.TreeMap<>();

            for (NetworkPlayerInfo playerInfo : players) {
                String profileName = playerInfo.getGameProfile().getName();
                String text = "";

                if (playerInfo.getDisplayName() != null) {
                    text = playerInfo.getDisplayName().getFormattedText();
                } else {
                    ScorePlayerTeam team = mc.theWorld.getScoreboard().getPlayersTeam(profileName);
                    if (team != null) {
                        text = ScorePlayerTeam.formatPlayerName(team, profileName);
                    }
                }

                if (text != null && !text.trim().isEmpty()) {
                    sortedTablist.put(profileName, text.trim());
                }
            }

            java.util.List<String> tablistLines = new java.util.ArrayList<>(sortedTablist.values());

            mascotLevel = "";
            mascotExp = "";

            for (int i = 0; i < tablistLines.size(); i++) {
                String rawLine = tablistLines.get(i);
                String cleanedText = EnumChatFormatting.getTextWithoutFormattingCodes(rawLine).trim();

                if (cleanedText.toUpperCase().contains("MASCOTE")) {

                    if (i + 1 < tablistLines.size()) {
                        mascotLevel = tablistLines.get(i + 1);
                    }

                    if (i + 2 < tablistLines.size()) {
                        String rawExpLine = tablistLines.get(i + 2);

                        // LÓGICA DE FORMATAÇÃO DA EXP
                        if (rawExpLine.contains("/")) {
                            // CORREÇÃO AQUI: FontRenderer extrai os códigos de cor perfeitamente na 1.8.9
                            String colorPrefix = mc.fontRendererObj.getFormatFromString(rawExpLine);
                            String cleanExp = EnumChatFormatting.getTextWithoutFormattingCodes(rawExpLine).trim();

                            String[] parts = cleanExp.split("/");
                            if (parts.length == 2) {
                                String currentStr = parts[0].replaceAll("\\.\\d+", "").replaceAll("[^0-9]", "");
                                String maxStr = parts[1].replaceAll("\\.\\d+", "").replaceAll("[^0-9]", "");

                                try {
                                    long currentLong = Long.parseLong(currentStr);
                                    long maxLong = Long.parseLong(maxStr);

                                    java.text.DecimalFormat df = (java.text.DecimalFormat) java.text.NumberFormat.getInstance(new java.util.Locale("pt", "BR"));

                                    mascotExp = colorPrefix + df.format(currentLong) + "/" + df.format(maxLong);
                                } catch (NumberFormatException e) {
                                    mascotExp = rawExpLine;
                                }
                            } else {
                                mascotExp = rawExpLine;
                            }
                        } else {
                            mascotExp = rawExpLine;
                        }
                    }

                    break;
                }
            }

        } catch (Exception e) {
            mascotLevel = "";
            mascotExp = "";
        }
    }

    public static String getMascoteLevel() {
        updateMascoteData();
        return mascotLevel;
    }

    public static String getMascoteExp() {
        updateMascoteData();
        return mascotExp;
    }

    public static boolean hasMascoteData() {
        updateMascoteData();
        return !mascotLevel.isEmpty() && !mascotExp.isEmpty();
    }
}