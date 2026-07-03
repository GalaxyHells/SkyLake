package com.galaxyhells.skylake.features.inventory;

import com.galaxyhells.skylake.SkyLake;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.lang.reflect.Field;

import static com.galaxyhells.skylake.utils.KeybindManager.lockSlotKey;

public class SlotLockFeature {

    // ==========================================
    // CACHE DE REFLECTION PARA ALTA PERFORMANCE
    // ==========================================
    private static Field theSlotField;
    private static Field guiLeftField;
    private static Field guiTopField;

    static {
        try {
            // "field_147006_u" = theSlot
            // "field_147003_i" = guiLeft
            // "field_147009_r" = guiTop
            theSlotField = ReflectionHelper.findField(GuiContainer.class, "theSlot", "field_147006_u");
            guiLeftField = ReflectionHelper.findField(GuiContainer.class, "guiLeft", "field_147003_i");
            guiTopField = ReflectionHelper.findField(GuiContainer.class, "guiTop", "field_147009_r");

            theSlotField.setAccessible(true);
            guiLeftField.setAccessible(true);
            guiTopField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // 1. EVENTOS DO INVENTÁRIO (TECLADO)
    // ==========================================
    @SubscribeEvent
    public void onKeyboardInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if (!(event.gui instanceof GuiContainer)) return;
        GuiContainer gui = (GuiContainer) event.gui;
        Minecraft mc = Minecraft.getMinecraft();

        Slot hoveredSlot;
        try {
            hoveredSlot = (Slot) theSlotField.get(gui);
        } catch (Exception e) { return; }

        int keyCode = Keyboard.getEventKey();
        boolean isKeyDown = Keyboard.getEventKeyState();

        if (hoveredSlot != null && hoveredSlot.inventory instanceof InventoryPlayer) {
            int slotIndex = hoveredSlot.getSlotIndex();

            // Trancar/Destrancar o slot ao apertar a tecla configurada (L)
            if (keyCode == lockSlotKey.getKeyCode() && isKeyDown) {
                if (SkyLake.optionsService.isSlotLocked(slotIndex)) {
                    SkyLake.optionsService.removeLockedSlot(slotIndex);
                    mc.thePlayer.playSound("random.orb", 1.0F, 1.0F);
                } else {
                    SkyLake.optionsService.addLockedSlot(slotIndex);
                    mc.thePlayer.playSound("random.anvil_use", 0.5F, 1.0F);
                }
                event.setCanceled(true);
                return;
            }

            // Bloquear a tecla de DROP (Q) em cima de um slot trancado
            if (keyCode == mc.gameSettings.keyBindDrop.getKeyCode() && isKeyDown) {
                if (SkyLake.optionsService.isSlotLocked(slotIndex)) {
                    event.setCanceled(true);
                    mc.thePlayer.playSound("note.bass", 1.0F, 1.0F);
                    return;
                }
            }
        }

        // Bloquear o truque de trocar itens da Hotbar apertando de 1 a 9 em cima de outro item
        if (isKeyDown) {
            for (int i = 0; i < 9; i++) {
                if (keyCode == mc.gameSettings.keyBindsHotbar[i].getKeyCode()) {
                    if (SkyLake.optionsService.isSlotLocked(i)) {
                        event.setCanceled(true);
                        mc.thePlayer.playSound("note.bass", 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    // ==========================================
    // 2. EVENTOS DO INVENTÁRIO (MOUSE)
    // ==========================================
    @SubscribeEvent
    public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!(event.gui instanceof GuiContainer)) return;
        if (!Mouse.getEventButtonState()) return; // Ignora se o botão foi solto

        GuiContainer gui = (GuiContainer) event.gui;
        Slot hoveredSlot;
        try {
            hoveredSlot = (Slot) theSlotField.get(gui);
        } catch (Exception e) { return; }

        // Impede de clicar em slots trancados (bloqueia pegar ou arrastar)
        if (hoveredSlot != null && hoveredSlot.inventory instanceof InventoryPlayer) {
            if (SkyLake.optionsService.isSlotLocked(hoveredSlot.getSlotIndex())) {
                event.setCanceled(true);
                Minecraft.getMinecraft().thePlayer.playSound("note.bass", 1.0F, 1.0F);
            }
        }
    }

    // ==========================================
    // 3. BLOQUEIO DE DROP NO MUNDO (FORA DA GUI)
    // ==========================================
    @SubscribeEvent
    public void onKeyInputInGame(InputEvent.KeyInputEvent event) {
        preventDropInGame();
    }

    @SubscribeEvent
    public void onMouseInputInGame(InputEvent.MouseInputEvent event) {
        preventDropInGame();
    }

    private void preventDropInGame() {
        Minecraft mc = Minecraft.getMinecraft();

        // Aplica apenas se estiver no mundo, sem NENHUMA janela aberta
        if (mc.thePlayer == null || mc.currentScreen != null) return;

        int currentSlot = mc.thePlayer.inventory.currentItem;

        if (SkyLake.optionsService.isSlotLocked(currentSlot)) {
            boolean intercepted = false;

            // "Drena" os cliques na tecla de drop, interceptando a ação antes do jogo processar
            while (mc.gameSettings.keyBindDrop.isPressed()) {
                intercepted = true;
            }

            if (intercepted) {
                mc.thePlayer.playSound("note.bass", 1.0F, 1.0F);
            }
        }
    }

    // ==========================================
    // 4. RENDERIZAÇÃO (Quadrado vermelho na GUI)
    // ==========================================
    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.gui instanceof GuiContainer)) return;
        GuiContainer gui = (GuiContainer) event.gui;

        int guiLeft, guiTop;
        try {
            guiLeft = guiLeftField.getInt(gui);
            guiTop = guiTopField.getInt(gui);
        } catch (Exception e) { return; }

        // Desenha o overlay vermelho sobre cada slot trancado
        for (Slot slot : gui.inventorySlots.inventorySlots) {
            if (slot.inventory instanceof InventoryPlayer && SkyLake.optionsService.isSlotLocked(slot.getSlotIndex())) {
                int x = guiLeft + slot.xDisplayPosition;
                int y = guiTop + slot.yDisplayPosition;
                net.minecraft.client.gui.Gui.drawRect(x, y, x + 16, y + 16, 0x60FF0000);
            }
        }
    }
}