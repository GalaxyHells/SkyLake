package com.galaxyhells.skylake.features.inventory;

import com.galaxyhells.skylake.data.ItemDataManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TooltipListener {

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        ItemStack item = event.itemStack;
        if (item == null) return;

        // Pega o nome exato que aparece para o jogador (com as cores §)
        String displayName = item.getDisplayName();

        // Passa para o nosso gerenciador que vai limpar e buscar
        double price = ItemDataManager.getPriceByName(displayName);

        if (price > 0) {
            event.toolTip.add(""); // Linha vazia de espaçamento
            event.toolTip.add("§7Preço de Venda: §e" + formatPrice(price) + " coins");
        }
    }

    private String formatPrice(double price) {
        return String.format("%,.0f", price).replace(",", ".");
    }
}