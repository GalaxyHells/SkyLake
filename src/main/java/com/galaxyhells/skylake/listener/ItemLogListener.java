package com.galaxyhells.skylake.listener;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.features.hud.itemlog.ItemLogFeature;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Listener para detectar quando o jogador coleta itens
 */
public class ItemLogListener {
    private final ItemLogFeature itemLogFeature;
    private ItemStack[] previousInventory = null;
    private int cooldown = 0;

    public ItemLogListener(ItemLogFeature itemLogFeature) {
        this.itemLogFeature = itemLogFeature;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        // Verifica se a feature está ativada
        if (Boolean.FALSE.equals(SkyLake.optionsService.get(OptionType.ITEM_LOG))) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        // Cooldown para evitar spam
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        // Inicializa o inventário anterior na primeira execução
        if (previousInventory == null) {
            previousInventory = mc.thePlayer.inventory.mainInventory.clone();
            return;
        }

        // Compara inventário atual com anterior
        ItemStack[] currentInventory = mc.thePlayer.inventory.mainInventory;
        
        for (int i = 0; i < currentInventory.length; i++) {
            ItemStack current = currentInventory[i];
            ItemStack previous = previousInventory[i];

            // Se o slot atual tem itens e o anterior não, ou aumentou a quantidade
            if (current != null && (previous == null || current.stackSize > previous.stackSize)) {
                int addedAmount = (previous == null) ? current.stackSize : (current.stackSize - previous.stackSize);
                if (addedAmount > 0) {
                    // Cria uma cópia do item com a quantidade adicionada
                    ItemStack logStack = current.copy();
                    logStack.stackSize = addedAmount;
                    itemLogFeature.addEntry(logStack);
                    cooldown = 5; // Cooldown de 5 ticks (250ms)
                    break;
                }
            }
        }

        // Atualiza o inventário anterior
        previousInventory = currentInventory.clone();
    }
}
