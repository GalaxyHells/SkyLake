package com.galaxyhells.skylake.features.hud.FancyHUD;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.ModConstants;
import com.galaxyhells.skylake.utils.OptionType;
import com.galaxyhells.skylake.utils.ThemeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

/**
 * Hotbar customizada com 9 slots + slot central ampliado
 */
public class FancyHotbar {
    
    private static final int SQUARE_SIZE = ThemeManager.scale(20);
    private static final int CENTER_SQUARE_SIZE = ThemeManager.scale(40);
    private static final int SPACING = ThemeManager.scale(2);
    private static final int HORIZONTAL_SQUARES = 9;
    private static final int PADDING = ThemeManager.scale(4);
    
    private final Minecraft mc = Minecraft.getMinecraft();
    
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.FANCY_HUD))) return;

        if (mc.gameSettings.showDebugInfo) {
            return;
        }

        // Não renderiza se inventário estiver aberto
        if (mc.currentScreen != null) {
            //return;
        }

        renderFancyHotbar();
    }
    
    private void renderFancyHotbar() {
        if (mc.thePlayer == null) {
            return;
        }
        
        // Slot atual selecionado
        int selectedSlot = mc.thePlayer.inventory.currentItem;
        
        // Coordenadas da tela
        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();
        
        // Largura dos 9 slots
        int horizontalSquaresWidth = HORIZONTAL_SQUARES * SQUARE_SIZE + (HORIZONTAL_SQUARES - 1) * SPACING;
        int horizontalSquaresHeight = SQUARE_SIZE;
        
        // Dimensões totais do layout
        int totalWidth = Math.max(horizontalSquaresWidth, CENTER_SQUARE_SIZE);
        int totalHeight = horizontalSquaresHeight + SPACING + CENTER_SQUARE_SIZE;
        
        // Posição: canto inferior direito
        int baseX = screenWidth - totalWidth - PADDING;
        int baseY = screenHeight - totalHeight - PADDING;
        
        // Renderizar 9 slots horizontais
        renderHorizontalSquares(baseX, baseY + CENTER_SQUARE_SIZE + SPACING, selectedSlot);
        
        // Renderizar slot central ampliado
        renderSelectedItemSquare(baseX, baseY, horizontalSquaresWidth);
        
        // Nome do item selecionado
        renderSelectedItemName(baseX, baseY, horizontalSquaresWidth);
    }
    
    private void renderHorizontalSquares(int baseX, int baseY, int selectedSlot) {
        Color squareColor = new Color(0, 0, 0, 144);
        
        // Habilitar transparência
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.disableTexture2D();
        
        for (int i = 0; i < HORIZONTAL_SQUARES; i++) {
            int x = baseX + i * (SQUARE_SIZE + SPACING);
            int y = baseY;
            
            drawRect(x, y, SQUARE_SIZE, SQUARE_SIZE, squareColor);
            
            // Renderizar item da hotbar neste slot
            renderHotbarItem(x, y, i);
            
            // Desenhar número no topo esquerdo
            drawSquareNumber(x, y, i + 1);
            
            // Desenhar quantidade no canto inferior direito
            drawItemQuantity(x, y, i);
            
            // Desenhar borda se este for o slot selecionado
            if (i == selectedSlot) {
                drawSelectedBorder(x, y);
            }
        }
        
        // Restaurar estados OpenGL
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    private void renderSelectedItemName(int baseX, int baseY, int horizontalSquaresWidth) {
        if (mc.thePlayer == null || mc.thePlayer.inventory == null) {
            return;
        }
        
        // Obter o item selecionado da hotbar
        int currentSlot = mc.thePlayer.inventory.currentItem;
        ItemStack selectedItem = mc.thePlayer.inventory.getStackInSlot(currentSlot);
        
        if (selectedItem == null) {
            return;
        }
        
        // Obter o nome do item
        String itemName = selectedItem.getDisplayName();
        if (itemName == null || itemName.isEmpty()) {
            return;
        }
        
        // Habilitar texturas para renderizar texto
        //GlStateManager.enableTexture2D();
        
        // Aplicar escala para o texto do nome (70% do tamanho original)
        float scale = 1f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        
        // Cor do texto (branco)
        //int textColor = ModConstants.HUD_TEXT_PRIMARY;
        int textColor = 0xFFFFFFFF;
        
        // Posição do texto (lado esquerdo do quadrado grande, alinhado à direita)
        // Calculamos a largura do texto para alinhar à direita
        int textWidth = mc.fontRendererObj.getStringWidth(itemName);
        int textX = (int)((baseX + horizontalSquaresWidth - textWidth - 44) / scale); // -5 para espaçamento
        //int textX = (int)((baseX - horizontalSquaresWidth - textWidth - 3) / scale); // -5 para espaçamento
        int textY = (int)((baseY + 24) / scale); // Acima dos quadrados pequenos
        
        // Renderizar o nome do item
        mc.fontRendererObj.drawStringWithShadow(itemName, textX, textY, textColor);
        
        // Restaurar escala
        GlStateManager.popMatrix();
        
        // Desabilitar texturas
        //GlStateManager.disableTexture2D();
    }
    
    private void renderSelectedItemSquare(int baseX, int baseY, int horizontalSquaresWidth) {
        //Color centerColor = new Color(255, 200, 100, 200); // Laranja semi-transparente
        Color centerColor = new Color(0, 0, 0, 144);
        //Color centerColor = new Color(ModConstants.HUD_BACKGROUND);
        //Color centerColor = ModConstants.argbToColor(ModConstants.HUD_BACKGROUND);
        
        // Posicionar quadrado maior acima no lado direito
        // Alinhado à direita com os 9 quadrados horizontais
        int centerX = baseX + horizontalSquaresWidth - CENTER_SQUARE_SIZE;
        int centerY = baseY;
        
        // Desenhar fundo do quadrado
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.disableTexture2D();
        drawRect(centerX, centerY, CENTER_SQUARE_SIZE, CENTER_SQUARE_SIZE, centerColor);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        
        // Renderizar item selecionado da hotbar
        renderSelectedItem(centerX, centerY);
        
        // Renderizar quantidade do item selecionado
        renderSelectedItemQuantity(centerX, centerY);
    }
    
    private void renderSelectedItem(int x, int y) {
        if (mc.thePlayer == null || mc.thePlayer.inventory == null) {
            return;
        }
        
        // Obter o item selecionado da hotbar (slot atual)
        int currentSlot = mc.thePlayer.inventory.currentItem;
        ItemStack selectedItem = mc.thePlayer.inventory.getStackInSlot(currentSlot);
        
        if (selectedItem == null) {
            // Renderizar painel de vidro branco quando não houver item selecionado
                        return;
        }
        
        // Habilitar renderização 3D para itens
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        
        // Aplicar escala para o item (1.5x o tamanho original)
        float itemScale = 1.5f;
        int itemSize = 16; // Tamanho padrão de ícone de item
        int scaledSize = (int)(itemSize * itemScale);
        
        // Posição centralizada do item no quadrado
        int itemX = x + (CENTER_SQUARE_SIZE - scaledSize) / 2;
        int itemY = y + (CENTER_SQUARE_SIZE - scaledSize) / 2;
        
        // Aplicar escala
        GlStateManager.pushMatrix();
        GlStateManager.translate(itemX, itemY, 0);
        GlStateManager.scale(itemScale, itemScale, itemScale);
        
        // Renderizar o item
        try {
            mc.getRenderItem().renderItemAndEffectIntoGUI(selectedItem, 0, 0);
        } catch (Exception e) {
            // Ignorar erros de renderização
        }
        
        // Restaurar escala
        GlStateManager.popMatrix();
        
        // Restaurar estados OpenGL
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableDepth();
    }
    
    private void renderSelectedItemQuantity(int x, int y) {
        if (mc.thePlayer == null || mc.thePlayer.inventory == null) {
            return;
        }
        
        // Obter o item selecionado da hotbar
        int currentSlot = mc.thePlayer.inventory.currentItem;
        ItemStack selectedItem = mc.thePlayer.inventory.getStackInSlot(currentSlot);
        
        if (selectedItem == null) {
            return;
        }
        
        // Obter quantidade do item
        int quantity = selectedItem.stackSize;
        
        // Ignorar se quantidade for 1
        if (quantity <= 1) {
            return;
        }
        
        // Habilitar texturas para renderizar texto
        //GlStateManager.enableTexture2D();
        
        // Aplicar escala para o texto da quantidade (80% do tamanho original para o quadrado grande)
        float scale = 0.8f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        
        // Cor do texto (branco)
        //int textColor = ModConstants.HUD_TEXT_PRIMARY;
        int textColor = 0xFFFFFFFF;

        //if ("A" == "A") return;
        
        // Posição do texto (canto inferior direito do quadrado grande)
        String quantityText = String.valueOf(quantity);
        int textWidth = mc.fontRendererObj.getStringWidth(quantityText);
        int textX = (int)((x + CENTER_SQUARE_SIZE - textWidth + 2) / scale); // +2 para melhor alinhamento
        int textY = (int)((y + CENTER_SQUARE_SIZE - mc.fontRendererObj.FONT_HEIGHT + 2) / scale); // +2 para subir um pouco
        
        // Renderizar a quantidade
        mc.fontRendererObj.drawStringWithShadow(quantityText, textX, textY, textColor);

        //if ("A" == "A") return;
        
        // Restaurar escala
        GlStateManager.popMatrix();

        if ("A" == "A") return;
        
        // Desabilitar texturas
        //GlStateManager.disableTexture2D();
    }
    
    private void renderHotbarItem(int x, int y, int slotIndex) {
        if (mc.thePlayer == null || mc.thePlayer.inventory == null) {
            return;
        }
        
        // Obter item do slot da hotbar (slots 0-8)
        ItemStack item = mc.thePlayer.inventory.getStackInSlot(slotIndex);
        
        if (item == null) {
            return;
        }
        
        // Habilitar texturas e renderização 3D
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        
        // Aplicar escala para o item caber no quadrado pequeno
        float itemScale = 1f; // 80% do tamanho original
        int itemSize = 16; // Tamanho padrão do ícone
        int scaledSize = (int)(itemSize * itemScale);
        
        // Posição centralizada do item no quadrado
        int itemX = x + (SQUARE_SIZE - scaledSize) / 2;
        int itemY = y + (SQUARE_SIZE - scaledSize) / 2 + 1; // +1 para compensar o número
        
        // Aplicar escala
        GlStateManager.pushMatrix();
        GlStateManager.translate(itemX, itemY, 0);
        GlStateManager.scale(itemScale, itemScale, itemScale);
        
        // Renderizar o item
        try {
            mc.getRenderItem().renderItemAndEffectIntoGUI(item, 0, 0);
        } catch (Exception e) {
            // Ignorar erros de renderização
        }
        
        // Restaurar estados
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
    }
    
    private void drawItemQuantity(int x, int y, int slotIndex) {
        if (mc.thePlayer == null || mc.thePlayer.inventory == null) {
            return;
        }
        
        // Obter item do slot da hotbar
        ItemStack item = mc.thePlayer.inventory.getStackInSlot(slotIndex);
        
        if (item == null) {
            return;
        }
        
        // Obter quantidade do item
        int quantity = item.stackSize;
        
        // Ignorar se quantidade for 1
        if (quantity <= 1) {
            return;
        }
        
        // Habilitar texturas para renderizar texto
        GlStateManager.enableTexture2D();
        
        // Aplicar escala para o texto da quantidade (60% do tamanho original)
        float scale = 0.75f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        
        // Cor do texto (branco)
        //int textColor = ModConstants.HUD_TEXT_PRIMARY;
        int textColor = 0xFFFFFFFF;
        
        // Posição do texto (canto inferior direito)
        String quantityText = String.valueOf(quantity);
        int textWidth = mc.fontRendererObj.getStringWidth(quantityText);
        int textX = (int)((x + SQUARE_SIZE - textWidth + 3) / scale); // -1 para melhor alinhamento
        int textY = (int)((y + SQUARE_SIZE - mc.fontRendererObj.FONT_HEIGHT + 3) / scale); // +1 para subir um pouco
        
        // Renderizar a quantidade
        mc.fontRendererObj.drawStringWithShadow(quantityText, textX, textY, textColor);
        
        // Restaurar escala
        GlStateManager.popMatrix();
        
        // Desabilitar texturas
        GlStateManager.disableTexture2D();
    }
    
    private void drawSelectedBorder(int x, int y) {
        // Cor da borda (amarelo brilhante)
        int borderColor = ModConstants.HUD_TEXT_WARNING;
        
        // Espessura da borda
        int borderThickness = 1;
        
        // Desenhar borda ao redor do quadrado
        // Topo
        Gui.drawRect(x - borderThickness, y - borderThickness, x + SQUARE_SIZE + borderThickness, y, borderColor);
        // Baixo
        Gui.drawRect(x - borderThickness, y + SQUARE_SIZE, x + SQUARE_SIZE + borderThickness, y + SQUARE_SIZE + borderThickness, borderColor);
        // Esquerda
        Gui.drawRect(x - borderThickness, y, x, y + SQUARE_SIZE, borderColor);
        // Direita
        Gui.drawRect(x + SQUARE_SIZE, y, x + SQUARE_SIZE + borderThickness, y + SQUARE_SIZE, borderColor);
    }
    
    private void drawSquareNumber(int x, int y, int number) {
        // Habilitar texturas para renderizar texto
        GlStateManager.enableTexture2D();
        
        // Aplicar escala para diminuir o tamanho do texto (70% do tamanho original)
        float scale = 0.7f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        
        // Cor do texto (branco)
        //int textColor = ModConstants.HUD_TEXT_PRIMARY;
        int textColor = 0xFFFFFFFF;
        
        // Posição do texto ajustada pela escala (topo esquerdo)
        int textX = (int)((x + 2) / scale);
        int textY = (int)((y + 1) / scale);
        
        // Renderizar o número
        String numberText = String.valueOf(number);
        mc.fontRendererObj.drawStringWithShadow(numberText, textX, textY, textColor);
        
        // Restaurar escala
        GlStateManager.popMatrix();
        
        // Desabilitar texturas
        GlStateManager.disableTexture2D();
    }
    
    private void drawRect(int x, int y, int width, int height, Color color) {
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, 
                           color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        
        // Desenhar retângulo preenchido
        Gui.drawRect(x, y, x + width, y + height, color.getRGB());
    }

    @SubscribeEvent(priority = net.minecraftforge.fml.common.eventhandler.EventPriority.HIGHEST)
    public void onRenderHotbar(RenderGameOverlayEvent.Pre event) {
        // Verifica se é o evento da hotbar
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }

        // Se a Fancy HUD estiver ativa, cancela a renderização da hotbar padrão
        if (Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.FANCY_HUD))) {
            // Verificar se inventário ou containers estão abertos - se estiverem, não renderiza Fancy Hotbar
            //if (mc.currentScreen == null) {
                event.setCanceled(true);
            //}
        }
    }
}
