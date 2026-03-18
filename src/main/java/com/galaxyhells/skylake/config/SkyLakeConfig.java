package com.galaxyhells.skylake.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;

public class SkyLakeConfig extends GuiScreen {

    @Override
    public void initGui() {
        int x = this.width / 2 - 100;
        int yStart = this.height / 2 - 40;

        // Boss Alert
        this.buttonList.add(new GuiButton(1, x, yStart, 200, 20, getBossAlertText()));

        // Rarity Background
        this.buttonList.add(new GuiButton(2, x, yStart + 25, 200, 20, getRarityText()));

        // Stat Overlay
        this.buttonList.add(new GuiButton(3, x, yStart + 50, 200, 20, getStatsText()));
    }

    private String getBossAlertText() {
        // Lê diretamente do Handler para evitar dessincronização
        return "Boss Alert: " + (ConfigHandler.bossAlert ? "§aON" : "§cOFF");
    }

    private String getRarityText() {
        return "Rarity Background: " + (ConfigHandler.rarityBackground ? "§aON" : "§cOFF");
    }

    private String getStatsText() {
        return "Stats HUD: " + (ConfigHandler.statOverlay ? "§aON" : "§cOFF");
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