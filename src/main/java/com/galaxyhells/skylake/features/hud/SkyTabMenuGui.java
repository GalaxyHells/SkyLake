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

        // --- CÓDIGO DO NOVO BOTÃO DE CONFIGURAÇÃO ---
        int settingsX = x + menuW - 25; // Canto direito
        int settingsY = y + 10;        // Topo
        boolean settingsHovered = mouseX >= settingsX && mouseX <= settingsX + 20 && mouseY >= settingsY && mouseY <= settingsY + 20;

        // Desenha o fundo do mini-botão se estiver com o mouse em cima
        if (settingsHovered) {
            Gui.drawRect(settingsX - 2, settingsY - 2, settingsX + 18, settingsY + 18, 0x80FFFFFF);
        }

        // Renderiza o ícone do Comparador (Representando Configurações)
        GlStateManager.pushMatrix();
        GlStateManager.translate(settingsX, settingsY, 0);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        this.itemRender.renderItemIntoGUI(new ItemStack(Items.comparator), 0, 0);
        GlStateManager.popMatrix();

        // Se estiver com o mouse em cima, mostra o texto "Configurações"
        if (settingsHovered) {
            this.drawHoveringText(java.util.Arrays.asList("§bConfigurações SkyLake"), mouseX, mouseY);
        }
        // --------------------------------------------

        // Fundo e Título
        drawMenuFrame(x, y, menuW, menuH, 0xCC111111);
        String title = "";
        if (currentPage == 0) title = "§6SkyLake §fMenu";
        if (currentPage == 1) title = "§bSkyLake §fTeleportes";
        if (currentPage == 1) title = "§bSkyLake §fPreços";
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
            renderButton(1, innerX + buttonW + gap, innerY, buttonW, innerH, "§aLista de preços.", Items.writable_book, mouseX, mouseY, "Slayers e Skills");
            renderButton(2, innerX + (buttonW + gap) * 2, innerY, buttonW, innerH, "§7Em breve...", Item.getItemFromBlock(Blocks.anvil), mouseX, mouseY, "Minions e Craft");
        }
        else if (currentPage == 1) {
            int cols = 6; // Definido para 5 colunas
            int spacing = 4;
            // Cálculo exato para preencher a largura sem sobras
            int btnW = (innerW - (spacing * (cols - 1))) / cols;
            int btnH = (innerW - (spacing * (cols - 1))) / cols;
            btnH = (int)(btnH * 0.75);

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
        else if (currentPage == 2) {
            int colW = ((innerW - 20) / 3);
            int curY = innerY;
            int spacing = 11; // Aumentado levemente para acomodar as sub-linhas

            // --- COLUNA 1: DRAGÕES (DETALHADO) ---
            drawCategoryTitle(innerX, curY, "§5§lArmaduras de Dragão");
            curY += 14;

            // Dados organizados: {Nome, Set Total, 10 Escamas, Cap, Peit, Calça, Bota}
            String[][] dragons = {
                    {"Superior", "200kk", "8.3kk", "41.5k", "66.4k", "58.1k", "33.2k"},
                    {"Forte", "36kk", "1.5kk", "7.5k", "12k", "10.5k", "6k"},
                    {"Instável", "30kk", "1.25kk", "6.2k", "10k", "8.7k", "5k"},
                    {"Jovem", "20kk", "830k", "4.1k", "6.6k", "5.8k", "3.3k"},
                    {"Sábio", "15kk", "625k", "3.1k", "5k", "4.3k", "2.5k"},
                    {"Ancião", "10kk", "410k", "2k", "3.2k", "2.8k", "1.6k"},
                    {"Protetor", "8kk", "330k", "1.6k", "2.6k", "2.3k", "1.3k"}
            };

            for (String[] d : dragons) {
                // Linha principal: Nome + Set + Escamas
                this.fontRendererObj.drawString("§f" + d[0] + ": §e" + d[1] + " §8| §d" + d[2] + " (esc)", innerX + 2, curY, 0xFFFFFF);
                curY += 9;
                // Sub-linha: Partes individuais (Cap, Peit, Cal, Bot)
                String parts = "§7C:" + d[3] + " P:" + d[4] + " L:" + d[5] + " B:" + d[6];
                this.fontRendererObj.drawString(parts, innerX + 4, curY, 0xFFFFFF);
                curY += spacing;
            }

            // --- COLUNA 2: JARDIM & HORTALIÇAS ---
            curY = innerY;
            int col2X = innerX + colW + 10;
            drawCategoryTitle(col2X, curY, "§a§lMini Jardim");
            curY += 14;
            String[][] garden = {{"Livro Hiper", "750k"}, {"Enxada Pol.", "1.8kk"}, {"Hidromel 5", "7.5kk"}, {"Salada Frut.", "600k"}, {"Buquê", "850k"}, {"Recombin.", "18.75kk"}};
            for(String[] s : garden) { drawPriceLine(col2X, curY, s[0], s[1]); curY += 10; }

            curY += 8;
            drawCategoryTitle(col2X, curY, "§2§lHortaliças");
            curY += 14;
            drawPriceLine(col2X, curY, "Adubo (Pack)", "400k"); curY += 10;
            drawPriceLine(col2X, curY, "Raiz (Pack)", "38.4kk");

            // --- COLUNA 3: OUTROS & COMBATE ---
            curY = innerY;
            int col3X = col2X + colW + 10;
            drawCategoryTitle(col3X, curY, "§6§lMinions & Itens");
            curY += 14;
            drawPriceLine(col3X, curY, "S. Compact.", "650k"); curY += 10;
            drawPriceLine(col3X, curY, "Dep. Grande", "300k"); curY += 10;
            drawPriceLine(col3X, curY, "B. Magma", "1.2kk");

            curY += 18;
            drawCategoryTitle(col3X, curY, "§c§lArmas (Arcos/Esp.)");
            curY += 14;
            drawPriceLine(col3X, curY, "Pigman", "15kk"); curY += 10;
            drawPriceLine(col3X, curY, "AOTE", "1kk"); curY += 10;
            drawPriceLine(col3X, curY, "Leaping", "10kk"); curY += 10;
            drawPriceLine(col3X, curY, "Apollo", "1kk");

            // --- Footer Notes
            int footerX = innerX + 2;
            int footerY = menuH + 3;

            footerY  = menuH + 5;
            this.fontRendererObj.drawStringWithShadow("Nota: C = Capacete, P = Peitoral, L = Calça, B = Botas, Esc = 10 Escamas", footerX, footerY, 0xFFFFFF);
            footerY = menuH + 18;
            this.fontRendererObj.drawStringWithShadow("Nota: k = 1.000 (mil), kk = 1.000.000 (milhão), b = 1.000.000.000 (bilhão)", footerX, footerY, 0xFFFFFF);
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
        // Borda superior decorativa
        Gui.drawRect(x, y, x + w, y + 1, 0xFF333333);

        // --- RENDERIZAÇÃO DO TEXTO (TOPO CENTRALIZADO) ---
        // Calculamos a largura do texto para subtrair da metade da largura do botão
        int textX = x + (w / 2) - (this.fontRendererObj.getStringWidth(label) / 2);
        int textY = y + 4; // 4 pixels de distância do topo
        this.fontRendererObj.drawStringWithShadow(label, textX, textY, 0xFFFFFF);

        // --- RENDERIZAÇÃO DO ÍCONE (CENTRO GEOMÉTRICO) ---
        // Itens no Minecraft têm 16x16 pixels.
        // Centralizamos usando (Tamanho do Botão / 2) - (Metade do Item)
        int iconX = x + (w / 2) - 8;
        int iconY = y + (h / 2) - 8;

        GlStateManager.pushMatrix();
        // Renderizamos o item diretamente na posição calculada
        this.itemRender.renderItemIntoGUI(stack, iconX, iconY);
        GlStateManager.popMatrix();
    }

    private void drawCategoryTitle(int x, int y, String title) {
        this.fontRendererObj.drawString(title, x, y, 0xFFFFFF);
        Gui.drawRect(x, y + 9, x + 80, y + 10, 0x50FFFFFF); // Linha decorativa embaixo
    }

    private void drawPriceLine(int x, int y, String label, String price) {
        this.fontRendererObj.drawString("§7" + label + ":", x, y, 0xFFFFFF);
        int priceX = x + 100 - this.fontRendererObj.getStringWidth(price); // Alinha o preço à direita da coluna
        this.fontRendererObj.drawString("§e" + price, priceX, y, 0xFFFFFF);
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

        // Lógica do clique no botão de CONFIGURAÇÕES (Comparador)
        int settingsX = x + menuW - 25;
        int settingsY = y + 10;
        if (mouseX >= settingsX && mouseX <= settingsX + 20 && mouseY >= settingsY && mouseY <= settingsY + 20) {
            mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
            // Abre a tela de configuração que o comando /skylake abriria
            mc.displayGuiScreen(new com.galaxyhells.skylake.config.SkyLakeConfig());
            return;
        }

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
                // Clique no Botão 2 (Lista de Preços)
                else if (mouseX >= innerX + btnW + gap && mouseX <= innerX + (btnW * 2) + gap) {
                    currentPage = 2; // ABRE A ABA DE PREÇOS
                    mc.thePlayer.playSound("random.click", 1.0F, 1.0F);
                }
            }
            else if (currentPage == 1) {
                int cols = 6; // Definido para 5 colunas
                int spacing = 4;
                // Cálculo exato para preencher a largura sem sobras
                int btnW2 = (innerW - (spacing * (cols - 1))) / cols;
                int btnH2 = (innerW - (spacing * (cols - 1))) / cols;
                btnH2 = (int)(btnH2 * 0.75);

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