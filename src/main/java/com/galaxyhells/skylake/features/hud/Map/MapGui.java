package com.galaxyhells.skylake.features.hud.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class MapGui extends GuiScreen {
    
    private static final int CHUNK_SIZE = 16;
    private static final int MAP_CHUNKS_X = 25; // Quantidade de chunks visíveis em X
    private static final int MAP_CHUNKS_Z = 18; // Quantidade de chunks visíveis em Z
    private static final int CHUNK_PIXEL_SIZE = 16; // Tamanho de cada chunk em pixels no mapa
    private static final int MAP_WIDTH = MAP_CHUNKS_X * CHUNK_PIXEL_SIZE;
    private static final int MAP_HEIGHT = MAP_CHUNKS_Z * CHUNK_PIXEL_SIZE;
    
    // Pontos de interesse
    private static final Map<String, BlockPos> POINTS_OF_INTEREST = new HashMap<>();
    static {
        POINTS_OF_INTEREST.put("spawn", new BlockPos(67, 90, 88));
        POINTS_OF_INTEREST.put("leilao", new BlockPos(99, 90, 89));
        POINTS_OF_INTEREST.put("cemiterio", new BlockPos(100, 91, 109));
    }
    
    private int mapX, mapY;
    private boolean isDragging = false;
    private int dragStartX, dragStartY;
    private int chunkOffsetX, chunkOffsetZ;
    
    public MapGui() {
        centerMapOnPlayer();
        chunkOffsetX = 0;
        chunkOffsetZ = 0;
    }
    
    private void centerMapOnPlayer() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            ScaledResolution sr = new ScaledResolution(mc);
            mapX = (sr.getScaledWidth() - MAP_WIDTH) / 2;
            mapY = (sr.getScaledHeight() - MAP_HEIGHT) / 2;
        }
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;
        
        // Desenhar fundo do mapa
        drawMapBackground(mc);
        
        // Desenhar chunks
        drawChunks(mc);
        
        // Desenhar pontos de interesse
        drawPointsOfInterest(mc);
        
        // Desenhar posição do jogador
        drawPlayerPosition(mc);
        
        // Desenhar borda do mapa
        drawMapBorder();
        
        // Desenhar instruções
        drawInstructions();
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedMouseButton == 0 && isDragging) { // Botão esquerdo
            // Calcular offset em chunks baseado no movimento do mouse
            int deltaX = mouseX - dragStartX;
            int deltaY = mouseY - dragStartY;
            
            // Converter pixels para chunks (cada chunk tem CHUNK_PIXEL_SIZE pixels)
            chunkOffsetX = -(deltaX / CHUNK_PIXEL_SIZE);
            chunkOffsetZ = -(deltaY / CHUNK_PIXEL_SIZE);
        }
    }
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) { // Botão esquerdo
            // Verificar se clicou dentro da área do mapa
            if (mouseX >= mapX && mouseX <= mapX + MAP_WIDTH &&
                mouseY >= mapY && mouseY <= mapY + MAP_HEIGHT) {
                isDragging = true;
                dragStartX = mouseX;
                dragStartY = mouseY;
            }
        }
    }
    
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) { // Botão esquerdo
            isDragging = false;
        }
    }
    
    private void drawInstructions() {
        Minecraft mc = Minecraft.getMinecraft();
        String instructions = "Pressione M para fechar | Clique e arraste para mover o mapa";
        this.drawCenteredString(this.fontRendererObj, instructions, this.width / 2, 20, 0xFFFFFF);
    }
    
    private void drawMapBackground(Minecraft mc) {
        // Fundo semi-transparente
        Gui.drawRect(mapX - 2, mapY - 2, mapX + MAP_WIDTH + 2, mapY + MAP_HEIGHT + 2, 0x90000000);
        Gui.drawRect(mapX, mapY, mapX + MAP_WIDTH, mapY + MAP_HEIGHT, 0x73000000);
    }
    
    private void drawChunks(Minecraft mc) {
        if (mc.thePlayer == null) return;
        
        int playerChunkX = mc.thePlayer.getPosition().getX() / CHUNK_SIZE;
        int playerChunkZ = mc.thePlayer.getPosition().getZ() / CHUNK_SIZE;
        
        // Calcular chunk inicial visível com offset
        int startChunkX = playerChunkX - MAP_CHUNKS_X / 2 + chunkOffsetX;
        int startChunkZ = playerChunkZ - MAP_CHUNKS_Z / 2 + chunkOffsetZ;
        
        // Desenhar grid de chunks
        for (int x = 0; x < MAP_CHUNKS_X; x++) {
            for (int z = 0; z < MAP_CHUNKS_Z; z++) {
                int chunkX = startChunkX + x;
                int chunkZ = startChunkZ + z;
                
                int pixelX = mapX + x * CHUNK_PIXEL_SIZE;
                int pixelZ = mapY + z * CHUNK_PIXEL_SIZE;
                
                // Cor base do chunk (alternando para criar grid)
                int chunkColor = ((chunkX + chunkZ) % 2 == 0) ? 0x40404040 : 0x50505050;
                
                // Desenhar chunk
                Gui.drawRect(pixelX, pixelZ, pixelX + CHUNK_PIXEL_SIZE, pixelZ + CHUNK_PIXEL_SIZE, chunkColor);
                
                // Desenhar borda do chunk
                Gui.drawRect(pixelX, pixelZ, pixelX + CHUNK_PIXEL_SIZE, pixelZ + 1, 0xFF303030);
                Gui.drawRect(pixelX, pixelZ + CHUNK_PIXEL_SIZE - 1, pixelX + CHUNK_PIXEL_SIZE, pixelZ + CHUNK_PIXEL_SIZE, 0xFF303030);
                Gui.drawRect(pixelX, pixelZ, pixelX + 1, pixelZ + CHUNK_PIXEL_SIZE, 0xFF303030);
                Gui.drawRect(pixelX + CHUNK_PIXEL_SIZE - 1, pixelZ, pixelX + CHUNK_PIXEL_SIZE, pixelZ + CHUNK_PIXEL_SIZE, 0xFF303030);
            }
        }
    }
    
    private void drawPointsOfInterest(Minecraft mc) {
        if (mc.thePlayer == null) return;
        
        int playerChunkX = mc.thePlayer.getPosition().getX() / CHUNK_SIZE;
        int playerChunkZ = mc.thePlayer.getPosition().getZ() / CHUNK_SIZE;
        int startChunkX = playerChunkX - MAP_CHUNKS_X / 2 + chunkOffsetX;
        int startChunkZ = playerChunkZ - MAP_CHUNKS_Z / 2 + chunkOffsetZ;
        
        for (Map.Entry<String, BlockPos> entry : POINTS_OF_INTEREST.entrySet()) {
            BlockPos pos = entry.getValue();
            String name = entry.getKey();
            
            int poiChunkX = pos.getX() / CHUNK_SIZE;
            int poiChunkZ = pos.getZ() / CHUNK_SIZE;
            
            // Verificar se o ponto está visível no mapa com offset
            if (poiChunkX >= startChunkX && poiChunkX < startChunkX + MAP_CHUNKS_X &&
                poiChunkZ >= startChunkZ && poiChunkZ < startChunkZ + MAP_CHUNKS_Z) {
                
                int mapX = this.mapX + (poiChunkX - startChunkX) * CHUNK_PIXEL_SIZE + CHUNK_PIXEL_SIZE / 2;
                int mapZ = this.mapY + (poiChunkZ - startChunkZ) * CHUNK_PIXEL_SIZE + CHUNK_PIXEL_SIZE / 2;
                
                // Desenhar marcador do ponto de interesse
                int color;
                switch (name) {
                    case "spawn":
                        color = 0xFF00FF00; // Verde
                        break;
                    case "leilao":
                        color = 0xFFFFFF00; // Amarelo
                        break;
                    case "cemiterio":
                        color = 0xFF8B4513; // Marrom
                        break;
                    default:
                        color = 0xFFFF0000; // Vermelho
                }
                
                // Desenhar círculo para o ponto de interesse
                drawCircle(mapX, mapZ, 4, color);
                
                // Desenhar texto
                String displayName = name.substring(0, 1).toUpperCase() + name.substring(1);
                mc.fontRendererObj.drawStringWithShadow(displayName, mapX - mc.fontRendererObj.getStringWidth(displayName) / 2, mapZ + 6, 0xFFFFFFFF);
            }
        }
    }
    
    private void drawPlayerPosition(Minecraft mc) {
        if (mc.thePlayer == null) return;
        
        // Calcular posição do jogador com offset
        int playerChunkX = mc.thePlayer.getPosition().getX() / CHUNK_SIZE;
        int playerChunkZ = mc.thePlayer.getPosition().getZ() / CHUNK_SIZE;
        int startChunkX = playerChunkX - MAP_CHUNKS_X / 2 + chunkOffsetX;
        int startChunkZ = playerChunkZ - MAP_CHUNKS_Z / 2 + chunkOffsetZ;
        
        int playerMapX = mapX + (playerChunkX - startChunkX) * CHUNK_PIXEL_SIZE + CHUNK_PIXEL_SIZE / 2;
        int playerMapZ = mapY + (playerChunkZ - startChunkZ) * CHUNK_PIXEL_SIZE + CHUNK_PIXEL_SIZE / 2;
        
        // Desenhar marcador do jogador (círculo azul)
        drawCircle(playerMapX, playerMapZ, 3, 0xFF0080FF);
        
        // Desenhar ponto central
        Gui.drawRect(playerMapX - 1, playerMapZ - 1, playerMapX + 1, playerMapZ + 1, 0xFFFFFFFF);
    }
    
    private void drawMapBorder() {
        // Borda externa
        Gui.drawRect(mapX - 2, mapY - 2, mapX + MAP_WIDTH + 2, mapY - 1, 0xFF000000);
        Gui.drawRect(mapX - 2, mapY + MAP_HEIGHT + 1, mapX + MAP_WIDTH + 2, mapY + MAP_HEIGHT + 2, 0xFF000000);
        Gui.drawRect(mapX - 2, mapY - 2, mapX - 1, mapY + MAP_HEIGHT + 2, 0xFF000000);
        Gui.drawRect(mapX + MAP_WIDTH + 1, mapY - 2, mapX + MAP_WIDTH + 2, mapY + MAP_HEIGHT + 2, 0xFF000000);
    }
    
    private void drawCircle(int centerX, int centerY, int radius, int color) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                if (x * x + y * y <= radius * radius) {
                    Gui.drawRect(centerX + x, centerY + y, centerX + x + 1, centerY + y + 1, color);
                }
            }
        }
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1 || keyCode == 50) { // ESC ou M
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
