package com.galaxyhells.skylake.mixins;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiContainer.class, priority = 1000)
public abstract class MixinGuiContainer {

    @Shadow protected int guiLeft;
    @Shadow protected int xSize;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void onInitGui(CallbackInfo ci) {
        System.out.println("DEBUG: O Mixin de Inventário está rodando!"); // <- ADICIONE ISSO
        Boolean enabled = SkyLake.optionsService.get(OptionType.CENTER_INVENTORY);

        // Verifica se a feature está ligada
        if (enabled != null && enabled) {
            // Re-calcula a centralização ignorando qualquer offset de poções
            // O Minecraft calcula width, xSize, etc. Apenas forçamos o centro.
            GuiContainer self = (GuiContainer) (Object) this;
            this.guiLeft = (self.width - this.xSize) / 2;
        }
    }
}