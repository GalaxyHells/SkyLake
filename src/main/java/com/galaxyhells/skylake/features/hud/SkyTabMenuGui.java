package com.galaxyhells.skylake.features.hud;

import com.galaxyhells.skylake.config.SkyLakeConfig;
import com.galaxyhells.skylake.data.PriceData;
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
            // PÁGINA DE TELEPORTES
            int cols = 5;
            int lines = 4;
            int spacing = 4;
            int btnW = (innerW - (spacing * (cols - 1))) / cols;
            int btnH = (innerH - (spacing * (lines - 1))) / lines;
            //btnH = (int)(btnH * 1.8); // Aumentado para botões mais altos

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
            // PÁGINA DE PREÇOS - 3 COLUNAS COLORIDAS
            int marginPrices = 2; // 2px das bordas
            int gapPrices = 2; // 2px entre colunas
            int colW = (innerW - (marginPrices * 2) - (gapPrices * 2)) / 3; // Largura de cada coluna
            
            // Coluna 1 - Cor Roxa
            int col1X = innerX + marginPrices;
            int col1Y = innerY + marginPrices;
            int col1H = innerH - (marginPrices * 2);
            // Fundo roxo mais transparente
            Gui.drawRect(col1X, col1Y, col1X + colW, col1Y + col1H, 0x22AA00FF);
            // Borda roxa
            Gui.drawRect(col1X, col1Y, col1X + colW, col1Y + 1, 0xFFAA00FF);
            Gui.drawRect(col1X, col1Y + col1H - 1, col1X + colW, col1Y + col1H, 0xFFAA00FF);
            Gui.drawRect(col1X, col1Y, col1X + 1, col1Y + col1H, 0xFFAA00FF);
            Gui.drawRect(col1X + colW - 1, col1Y, col1X + colW, col1Y + col1H, 0xFFAA00FF);
            
            // Título da coluna 1
            this.fontRendererObj.drawStringWithShadow("§c§lItens Dragão", col1X + 5, col1Y + 5, 0xFFFFFF);
            
            // Itens de dragão - Drops
            int itemY = col1Y + 25;
            this.fontRendererObj.drawStringWithShadow("§e§lDrops:", col1X + 5, itemY, 0xFFFFFF);
            itemY += 12;
            
            for (PriceData.ItemPrice item : PriceData.DragonItems.DROPS) {
                // Nome do item à esquerda
                this.fontRendererObj.drawStringWithShadow(item.name, col1X + 10, itemY, 0xFFFFFF);
                // Preço do item à direita
                int priceX = col1X + colW - 10 - this.fontRendererObj.getStringWidth(item.price);
                this.fontRendererObj.drawStringWithShadow(item.price, priceX, itemY, 0xFFFFFF);
                itemY += 10;
            }
            
            // Armaduras de dragão
            itemY += 10;
            this.fontRendererObj.drawStringWithShadow("§c§lArmaduras:", col1X + 5, itemY, 0xFFFFFF);
            itemY += 12;
            
            for (PriceData.ArmorSet armor : PriceData.DragonItems.ARMOR_SETS) {
                // Nome da armadura à esquerda
                this.fontRendererObj.drawStringWithShadow(armor.name, col1X + 5, itemY, 0xFFFFFF);
                // Preço completo
                String cleanPrice = armor.fullSetPrice.replace("~", "");
                int priceX = col1X + colW - 10 - this.fontRendererObj.getStringWidth(cleanPrice);
                this.fontRendererObj.drawStringWithShadow(cleanPrice, priceX, itemY, 0xFFFFFF);
                itemY += 10;
                
                // Primeira linha detalhada: Capacete e Calça
                String line1 = "Capacete: " + armor.helmetPrice.replace("~", "") + " | Calça: " + armor.leggingsPrice.replace("~", "");
                this.fontRendererObj.drawStringWithShadow("§7" + line1, col1X + 5, itemY, 0xFFFFFF);
                itemY += 10;
                
                // Segunda linha detalhada: Peitoral e Botas
                String line2 = "Peitoral: " + armor.chestplatePrice.replace("~", "") + " | Botas: " + armor.bootsPrice.replace("~", "");
                this.fontRendererObj.drawStringWithShadow("§7" + line2, col1X + 5, itemY, 0xFFFFFF);
                itemY += 12;
            }
            
            // Coluna 2 - Cor Verde
            int col2X = col1X + colW + gapPrices;
            int col2Y = innerY + marginPrices;
            int col2H = innerH - (marginPrices * 2);
            // Fundo verde
            Gui.drawRect(col2X, col2Y, col2X + colW, col2Y + col2H, 0x2200FF00);
            // Borda verde
            Gui.drawRect(col2X, col2Y, col2X + colW, col2Y + 1, 0xFF00FF00);
            Gui.drawRect(col2X, col2Y + col2H - 1, col2X + colW, col2Y + col2H, 0xFF00FF00);
            Gui.drawRect(col2X, col2Y, col2X + 1, col2Y + col2H, 0xFF00FF00);
            Gui.drawRect(col2X + colW - 1, col2Y, col2X + colW, col2Y + col2H, 0xFF00FF00);
            
            // Título da coluna 2
            this.fontRendererObj.drawStringWithShadow("§2§lHerbalismo", col2X + 5, col2Y + 5, 0xFFFFFF);
            
            // Itens da loja do mini jardim
            int itemY2 = col2Y + 25;
            this.fontRendererObj.drawStringWithShadow("§a§lLoja Mini Jardim:", col2X + 5, itemY2, 0xFFFFFF);
            itemY2 += 12;
            
            for (PriceData.ItemPrice item : PriceData.Herbalism.SHOP_ITEMS) {
                // Nome do item à esquerda
                this.fontRendererObj.drawStringWithShadow(item.name, col2X + 5, itemY2, 0xFFFFFF);
                // Preço do item à direita
                int priceX = col2X + colW - 10 - this.fontRendererObj.getStringWidth(item.price.replace("~", ""));
                this.fontRendererObj.drawStringWithShadow(item.price.replace("~", ""), priceX, itemY2, 0xFFFFFF);
                itemY2 += 10;
            }
            
            // Drops de herbalismo
            itemY2 += 10;
            this.fontRendererObj.drawStringWithShadow("§e§lDrops:", col2X + 5, itemY2, 0xFFFFFF);
            itemY2 += 12;
            
            for (PriceData.ItemPrice item : PriceData.Herbalism.DROPS) {
                // Nome do item à esquerda
                this.fontRendererObj.drawStringWithShadow(item.name, col2X + 5, itemY2, 0xFFFFFF);
                // Preço do item à direita
                int priceX = col2X + colW - 10 - this.fontRendererObj.getStringWidth(item.price.replace("~", ""));
                this.fontRendererObj.drawStringWithShadow(item.price.replace("~", ""), priceX, itemY2, 0xFFFFFF);
                itemY2 += 10;
            }
            
            // Enxadas de herbalismo
            itemY2 += 10;
            this.fontRendererObj.drawStringWithShadow("§6§lEnxadas:", col2X + 5, itemY2, 0xFFFFFF);
            itemY2 += 12;
            
            for (PriceData.ItemPrice item : PriceData.Herbalism.ITEMS) {
                // Nome do item à esquerda
                this.fontRendererObj.drawStringWithShadow(item.name, col2X + 5, itemY2, 0xFFFFFF);
                // Preço do item à direita
                int priceX = col2X + colW - 10 - this.fontRendererObj.getStringWidth(item.price.replace("~", ""));
                this.fontRendererObj.drawStringWithShadow(item.price.replace("~", ""), priceX, itemY2, 0xFFFFFF);
                itemY2 += 10;
            }
            
            // Armaduras de herbalismo
            itemY2 += 10;
            this.fontRendererObj.drawStringWithShadow("§2§lArmaduras:", col2X + 5, itemY2, 0xFFFFFF);
            itemY2 += 12;
            
            for (PriceData.ItemPrice item : PriceData.Herbalism.ARMOR_SETS) {
                // Nome do item à esquerda
                this.fontRendererObj.drawStringWithShadow(item.name, col2X + 5, itemY2, 0xFFFFFF);
                // Preço do item à direita
                int priceX = col2X + colW - 10 - this.fontRendererObj.getStringWidth(item.price.replace("~", ""));
                this.fontRendererObj.drawStringWithShadow(item.price.replace("~", ""), priceX, itemY2, 0xFFFFFF);
                itemY2 += 10;
            }
            
            // Coluna 3 - Cor Azul
            int col3X = col2X + colW + gapPrices;
            int col3Y = innerY + marginPrices;
            int col3H = innerH - (marginPrices * 2);
            // Fundo azul mais transparente
            Gui.drawRect(col3X, col3Y, col3X + colW, col3Y + col3H, 0x220000FF);
            // Borda azul
            Gui.drawRect(col3X, col3Y, col3X + colW, col3Y + 1, 0xFF0000FF);
            Gui.drawRect(col3X, col3Y + col3H - 1, col3X + colW, col3Y + col3H, 0xFF0000FF);
            Gui.drawRect(col3X, col3Y, col3X + 1, col3Y + col3H, 0xFF0000FF);
            Gui.drawRect(col3X + colW - 1, col3Y, col3X + colW, col3Y + col3H, 0xFF0000FF);
            
            // Título da coluna 3
            this.fontRendererObj.drawStringWithShadow("§9§lItens Diversos", col3X + 5, col3Y + 5, 0xFFFFFF);
            
            // Itens para minions
            int itemY3 = col3Y + 25;
            this.fontRendererObj.drawStringWithShadow("§b§lItens Minions:", col3X + 5, itemY3, 0xFFFFFF);
            itemY3 += 12;
            
            for (PriceData.ItemPrice item : PriceData.MinionItems.ITEMS) {
                // Nome do item à esquerda
                this.fontRendererObj.drawStringWithShadow(item.name, col3X + 5, itemY3, 0xFFFFFF);
                // Preço do item à direita
                int priceX = col3X + colW - 10 - this.fontRendererObj.getStringWidth(item.price.replace("~", ""));
                this.fontRendererObj.drawStringWithShadow(item.price.replace("~", ""), priceX, itemY3, 0xFFFFFF);
                itemY3 += 10;
            }
            
            // Arcos
            itemY3 += 10;
            this.fontRendererObj.drawStringWithShadow("§f§lArcos:", col3X + 5, itemY3, 0xFFFFFF);
            itemY3 += 12;
            
            for (PriceData.ItemPrice item : PriceData.Bows.ITEMS) {
                // Nome do item à esquerda
                this.fontRendererObj.drawStringWithShadow(item.name, col3X + 5, itemY3, 0xFFFFFF);
                // Preço do item à direita
                int priceX = col3X + colW - 10 - this.fontRendererObj.getStringWidth(item.price.replace("~", ""));
                this.fontRendererObj.drawStringWithShadow(item.price.replace("~", ""), priceX, itemY3, 0xFFFFFF);
                itemY3 += 10;
            }
            
            // Armas
            itemY3 += 10;
            this.fontRendererObj.drawStringWithShadow("§c§lArmas:", col3X + 5, itemY3, 0xFFFFFF);
            itemY3 += 12;
            
            for (PriceData.ItemPrice item : PriceData.Weapons.ITEMS) {
                // Nome do item à esquerda
                this.fontRendererObj.drawStringWithShadow(item.name, col3X + 5, itemY3, 0xFFFFFF);
                // Preço do item à direita
                int priceX = col3X + colW - 10 - this.fontRendererObj.getStringWidth(item.price.replace("~", ""));
                this.fontRendererObj.drawStringWithShadow(item.price.replace("~", ""), priceX, itemY3, 0xFFFFFF);
                itemY3 += 10;
            }
            
            // Livros
            itemY3 += 10;
            this.fontRendererObj.drawStringWithShadow("§d§lLivros:", col3X + 5, itemY3, 0xFFFFFF);
            itemY3 += 12;
            
            for (PriceData.ItemPrice item : PriceData.Books.ITEMS) {
                // Nome do item à esquerda
                this.fontRendererObj.drawStringWithShadow(item.name, col3X + 5, itemY3, 0xFFFFFF);
                // Preço do item à direita
                int priceX = col3X + colW - 10 - this.fontRendererObj.getStringWidth(item.price.replace("~", ""));
                this.fontRendererObj.drawStringWithShadow(item.price.replace("~", ""), priceX, itemY3, 0xFFFFFF);
                itemY3 += 10;
            }
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
                else if (currentPage == 1) {
                    // PÁGINA DE TELEPORTES - VERIFICAR CLIQUES NOS BOTÕES
                    int cols = 5;
                    int lines = 4;
                    int spacing = 4;
                    int teleportBtnW = (innerW - (spacing * (cols - 1))) / cols;
                    int teleportBtnH = (innerH - (spacing * (lines - 1))) / lines;
                    //teleportBtnH = (int)(teleportBtnH * 1.8); // Aumentado para botões mais altos

                    for (int i = 0; i < WARPS_DATA.length; i++) {
                        int col = i % cols;
                        int row = i / cols;
                        int bx = innerX + (col * (teleportBtnW + spacing));
                        int by = innerY + (row * (teleportBtnH + spacing));

                        if (mouseX >= bx && mouseX <= bx + teleportBtnW && mouseY >= by && mouseY <= by + teleportBtnH) {
                            // Executa o comando de teleporte
                            String command = WARPS_DATA[i][2];
                            mc.thePlayer.sendChatMessage("/" + command);
                            mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
                            return;
                        }
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

    public static String[][] getWarpsData() {
        return WARPS_DATA;
    }

    private final Minecraft mc = Minecraft.getMinecraft();
    private net.minecraft.client.renderer.entity.RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
}
