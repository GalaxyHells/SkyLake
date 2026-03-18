package com.galaxyhells.skylake.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionbarParser {
    public static int currentHP = 100, maxHP = 100;
    public static int currentMana = 100, maxMana = 100;

    // Regex para capturar os padrões "852/852❤" e "55/55✎ Mana"
    private static final Pattern HP_PATTERN = Pattern.compile("(\\d+)/(\\d+)❤");
    private static final Pattern MANA_PATTERN = Pattern.compile("(\\d+)/(\\d+)✎");

    public static void parse(String text) {
        String cleanText = text.replaceAll("§[0-9a-fk-or]", ""); // Remove cores

        Matcher hpMatch = HP_PATTERN.matcher(cleanText);
        if (hpMatch.find()) {
            currentHP = Integer.parseInt(hpMatch.group(1));
            maxHP = Integer.parseInt(hpMatch.group(2));
        }

        Matcher manaMatch = MANA_PATTERN.matcher(cleanText);
        if (manaMatch.find()) {
            currentMana = Integer.parseInt(manaMatch.group(1));
            maxMana = Integer.parseInt(manaMatch.group(2));
        }
    }
}