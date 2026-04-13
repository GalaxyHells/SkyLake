package com.galaxyhells.skylake.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;

public class SkyLakeConfig extends GuiScreen {

    @Override
    public void initGui() {
        int x = this.width / 2 - 100;
        int yStart = this.height / 2 - 120;

        // Boss Alert
        this.buttonList.add(new GuiButton(1, x, yStart, 200, 20, getBossAlertText()));

        // Rarity Background
        this.buttonList.add(new GuiButton(2, x, yStart + 25, 200, 20, getRarityText()));

        // Stat Overlay
        this.buttonList.add(new GuiButton(3, x, yStart + 50, 200, 20, getStatsText()));

        // Mutant Timer
        this.buttonList.add(new GuiButton(4, x, yStart + 75, 200, 20, getMutantTimerText()));

        // Magma Timer
        this.buttonList.add(new GuiButton(5, x, yStart + 100, 200, 20, getMagmaTimerText()));

        // Mutant Highlight
        this.buttonList.add(new GuiButton(6, x, yStart + 125, 200, 20, getMutanthighlightText()));

        // AFK Feature
        this.buttonList.add(new GuiButton(7, x, yStart + 150, 200, 20, getAFKFeatureText()));

        // Mutant Spawn Boxes
        this.buttonList.add(new GuiButton(8, x, yStart + 175, 200, 20, getMutantSpawnBoxesText()));

        // Fancy HUD
        this.buttonList.add(new GuiButton(9, x, yStart + 200, 200, 20, getFancyHUDText()));

        // Map Feature
        this.buttonList.add(new GuiButton(10, x, yStart + 225, 200, 20, getMapFeatureText()));

        // Fancy Stat Overlay
        this.buttonList.add(new GuiButton(11, x, yStart + 250, 200, 20, getFancyStatOverlayText()));

        // Auto Sprint
        this.buttonList.add(new GuiButton(12, x, yStart + 275, 200, 20, getAutoSprintText()));

        // Auto Login
        this.buttonList.add(new GuiButton(13, x, yStart + 300, 200, 20, getAutoLoginText()));
    }

    private String getBossAlertText() {
        // Lê diretamente do Handler para evitar dessincronização
        return "Alerta de spawn Enderman Mutante: " + (ConfigHandler.bossAlert ? "§aON" : "§cOFF");
    }

    private String getRarityText() {
        return "Cor de fundo do item: " + (ConfigHandler.rarityBackground ? "§aON" : "§cOFF");
    }

    private String getStatsText() {
        return "HUD de Stats: " + (ConfigHandler.statOverlay ? "§aON" : "§cOFF");
    }

    private String getMutantTimerText() {
        return "Contagem pro Enderman Mutante: " + (ConfigHandler.mutantTimer ? "§aON" : "§cOFF");
    }

    private String getMagmaTimerText() {
        return "Contagem pro Magma Boss: " + (ConfigHandler.magmaTimer ? "§aON" : "§cOFF");
    }

    private String getMutanthighlightText() {
        return "Destacar Enderman Mutante: " + (ConfigHandler.mutantHighlight ? "§aON" : "§cOFF");
    }

    private String getAFKFeatureText() {

        return "AFK Automático: " + (ConfigHandler.afkFeature ? "§aON" : "§cOFF");
    }

    private String getMutantSpawnBoxesText() {
        return "Caixas de spawn do mutante: " + (ConfigHandler.mutantSpawnBoxes ? "§aON" : "§cOFF");
    }

    private String getFancyHUDText() {
        return "HUD Maravilhosa: " + (ConfigHandler.fancyHUD ? "§aON" : "§cOFF");
    }

    private String getMapFeatureText() {
        return "Mapa (Tecla M): " + (ConfigHandler.mapFeature ? "§aON" : "§cOFF");
    }

    private String getFancyStatOverlayText() {
        return "Stat Maravilhoso: " + (ConfigHandler.fancyStatOverlay ? "§aON" : "§cOFF");
    }

    private String getAutoSprintText() {
        return "Auto Sprint: " + (ConfigHandler.autoSprint ? "§aON" : "§cOFF");
    }

    private String getAutoLoginText() {
        return "Auto Login: " + (ConfigHandler.autoLogin ? "§aON" : "§cOFF");
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            ConfigHandler.bossAlert = !ConfigHandler.bossAlert;
            button.displayString = getBossAlertText();
        } else if (button.id == 2) {
            // Alterna a nova função
            ConfigHandler.rarityBackground = !ConfigHandler.rarityBackground;
            button.displayString = getRarityText();
        } else if (button.id == 3) {
            // Alterna o HUD de Vida e Mana
            ConfigHandler.statOverlay = !ConfigHandler.statOverlay;
            button.displayString = getStatsText();
        } else if (button.id == 4) {
            // Alterna o display do Tempo pro Mutante
            ConfigHandler.mutantTimer = !ConfigHandler.mutantTimer;
            button.displayString = getMutantTimerText();
        } else if (button.id == 5) {
            // Alterna o display do Tempo pro Magma Boss
            ConfigHandler.magmaTimer = !ConfigHandler.magmaTimer;
            button.displayString = getMagmaTimerText();
        } else if (button.id == 6) {
            // Alterna o destaque do Enderman Mutante
            ConfigHandler.mutantHighlight = !ConfigHandler.mutantHighlight;
            button.displayString = getMutanthighlightText();
        } else if (button.id == 7) {
            ConfigHandler.afkFeature = !ConfigHandler.afkFeature;
            button.displayString = getAFKFeatureText();
        } else if (button.id == 8) {
            ConfigHandler.mutantSpawnBoxes = !ConfigHandler.mutantSpawnBoxes;
            button.displayString = getMutantSpawnBoxesText();
        } else if (button.id == 9) {
            ConfigHandler.fancyHUD = !ConfigHandler.fancyHUD;
            button.displayString = getFancyHUDText();
        } else if (button.id == 10) {
            ConfigHandler.mapFeature = !ConfigHandler.mapFeature;
            button.displayString = getMapFeatureText();
        } else if (button.id == 11) {
            ConfigHandler.fancyStatOverlay = !ConfigHandler.fancyStatOverlay;
            button.displayString = getFancyStatOverlayText();
        } else if (button.id == 12) {
            ConfigHandler.autoSprint = !ConfigHandler.autoSprint;
            button.displayString = getAutoSprintText();
        } else if (button.id == 13) {
            ConfigHandler.autoLogin = !ConfigHandler.autoLogin;
            button.displayString = getAutoLoginText();
        }

        // Salva qualquer alteração no arquivo
        ConfigHandler.saveConfig();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "SkyLake Config", this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
