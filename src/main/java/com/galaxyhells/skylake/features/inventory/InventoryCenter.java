package com.galaxyhells.skylake.features.inventory;

import com.galaxyhells.skylake.utils.OptionType;
import com.galaxyhells.skylake.SkyLake;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class InventoryCenter {

    private static final String FIELD_GUI_LEFT = "field_147003_i";
    private static final String FIELD_X_SIZE = "field_146999_f";

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        // Usamos Phase.END para garantir que o Minecraft processou tudo antes de nós corrigirmos
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getMinecraft();

        // Verifica se a feature está ligada
        Boolean enabled = SkyLake.optionsService.get(OptionType.CENTER_INVENTORY);
        if (enabled == null || !enabled) return;

        // Se estiver com inventário aberto
        if (mc.currentScreen instanceof GuiContainer) {
            GuiContainer gui = (GuiContainer) mc.currentScreen;

            // Obtenha os valores
            Integer currentLeft = ReflectionHelper.getPrivateValue(GuiContainer.class, gui, FIELD_GUI_LEFT);
            int xSize = ReflectionHelper.getPrivateValue(GuiContainer.class, gui, FIELD_X_SIZE);
            int centeredLeft = (gui.width - xSize) / 2;

            // Agora comparamos os dois Integers corretamente (o Java faz o unboxing automático)
            if (currentLeft != null && currentLeft != centeredLeft) {
                ReflectionHelper.setPrivateValue(GuiContainer.class, gui, centeredLeft, FIELD_GUI_LEFT);
            }
        }
    }
}