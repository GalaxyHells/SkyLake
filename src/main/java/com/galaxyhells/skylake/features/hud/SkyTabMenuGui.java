package com.galaxyhells.skylake.features.hud;

import com.galaxyhells.skylake.utils.ActionbarParser; // Reusing your existing data parser
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;

public class SkyTabMenuGui extends GuiScreen {

    private static final String[][] WARPS_DATA = {
            {"§aVilarejo", "emerald_block", "spawn"},
            {"§eIlha", "grass", "is"},
            {"§6Fazenda", "hay_block", "warp fazenda"},
            {"§6Cânion", "sand", "warp cânion"},
            {"§7Mina", "iron_pickaxe", "warp mina"},
            {"§2Selva", "sapling", "warp selva"},
            {"§2Eucalipto", "sapling", "warp caminhodoseucaliptos"},
            {"§2Taiga", "sapling", "warp taiga"},
            {"§2Escura", "sapling", "warp florestaescura"},
            {"§2Acácia", "sapling", "warp savana"},
            {"§5Aranhas", "string", "warp covildasaranhas"},
            {"§cNether", "nether_brick", "warp nether"},
            {"§1O Fim", "ender_eye", "warp end"}
    };

    // Unique IDs for the three main buttons
    private static final int BTN_TELEPORTS = 0;
    private static final int BTN_SOON_1 = 1;
    private static final int BTN_SOON_2 = 2;
    // 0 = Principal, 1 = Teleportes
    private int currentPage = 0;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        ScaledResolution sr = new ScaledResolution(mc);
        int screenW = sr.getScaledWidth();
        int screenH = sr.getScaledHeight();

        // Configurações de tamanho (80% da tela)
        int menuW = (int) (screenW * 0.8);
        int menuH = (int) (screenH * 0.8);
        int x = (screenW - menuW) / 2;
        int y = (screenH - menuH) / 2;
        int margin = 4;

        // Fundo e Título
        drawMenuFrame(x, y, menuW, menuH, 0xCC111111);
        String title = currentPage == 0 ? "§6SkyLake §fMenu" : "§bSkyLake §fTeleportes";
        this.fontRendererObj.drawStringWithShadow(title, x + menuW / 2 - this.fontRendererObj.getStringWidth(title) / 2, y + 12, 0xFFFFFF);

        // Área útil dos botões
        int innerX = x + margin;
        int innerY = y + 40;
        int innerW = menuW - (margin * 2);
        int innerH = menuH - 40 - margin;
        int gap = 2;
        int buttonW = (innerW - (gap * 2)) / 3;

        // --- RENDERIZAÇÃO POR PÁGINA ---
        if (currentPage == 0) {
            // PÁGINA PRINCIPAL
            renderButton(0, innerX, innerY, buttonW, innerH, "§bTeleportes", Items.compass, mouseX, mouseY, "Warps rápidos...");
            renderButton(1, innerX + buttonW + gap, innerY, buttonW, innerH, "§7Em breve...", Items.writable_book, mouseX, mouseY, "Slayers e Skills");
            renderButton(2, innerX + (buttonW + gap) * 2, innerY, buttonW, innerH, "§7Em breve...", Item.getItemFromBlock(Blocks.anvil), mouseX, mouseY, "Minions e Craft");
        }
        else if (currentPage == 1) {
            int cols = 5; // Definido para 5 colunas
            int spacing = 4;
            // Cálculo exato para preencher a largura sem sobras
            int btnW = (innerW - (spacing * (cols - 1))) / cols;
            int btnH = 22;

            for (int i = 0; i < WARPS_DATA.length; i++) {
                int col = i % cols;
                int row = i / cols;
                int bx = innerX + (col * (btnW + spacing));
                int by = innerY + (row * (btnH + spacing));

                // Pega o item correto (Tratando se é bloco ou item)
                ItemStack icon;
//                if (warps[i][1].equals("jungle_door")) icon = new ItemStack(Items.jungle_door);
//                else if (warps[i][1].equals("iron_pickaxe")) icon = new ItemStack(Items.iron_pickaxe);
//                else if (warps[i][1].equals("string")) icon = new ItemStack(Items.string);
//                else if (warps[i][1].equals("ender_eye")) icon = new ItemStack(Items.ender_eye);
//                else icon = new ItemStack(net.minecraft.block.Block.getBlockFromName(warps[i][1]));

                Block block = net.minecraft.block.Block.getBlockFromName(WARPS_DATA[i][1]);
                if (block != null) {
                    icon = new ItemStack(block);
                } else {
                    // Caso o bloco falhe, usa uma barreira ou pedra como reserva
                    icon = new ItemStack(net.minecraft.init.Blocks.stone);
                }

                drawSmallButton(bx, by, btnW, btnH, WARPS_DATA[i][0], icon, mouseX, mouseY);
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    // Método auxiliar para desenhar o botão e detectar hover
    private void renderButton(int id, int x, int y, int w, int h, String label, Item icon, int mx, int my, String desc) {
        boolean hovered = mx >= x && mx <= x + w && my >= y && my <= y + h;
        int color = hovered ? 0xFF333333 : 0xFF181818;

        Gui.drawRect(x, y, x + w, y + h, 0xFF333333); // Borda
        Gui.drawRect(x + 2, y + 2, x + w - 2, y + h - 2, color); // Fundo

        this.fontRendererObj.drawStringWithShadow(label, x + w / 2 - this.fontRendererObj.getStringWidth(label) / 2, y + 10, 0xFFFFFF);
        this.fontRendererObj.drawStringWithShadow("§8" + desc, x + 8, y + h - 15, 0xFFFFFF);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + w / 2, y + h / 2, 0);
        GlStateManager.scale(3.0F, 3.0F, 1.0F);
        this.itemRender.renderItemIntoGUI(new ItemStack(icon), -8, -8);
        GlStateManager.popMatrix();
    }

    // --- CUSTOM HELPER METHODS FOR APPEARANCE ---

    /**
     * Draws a textured frame around the entire menu area.
     */
    private void drawMenuFrame(int x, int y, int w, int h, int innerColor) {
        // Texture references from search results for stylized borders
        ResourceLocation BORDER_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/villager.png"); // Using Stone Brick style border parts
        mc.getTextureManager().bindTexture(BORDER_TEXTURE);

        // Custom 3D-styled dark beveled background
        Gui.drawRect(x, y, x + w, y + h, 0xFF222222); // Solid border color
        Gui.drawRect(x + 2, y + 2, x + w - 2, y + h - 2, 0xFF111111); // Darker inner edge
        Gui.drawRect(x + 4, y + 4, x + w - 4, y + h - 4, innerColor); // Your translucent background
    }

    /**
     * Draws a premium column-style button with a text header, an icon, a hover state, and description text.
     */
    private void drawMenuColumnButton(int x, int y, int w, int h, String label, net.minecraft.item.Item itemIcon, boolean hovered, String subText) {
        int color = hovered ? 0xFF333333 : 0xFF181818; // Change background on hover
        int textColor = hovered ? 0xFFFFFFFF : 0xFFAAAAAA;

        // --- BUTTON BASE & BORDER ---
        Gui.drawRect(x, y, x + w, y + h, 0xFF333333); // Border
        Gui.drawRect(x + 2, y + 2, x + w - 2, y + h - 2, color); // Main area

        // --- CONTENT LAYOUT ---
        // Header Text
        this.fontRendererObj.drawStringWithShadow(label, x + w / 2 - this.mc.fontRendererObj.getStringWidth(label) / 2, y + 10, 0xFFFFFF);

        // Subtext / Description
        int subTextY = y + 150; // Padding from top title and icon
        this.fontRendererObj.drawStringWithShadow(subText, x + 8, subTextY, textColor);

        // --- CENTERED ITEM ICON ---
        // We must push and pop matrix to scale/rotate standard items to act as icons
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + w / 2, y + h / 1.5, 0.0F); // Center of the button
        GlStateManager.scale(4.0F, 4.0F, 1.0F); // LARGE Icon scale
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F); // Tilt slightly
        this.itemRender.renderItemIntoGUI(new ItemStack(itemIcon), -8, -8); // Render centered at the new scaled position
        GlStateManager.popMatrix();
    }

    private void drawSmallButton(int x, int y, int w, int h, String label, ItemStack stack, int mx, int my) {
        if (stack == null || stack.getItem() == null) return;

        boolean hovered = mx >= x && mx <= x + w && my >= y && my <= y + h;
        // Fundo do botão
        Gui.drawRect(x, y, x + w, y + h, hovered ? 0xFF444444 : 0xFF181818);

        Gui.drawRect(x, y, x + w, y + 1, 0xFF333333);

        if (stack != null && stack.getItem() != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 4, y + (h / 2) - 8, 0);
            GlStateManager.scale(0.8F, 0.8F, 1.0F);
            this.itemRender.renderItemIntoGUI(stack, 0, 0);
            GlStateManager.popMatrix();
        }

        this.fontRendererObj.drawStringWithShadow(label, x + 22, y + (h / 2) - 4, 0xFFFFFF);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        ScaledResolution sr = new ScaledResolution(mc);
        int screenW = sr.getScaledWidth();
        int screenH = sr.getScaledHeight();

        // Recalculamos as dimensões base (iguais às do drawScreen)
        int menuW = (int) (screenW * 0.8);
        int menuH = (int) (screenH * 0.8);
        int x = (screenW - menuW) / 2;
        int y = (screenH - menuH) / 2;
        int margin = 4;

        int innerX = x + margin;
        int innerY = y + 40;
        int innerW = menuW - (margin * 2);
        int innerH = menuH - 40 - margin;

        int gap = 2;
        int btnW = (innerW - (gap * 2)) / 3; // Largura para os 3 botões grandes

        // Se clicar com o DIREITO, volta para a página principal
        if (mouseButton == 1) {
            currentPage = 0;
            return;
        }

        if (mouseButton == 0) { // Clique Esquerdo
            if (currentPage == 0) {
                // Clique no Botão 1 (Teleportes) - Ocupa o primeiro terço da largura
                if (mouseX >= innerX && mouseX <= innerX + btnW && mouseY >= innerY && mouseY <= innerY + innerH) {
                    currentPage = 1;
                    mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
                }
            }
            else if (currentPage == 1) {
                int cols = 5;
                int spacing = 4;
                int btnW2 = (innerW - (spacing * (cols - 1))) / cols;
                int btnH2 = 22;

                for (int i = 0; i < WARPS_DATA.length; i++) {
                    int col = i % cols;
                    int row = i / cols;
                    int bx = innerX + (col * (btnW2 + spacing));
                    int by = innerY + (row * (btnH2 + spacing));

                    if (mouseX >= bx && mouseX <= bx + btnW2 && mouseY >= by && mouseY <= by + btnH2) {
                        // Usa o comando definido na terceira coluna da global
                        mc.thePlayer.sendChatMessage("/" + WARPS_DATA[i][2]);
                        mc.displayGuiScreen(null);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}