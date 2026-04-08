package com.galaxyhells.skylake.features.hud;

import com.galaxyhells.skylake.config.SkyLakeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;

public class SkyTabMenuGui extends GuiScreen {

    // Dados reorganizados: Ilha primeiro, depois agrupados por categoria
    private static final String[][] WARPS_DATA = {
            // Ilha (sempre primeira)
            {"§eIlha", "grass", "is"},
            
            // Biomas
            {"§2Eucalipto", "sapling", "warp caminhodoseucaliptos"},
            {"§2Taiga", "sapling", "warp taiga"},
            {"§2Escura", "sapling", "warp florestaescura"},
            {"§2Acácia", "sapling", "warp savana"},
            {"§2Selva", "sapling", "warp selva"},
            
            // Estruturas
            {"§aVilarejo", "emerald_block", "spawn"},
            {"§6Fazenda", "hay_block", "warp fazenda"},
            {"§6Cânion", "sand", "warp cânion"},
            {"§7Mina", "iron_pickaxe", "warp mina"},
            
            // Dimensões
            {"§5Aranhas", "string", "warp covildasaranhas"},
            {"§cNether", "nether_brick", "warp nether"},
            {"§1O Fim", "ender_eye", "warp end"}
    };

    // Unique IDs for three main buttons
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

        // Menu centralizado com margens responsivas
        int menuW = Math.min(600, screenW - 40);
        int menuH = Math.min(400, screenH - 40);
        int x = (screenW - menuW) / 2;
        int y = (screenH - menuH) / 2;
        int margin = 15;

        // Fundo e Título
        drawMenuFrame(x, y, menuW, menuH, 0xCC111111);
        String title = "";
        if (currentPage == 0) title = "§6SkyLake §fMenu";
        if (currentPage == 1) title = "§bSkyLake §fTeleportes";
        if (currentPage == 2) title = "§6SkyLake §fPreços";
        this.fontRendererObj.drawStringWithShadow(title, x + menuW / 2 - this.fontRendererObj.getStringWidth(title) / 2, y + 12, 0xFFFFFF);

        // Botão de configurações no topo direito
        drawConfigButton(x + menuW - 25, y + 8, mouseX, mouseY);

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
            renderButton(1, innerX + buttonW + gap, innerY, buttonW, innerH, "§aPreços", Items.writable_book, mouseX, mouseY, "Slayers e Skills");
            renderButton(2, innerX + (buttonW + gap) * 2, innerY, buttonW, innerH, "§7Em breve...", Item.getItemFromBlock(Blocks.anvil), mouseX, mouseY, "Minions e Craft");
        }
        else if (currentPage == 1) {
            int cols = 5; // Alterado para 5 botões horizontais
            int spacing = 4;
            int btnW = (innerW - (spacing * (cols - 1))) / cols;
            int btnH = (innerW - (spacing * (cols - 1))) / cols;
            btnH = (int)(btnH * 0.75);

            for (int i = 0; i < WARPS_DATA.length; i++) {
                int col = i % cols;
                int row = i / cols;
                int bx = innerX + (col * (btnW + spacing));
                int by = innerY + (row * (btnH + spacing));

                ItemStack icon;
                Block block = Block.getBlockFromName(WARPS_DATA[i][1]);
                if (block != null) {
                    icon = new ItemStack(block);
                } else {
                    icon = new ItemStack(Blocks.stone);
                }

                drawSmallButton(bx, by, btnW, btnH, WARPS_DATA[i][0], icon, mouseX, mouseY);
            }
        }
        else if (currentPage == 2) {
            // PÁGINA DE PREÇOS - DADOS REAIS DO REDECANARY
            int colGap = 6; // Espaçamento mínimo entre colunas
            int colW = ((innerW - (colGap * 3)) / 4); // 4 colunas ajustadas
            int curY = innerY;
            int spacing = 10;
            int smallSpacing = 8;
            
            // --- COLUNA 1: DRAGÕES & MINIONS ---
            drawCategoryTitle(innerX, curY, "§c§lDragões & Minions");
            curY += 12;
            
            // Dragões
            drawPriceLine(innerX, curY, "§5Pedra do Dragão", "§e5kk"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§5AOTD", "§e25kk"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§5Garra do Dragão", "§e3kk"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§5Pet Dragão", "§7???"); curY += spacing + 2;
            
            // Minions
            drawCategoryTitle(innerX, curY, "§e§lItens de Minions");
            curY += 12;
            drawPriceLine(innerX, curY, "§7Balde Lava", "§e300k"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§7Balde Lava Enc", "§e500k"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§7Balde de Magma", "§e1.2kk"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§7Compactador", "§e10k"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§7Super Compactador", "§e1kk"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§7Depósito Pequeno", "§e500 c"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§7Depósito Médio", "§e10k"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§7Depósito Grande", "§e300k"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§7Catalisador", "§e30k"); curY += smallSpacing;
            drawPriceLine(innerX, curY, "§7Fornalha de Fundição", "§e500 c"); curY += spacing;
            
            // --- COLUNA 2: MINI JARDIM ---
            int col2X = innerX + colW + colGap;
            curY = innerY;
            drawCategoryTitle(col2X, curY, "§a§lMini Jardim");
            curY += 12;
            
            // Livros
            drawPriceLine(col2X, curY, "§2Livro Hiper Nv 5", "§e750k"); curY += smallSpacing;
            drawPriceLine(col2X, curY, "§2 Batata Quente", "§e220k"); curY += smallSpacing;
            drawPriceLine(col2X, curY, "§2 Cenoura Quente", "§e350k"); curY += smallSpacing;
            drawPriceLine(col2X, curY, "§2Enxada Polinizada", "§e1.8kk"); curY += smallSpacing;
            drawPriceLine(col2X, curY, "§2Livro Hidromel 5", "§e7.5kk"); curY += smallSpacing;
            drawPriceLine(col2X, curY, "§2Salada de Frutas", "§e600k"); curY += smallSpacing;
            drawPriceLine(col2X, curY, "§2Buquê de Flores", "§e850k"); curY += smallSpacing;
            drawPriceLine(col2X, curY, "§2Cristal Iluminado", "§e1kk"); curY += smallSpacing;
            drawPriceLine(col2X, curY, "§2Colar de Girassol", "§e1.2kk"); curY += smallSpacing;
            drawPriceLine(col2X, curY, "§2Dente de Leão", "§e2kk"); curY += smallSpacing;
            drawPriceLine(col2X, curY, "§2Recombinador", "§e18.75kk"); curY += spacing + 2;
            
            // --- COLUNA 3: HORTALIÇAS & ARMADURAS ---
            int col3X = col2X + colW + colGap;
            curY = innerY;
            drawCategoryTitle(col3X, curY, "§6§lDrops & Armaduras");
            curY += 12;
            
            // Drops
            drawPriceLine(col3X, curY, "§eAdubo (pack)", "§e400k"); curY += smallSpacing;
            drawPriceLine(col3X, curY, "§eAdubo (unidade)", "§e6.25k"); curY += smallSpacing;
            drawPriceLine(col3X, curY, "§eRaiz (pack)", "§e38.4kk"); curY += smallSpacing;
            drawPriceLine(col3X, curY, "§eRaiz (unidade)", "§e600k"); curY += spacing + 2;
            
            // Armaduras Herbalismo
            drawCategoryTitle(col3X, curY, "§2§lArmaduras");
            curY += 12;
            drawPriceLine(col3X, curY, "§2Set Abóbora", "§e200k"); curY += smallSpacing;
            drawPriceLine(col3X, curY, "§2Set Broto", "§e1kk"); curY += smallSpacing;
            drawPriceLine(col3X, curY, "§2Set Raiz", "§e8.2kk"); curY += smallSpacing;
            drawPriceLine(col3X, curY, "§2Set Sol", "§e51.4kk"); curY += spacing;
            
            // --- COLUNA 4: ARCOS & ESPADAS ---
            int col4X = col3X + colW + colGap;
            curY = innerY;
            drawCategoryTitle(col4X, curY, "§b§lArcos & Espadas");
            curY += 12;
            
            // Arcos
            drawPriceLine(col4X, curY, "§9Apollo", "§e1kk"); curY += smallSpacing;
            drawPriceLine(col4X, curY, "§9Furação", "§e600k"); curY += spacing + 2;
            
            // Espadas
            drawCategoryTitle(col4X, curY, "§c§lEspadas");
            curY += 12;
            drawPriceLine(col4X, curY, "§cPigman", "§e15kk"); curY += smallSpacing;
            drawPriceLine(col4X, curY, "§cAspecto do Fim", "§e1kk"); curY += smallSpacing;
            drawPriceLine(col4X, curY, "§cEspada do Golem", "§e400k"); curY += smallSpacing;
            drawPriceLine(col4X, curY, "§cEspada Saltitante", "§e10kk"); curY += spacing;
            
            // Notas
            int footerY = y + menuH - 25;
            this.fontRendererObj.drawStringWithShadow("§7kk=milhão | k=mil | c=coins | Enc=Encantado", innerX + 10, footerY, 0xFFFFFF);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawMenuFrame(int x, int y, int w, int h, int borderColor) {
        Gui.drawRect(x, y, x + w, y + h, 0xFF222222);
        Gui.drawRect(x + 2, y + 2, x + w - 2, y + h - 2, 0xFF111111);
        Gui.drawRect(x + 4, y + 4, x + w - 4, y + h - 4, borderColor);
    }

    private void drawMenuColumnButton(int x, int y, int w, int h, String label, Item itemIcon, boolean hovered, String subText) {
        int color = hovered ? 0xFF333333 : 0xFF181818;
        int textColor = hovered ? 0xFFFFFFFF : 0xFFAAAAAA;

        Gui.drawRect(x, y, x + w, y + h, 0xFF333333);
        Gui.drawRect(x + 2, y + 2, x + w - 2, y + h - 2, color);

        this.fontRendererObj.drawStringWithShadow(label, x + w / 2 - this.mc.fontRendererObj.getStringWidth(label) / 2, y + 10, 0xFFFFFF);

        int subTextY = y + 150;
        this.fontRendererObj.drawStringWithShadow(subText, x + 8, subTextY, textColor);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + w / 2, y + h / 1.5, 0.0F);
        GlStateManager.scale(4.0F, 4.0F, 1.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        this.itemRender.renderItemIntoGUI(new ItemStack(itemIcon), -8, -8);
        GlStateManager.popMatrix();
    }

    private void drawSmallButton(int x, int y, int w, int h, String label, ItemStack stack, int mx, int my) {
        if (stack == null || stack.getItem() == null) return;

        boolean hovered = mx >= x && mx <= x + w && my >= y && my <= y + h;

        int hoverOffset = hovered ? 2 : 0;
        Gui.drawRect(x, y - hoverOffset, x + w, y + h - hoverOffset, hovered ? 0xFF444444 : 0xFF181818);
        Gui.drawRect(x, y - hoverOffset, x + w, y + 1 - hoverOffset, 0xFF333333);

        int textX = x + (w / 2) - (this.fontRendererObj.getStringWidth(label) / 2);
        int textY = y + 4 - hoverOffset;
        this.fontRendererObj.drawStringWithShadow(label, textX, textY, 0xFFFFFF);

        int iconSize = 32;
        int iconX = x + (w / 2) - (iconSize / 2);
        int iconY = y + (h / 2) - (iconSize / 2) + 4 - hoverOffset;

        GlStateManager.pushMatrix();
        float scale = 2.0F;
        GlStateManager.translate(iconX + iconSize/2, iconY + iconSize/2, 0.0F);
        GlStateManager.scale(scale, scale, 1.0F);
        this.itemRender.renderItemIntoGUI(stack, -8, -8);
        GlStateManager.popMatrix();
    }

    private void drawConfigButton(int x, int y, int mouseX, int mouseY) {
        int size = 16;
        boolean hovered = mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
        
        // Fundo do botão
        int color = hovered ? 0xFF555555 : 0xFF333333;
        Gui.drawRect(x, y, x + size, y + size, 0xFF222222);
        Gui.drawRect(x + 1, y + 1, x + size - 1, y + size - 1, color);
        
        // Ícone de redstone (engrenagem)
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + size/2, y + size/2, 0);
        GlStateManager.scale(0.8F, 0.8F, 1.0F);
        this.itemRender.renderItemIntoGUI(new ItemStack(Items.redstone), -8, -8);
        GlStateManager.popMatrix();
    }

    private void drawCategoryTitle(int x, int y, String title) {
        this.fontRendererObj.drawString(title, x, y, 0xFFFFFF);
        Gui.drawRect(x, y + 9, x + 80, y + 10, 0x50FFFFFF);
    }

    private void drawPriceLine(int x, int y, String label, String price) {
        this.fontRendererObj.drawString(label, x, y, 0xFFFFFF);
        int priceX = x + 105 - this.fontRendererObj.getStringWidth(price);
        this.fontRendererObj.drawString(price, priceX, y, 0xFFFFFF);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            ScaledResolution sr = new ScaledResolution(mc);
            int screenW = sr.getScaledWidth();
            int screenH = sr.getScaledHeight();

            int menuW = Math.min(600, screenW - 40);
            int menuH = Math.min(400, screenH - 40);
            int x = (screenW - menuW) / 2;
            int y = (screenH - menuH) / 2;
            int margin = 15;

            int innerX = x + margin;
            int innerY = y + 40;
            int innerW = menuW - (margin * 2);
            int innerH = menuH - 40 - margin;
            int gap = 2;
            int btnW = (innerW - (gap * 2)) / 3;

            if (mouseButton == 0) {
                // Clique no botão de configurações (topo direito)
                int cfgBtnX = x + menuW - 25;
                int cfgBtnY = y + 8;
                if (mouseX >= cfgBtnX && mouseX <= cfgBtnX + 16 && mouseY >= cfgBtnY && mouseY <= cfgBtnY + 16) {
                    mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
                    mc.displayGuiScreen(new SkyLakeConfig());
                    return;
                }
                
                if (currentPage == 0) {
                    if (mouseX >= innerX && mouseX <= innerX + btnW && mouseY >= innerY && mouseY <= innerY + innerH) {
                        currentPage = 1;
                        mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
                    }
                    else if (mouseX >= innerX + btnW + gap && mouseX <= innerX + (btnW * 2) + gap) {
                        currentPage = 2; // ABRE A ABA DE PREÇOS
                        mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro no mouseClicked: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (Exception e) {
            System.err.println("Erro no super.mouseClicked: " + e.getMessage());
            // Não propaga a exceção para evitar crash
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    private void renderButton(int id, int x, int y, int w, int h, String label, Item itemIcon, int mouseX, int mouseY, String desc) {
        boolean hovered = mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;

        int color = hovered ? 0xFF333333 : 0xFF181818;
        Gui.drawRect(x, y, x + w, y + h, 0xFF333333);
        Gui.drawRect(x + 2, y + 2, x + w - 2, y + h - 2, color);

        this.fontRendererObj.drawStringWithShadow(label, x + w / 2 - this.fontRendererObj.getStringWidth(label) / 2, y + 10, 0xFFFFFF);
        this.fontRendererObj.drawStringWithShadow("§8" + desc, x + 8, y + h - 15, 0xFFFFFF);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + w / 2, y + h / 2, 0);
        GlStateManager.scale(1.5F, 1.5F, 1.0F);
        this.itemRender.renderItemIntoGUI(new ItemStack(itemIcon), -8, -8);
        GlStateManager.popMatrix();
    }

    private final Minecraft mc = Minecraft.getMinecraft();
    private net.minecraft.client.renderer.entity.RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
}
