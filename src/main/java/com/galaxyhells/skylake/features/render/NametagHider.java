package com.galaxyhells.skylake.features.render;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.item.EntityArmorStand;

public class NametagHider {

    public static final NametagHider INSTANCE = new NametagHider();

    // Usamos prioridade para não dar conflito com o outro mod que desenha a nametag nova
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderNametag(RenderLivingEvent.Specials.Pre event) {

        // Verifica se a feature está ativada no seu sistema de opções
        if (Boolean.FALSE.equals(SkyLake.optionsService.get(OptionType.HIDE_NAMETAGS))) {
            return;
        }

        // DICA DE OURO PARA SKYBLOCK: 
        // No Hypixel, nametags customizadas frequentemente são Armor Stands invisíveis.
        // Se a nametag padrão for da própria entidade (como um Enderman), cancelamos.
        // É bom garantir que não estamos escondendo nametags de jogadores acidentalmente.

        // Se o evento já foi cancelado por outra classe, ignoramos
        if (event.isCanceled()) {
            return;
        }

        if (event.entity instanceof EntityLiving || event.entity instanceof EntityArmorStand) {
            // Cancela a renderização padrão (texto com sombra)
            event.setCanceled(true);
        }
    }
}