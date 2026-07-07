package com.galaxyhells.skylake.config;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.OptionType;
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

        // Low Health Warning
        this.buttonList.add(new GuiButton(15, x, yStart + 75, 200, 20, getLowHealthWarningText()));

        // Mutant Timer
        this.buttonList.add(new GuiButton(4, x, yStart + 100, 200, 20, getMutantTimerText()));

        // Magma Timer
        this.buttonList.add(new GuiButton(5, x, yStart + 125, 200, 20, getMagmaTimerText()));

        // Mutant Highlight
        this.buttonList.add(new GuiButton(6, x, yStart + 150, 200, 20, getMutanthighlightText()));

        // Mutant Spawn Boxes
        this.buttonList.add(new GuiButton(7, x, yStart + 175, 200, 20, getMutantSpawnBoxesText()));

        // Fancy HUD
        this.buttonList.add(new GuiButton(8, x, yStart + 200, 200, 20, getFancyHUDText()));

        // Map Feature
//        this.buttonList.add(new GuiButton(9, x, yStart + 225, 200, 20, getMapFeatureText()));

        // Fancy Stat Overlay
        this.buttonList.add(new GuiButton(10, x, yStart + 250, 200, 20, getFancyStatOverlayText()));

        // Auto Sprint
        this.buttonList.add(new GuiButton(11, x, yStart + 275, 200, 20, getAutoSprintText()));

        // Auto Login
        this.buttonList.add(new GuiButton(12, x, yStart + 300, 200, 20, getAutoLoginText()));

        // Auto Fishing
        this.buttonList.add(new GuiButton(13, x, yStart + 325, 200, 20, getAutoFishingText()));

        // Auto Attack
        this.buttonList.add(new GuiButton(14, x, yStart + 350, 200, 20, getAutoAttackText()));

        // Magma Drop Announcer
        this.buttonList.add(new GuiButton(16, x, yStart + 375, 200, 20, getMagmaDropAnnouncerText()));
    }

    private String getBossAlertText() {
        // Lê do novo OptionsService
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.BOSS_ALERT));
        return "Alerta de spawn Enderman Mutante: " + (value ? "§aON" : "§cOFF");
    }

    private String getRarityText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.RARITY_BACKGROUND));
        return "Cor de fundo do item: " + (value ? "§aON" : "§cOFF");
    }

    private String getStatsText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.STAT_OVERLAY));
        return "HUD de Stats: " + (value ? "§aON" : "§cOFF");
    }

    private String getLowHealthWarningText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.LOW_HEALTH_WARNING));
        return "Alerta de Vida Baixa: " + (value ? "§aON" : "§cOFF");
    }

    private String getMutantTimerText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MUTANT_TIMER));
        return "Contagem pro Enderman Mutante: " + (value ? "§aON" : "§cOFF");
    }

    private String getMagmaTimerText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MAGMA_TIMER));
        return "Contagem pro Magma Boss: " + (value ? "§aON" : "§cOFF");
    }

    private String getMutanthighlightText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MUTANT_HIGHLIGHT));
        return "Destacar Enderman Mutante: " + (value ? "§aON" : "§cOFF");
    }

    
    private String getMutantSpawnBoxesText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MUTANT_SPAWN_BOXES));
        return "Caixas de spawn do mutante: " + (value ? "§aON" : "§cOFF");
    }

    private String getFancyHUDText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.FANCY_HUD));
        return "Hotbar Maravilhosa: " + (value ? "§aON" : "§cOFF");
    }

//    private String getMapFeatureText() {
//        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MAP_FEATURE));
//        return "Mapa (Tecla M): " + (value ? "§aON" : "§cOFF");
//    }

    private String getFancyStatOverlayText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.FANCY_STAT_OVERLAY));
        return "Stat Maravilhoso: " + (value ? "§aON" : "§cOFF");
    }

    private String getAutoSprintText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_SPRINT));
        return "Auto Sprint: " + (value ? "§aON" : "§cOFF");
    }

    private String getAutoLoginText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_LOGIN));
        return "Auto Login: " + (value ? "§aON" : "§cOFF");
    }

    
    private String getAutoFishingText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_FISHING));
        return "Pescaria Automática: " + (value ? "§aON" : "§cOFF");
    }

    private String getAutoAttackText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_ATTACK));
        return "Ataque Automático: " + (value ? "§aON" : "§cOFF");
    }

    private String getMagmaDropAnnouncerText() {
        boolean value = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MAGMA_DROP_ANNOUNCER));
        return "Anunciador Drops Magma Boss: " + (value ? "§aON" : "§cOFF");
    }

    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.BOSS_ALERT));
            SkyLake.optionsService.set(OptionType.BOSS_ALERT, !currentValue);
            button.displayString = getBossAlertText();
        } else if (button.id == 2) {
            // Alterna a nova função
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.RARITY_BACKGROUND));
            SkyLake.optionsService.set(OptionType.RARITY_BACKGROUND, !currentValue);
            button.displayString = getRarityText();
        } else if (button.id == 3) {
            // Alterna o HUD de Vida e Mana
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.STAT_OVERLAY));
            SkyLake.optionsService.set(OptionType.STAT_OVERLAY, !currentValue);
            button.displayString = getStatsText();
        } else if (button.id == 4) {
            // Alterna o display do Tempo pro Mutante
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MUTANT_TIMER));
            SkyLake.optionsService.set(OptionType.MUTANT_TIMER, !currentValue);
            button.displayString = getMutantTimerText();
        } else if (button.id == 5) {
            // Alterna o display do Tempo pro Magma Boss
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MAGMA_TIMER));
            SkyLake.optionsService.set(OptionType.MAGMA_TIMER, !currentValue);
            button.displayString = getMagmaTimerText();
        } else if (button.id == 6) {
            // Alterna o destaque do Enderman Mutante
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MUTANT_HIGHLIGHT));
            SkyLake.optionsService.set(OptionType.MUTANT_HIGHLIGHT, !currentValue);
            button.displayString = getMutanthighlightText();
        } else if (button.id == 7) {
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MUTANT_SPAWN_BOXES));
            SkyLake.optionsService.set(OptionType.MUTANT_SPAWN_BOXES, !currentValue);
            button.displayString = getMutantSpawnBoxesText();
        } else if (button.id == 8) {
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.FANCY_HUD));
            SkyLake.optionsService.set(OptionType.FANCY_HUD, !currentValue);
            button.displayString = getFancyHUDText();
        } else if (button.id == 9) {
//            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MAP_FEATURE));
//            SkyLake.optionsService.set(OptionType.MAP_FEATURE, !currentValue);
//            button.displayString = getMapFeatureText();
        } else if (button.id == 10) {
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.FANCY_STAT_OVERLAY));
            SkyLake.optionsService.set(OptionType.FANCY_STAT_OVERLAY, !currentValue);
            button.displayString = getFancyStatOverlayText();
        } else if (button.id == 11) {
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_SPRINT));
            SkyLake.optionsService.set(OptionType.AUTO_SPRINT, !currentValue);
            button.displayString = getAutoSprintText();
        } else if (button.id == 12) {
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_LOGIN));
            SkyLake.optionsService.set(OptionType.AUTO_LOGIN, !currentValue);
            button.displayString = getAutoLoginText();
        } else if (button.id == 13) {
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_FISHING));
            SkyLake.optionsService.set(OptionType.AUTO_FISHING, !currentValue);
            button.displayString = getAutoFishingText();
        } else if (button.id == 14) {
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_ATTACK));
            SkyLake.optionsService.set(OptionType.AUTO_ATTACK, !currentValue);
            button.displayString = getAutoAttackText();
        } else if (button.id == 15) {
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.LOW_HEALTH_WARNING));
            SkyLake.optionsService.set(OptionType.LOW_HEALTH_WARNING, !currentValue);
            button.displayString = getLowHealthWarningText();
        } else if (button.id == 16) {
            boolean currentValue = Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.MAGMA_DROP_ANNOUNCER));
            SkyLake.optionsService.set(OptionType.MAGMA_DROP_ANNOUNCER, !currentValue);
            button.displayString = getMagmaDropAnnouncerText();
        }

        // Salva qualquer alteração no arquivo
        SkyLake.optionsService.save();
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
