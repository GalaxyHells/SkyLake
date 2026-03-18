package com.galaxyhells.skylake.features.inventory;

import com.galaxyhells.skylake.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.HashSet;
import java.util.Set;

import static com.galaxyhells.skylake.config.ConfigHandler.lockedSlots;

public class SlotLockFeature {

    // Guarda os números dos slots que estão trancados (0-8 Hotbar, 9-35 Inventário)
    //public static final Set<Integer> lockedSlots = new HashSet<>();

    // ==========================================
    // 1. SISTEMA DE TRANCAR/DESTRANCAR E BLOQUEIO DE TECLADO
    // ==========================================
    @SubscribeEvent
    public void onKeyboardInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {
        if (!(event.gui instanceof GuiContainer)) return;
        GuiContainer gui = (GuiContainer) event.gui;

        // Usa Reflection para pegar o Slot que está debaixo do mouse (a variável 'theSlot' é privada no 1.8.9)
        Slot hoveredSlot;
        try {
            hoveredSlot = ReflectionHelper.getPrivateValue(GuiContainer.class, gui, "theSlot", "field_147006_u");
        } catch (Exception e) { return; }

        if (hoveredSlot == null || !(hoveredSlot.inventory instanceof InventoryPlayer)) return;

        int slotIndex = hoveredSlot.getSlotIndex();

        // Se apertou "L"
        if (Keyboard.getEventKey() == Keyboard.KEY_L && Keyboard.getEventKeyState()) {
            if (lockedSlots.contains(slotIndex)) {
                lockedSlots.remove(slotIndex);
                Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0F, 1.0F);
            } else {
                lockedSlots.add(slotIndex);
                Minecraft.getMinecraft().thePlayer.playSound("random.anvil_use", 0.5F, 1.0F);
            }

            // SALVA NO ARQUIVO IMEDIATAMENTE!
            ConfigHandler.saveConfig();

            event.setCanceled(true);
            return;
        }

        // Se tentou apertar a tecla de DROP (Geralmente "Q") no inventário
        int dropKey = Minecraft.getMinecraft().gameSettings.keyBindDrop.getKeyCode();
        if (Keyboard.getEventKey() == dropKey && Keyboard.getEventKeyState()) {
            if (lockedSlots.contains(slotIndex)) {
                event.setCanceled(true); // BLOQUEIA O DROP
                Minecraft.getMinecraft().thePlayer.playSound("note.bass", 1.0F, 1.0F); // Toca som de erro
            }
        }
    }

    // ==========================================
    // 2. BLOQUEIO DE CLIQUES (Para não arrastar o item pra fora)
    // ==========================================
    @SubscribeEvent
    public void onMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!(event.gui instanceof GuiContainer)) return;
        if (!Mouse.getEventButtonState()) return; // Só ativa quando clica o botão

        GuiContainer gui = (GuiContainer) event.gui;
        Slot hoveredSlot;
        try {
            hoveredSlot = ReflectionHelper.getPrivateValue(GuiContainer.class, gui, "theSlot", "field_147006_u");
        } catch (Exception e) { return; }

        if (hoveredSlot != null && hoveredSlot.inventory instanceof InventoryPlayer) {
            if (lockedSlots.contains(hoveredSlot.getSlotIndex())) {
                event.setCanceled(true); // BLOQUEIA O CLIQUE NO ITEM
                Minecraft.getMinecraft().thePlayer.playSound("note.bass", 1.0F, 1.0F);
            }
        }
    }

    // ==========================================
    // 3. BLOQUEIO DE DROP NA HOTBAR
    // ==========================================
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) return;

        // Pegamos o slot que o jogador está segurando agora (0-8)
        int currentSlot = mc.thePlayer.inventory.currentItem;

        // Se o slot atual estiver na nossa lista de trancados do ConfigHandler
        if (com.galaxyhells.skylake.config.ConfigHandler.lockedSlots.contains(currentSlot)) {

            // Verificamos se a tecla de drop (Q) está sendo pressionada
            if (mc.gameSettings.keyBindDrop.isKeyDown()) {

                // TRUQUE MESTRE: Resetamos o estado interno da tecla.
                // Isso faz o Minecraft pensar que você soltou a tecla instantaneamente.
                net.minecraft.client.settings.KeyBinding.setKeyBindState(mc.gameSettings.keyBindDrop.getKeyCode(), false);

                // E limpamos o contador de cliques (pressTime) via Reflection para garantir
                try {
                    java.lang.reflect.Field field = net.minecraft.client.settings.KeyBinding.class.getDeclaredField("field_151474_i"); // Obfuscated name para pressTime
                    field.setAccessible(true);
                    field.setInt(mc.gameSettings.keyBindDrop, 0);
                } catch (Exception e) {
                    // Se o reflection falhar, o setKeyBindState já deve ajudar 90% das vezes
                }

                // Toca o som de erro para avisar que está trancado
                mc.thePlayer.playSound("note.bass", 1.0F, 1.0F);
            }
        }
    }

    // ==========================================
    // 4. RENDERIZAÇÃO (Quadrado vermelho em cima do slot)
    // ==========================================
    @SubscribeEvent
    public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.gui instanceof GuiContainer)) return;
        GuiContainer gui = (GuiContainer) event.gui;

        // Pega as coordenadas X e Y da janela do inventário na tela
        int guiLeft, guiTop;
        try {
            guiLeft = ReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiLeft", "field_147003_i");
            guiTop = ReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiTop", "field_147009_r");
        } catch (Exception e) { return; }

        for (Slot slot : gui.inventorySlots.inventorySlots) {
            if (slot.inventory instanceof InventoryPlayer && lockedSlots.contains(slot.getSlotIndex())) {
                int x = guiLeft + slot.xDisplayPosition;
                int y = guiTop + slot.yDisplayPosition;

                // Desenha um retângulo vermelho translúcido por cima do slot
                net.minecraft.client.gui.Gui.drawRect(x, y, x + 16, y + 16, 0x60FF0000);
            }
        }
    }

    // ==========================================
    // 5. BLOQUEIO DE LANÇAMENTO
    // ==========================================
    @SubscribeEvent
    public void onItemDrop(net.minecraftforge.event.entity.item.ItemTossEvent event) {
        // Verifica se quem está dropando é o jogador
        if (event.player instanceof net.minecraft.entity.player.EntityPlayer) {

            // No 1.8.9, o slot selecionado na hotbar é o que conta para o drop externo
            int currentSlot = event.player.inventory.currentItem;

            if (com.galaxyhells.skylake.config.ConfigHandler.lockedSlots.contains(currentSlot)) {
                // CANCELA O EVENTO: O item volta para o seu inventário instantaneamente
                event.setCanceled(true);

                // Devolve o item para a mão do jogador (preveni ghost items)
                event.player.inventory.setInventorySlotContents(currentSlot, event.entityItem.getEntityItem());

                // Toca o som de erro
                event.player.playSound("note.bass", 1.0F, 1.0F);
            }
        }
    }
}