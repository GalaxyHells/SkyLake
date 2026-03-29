package com.galaxyhells.skylake.features.render;

import com.galaxyhells.skylake.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TreasureWaypoint {

    public static final TreasureWaypoint INSTANCE = new TreasureWaypoint();

    private double targetX, targetY, targetZ;
    private boolean hasWaypoint = false;

    // Adicionamos o "volatile" para garantir que a renderização veja a mudança feita pelo Tick
    private volatile boolean shouldRender = false;

    public void clear() {
        this.shouldRender = false;
        this.hasWaypoint = false;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        // Se não tiver alvo ou a renderização estiver desligada, sai fora
        if (!shouldRender || !hasWaypoint) return;

        // Renderiza no centro do bloco (+0.5) para alinhar com a cabeça/armorstand
        RenderUtils.drawCustomBox(targetX + 0.5, targetY + 1, targetZ + 0.5, 1.0f, 1.0f, 0xFF00FFFF);
    }

    public void setTarget(BlockPos pos) {
        if (pos == null) {
            this.clear();
            return;
        }

        this.targetX = pos.getX();
        this.targetY = pos.getY();
        this.targetZ = pos.getZ();

        this.hasWaypoint = true;
        this.shouldRender = true; // Ativa a renderização explicitamente
    }
}