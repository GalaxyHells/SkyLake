package com.galaxyhells.skylake.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;

public class SkyLakeConfig extends GuiScreen {

    @Override
    public void initGui() {
        // Usa o valor do ConfigHandler para o texto inicial
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 10, 200, 20, getBossAlertText()));
    }

    private String getBossAlertText() {
        // Lê diretamente do Handler para evitar dessincronização
        return "Boss Alert: " + (ConfigHandler.bossAlert ? "§aON" : "§cOFF");
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            ConfigHandler.bossAlert = !ConfigHandler.bossAlert;
            ConfigHandler.saveConfig();
            button.displayString = getBossAlertText();
        }
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