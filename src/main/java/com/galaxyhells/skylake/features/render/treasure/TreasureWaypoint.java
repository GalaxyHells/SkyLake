package com.galaxyhells.skylake.features.render.treasure;

import com.galaxyhells.skylake.utils.RenderUtils2;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class TreasureWaypoint {

    public static final TreasureWaypoint INSTANCE = new TreasureWaypoint();

    // Agora armazenamos uma lista de pontos
    private final List<BlockPos> targets = new ArrayList<>();
    private volatile boolean shouldRender = false;

    public void clear() {
        synchronized (targets) {
            this.targets.clear();
        }
        this.shouldRender = false;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!shouldRender || targets.isEmpty()) return;

        synchronized (targets) {
            for (BlockPos pos : targets) {
                // Renderiza cada caixa.
                // Dica: Use 0x5500FFFF (mais transparente) se forem muitos, para não poluir a visão
                RenderUtils2.drawCustomBox(
                        pos.getX() + 0.5,
                        pos.getY() + 1,
                        pos.getZ() + 0.5,
                        1.0f, 1.0f, 0xFF00FFFF
                );
            }
        }
    }

    // Define apenas um alvo (usado pela rota normal)
    public void setTarget(BlockPos pos) {
        synchronized (targets) {
            this.targets.clear();
            if (pos != null) {
                this.targets.add(pos);
                this.shouldRender = true;
            } else {
                this.shouldRender = false;
            }
        }
    }

    // Define vários alvos ao mesmo tempo (Nova Função)
    public void setMultipleTargets(List<BlockPos> positions) {
        synchronized (targets) {
            this.targets.clear();
            if (positions != null && !positions.isEmpty()) {
                this.targets.addAll(positions);
                this.shouldRender = true;
            } else {
                this.shouldRender = false;
            }
        }
    }

    // Retorna a lista atual de alvos
    public List<BlockPos> getTargets() {
        synchronized (targets) {
            return new ArrayList<>(targets);
        }
    }

    // Remove apenas um alvo específico (apaga a caixa dele)
    public void removeTarget(BlockPos pos) {
        synchronized (targets) {
            targets.remove(pos);
            if (targets.isEmpty()) {
                this.shouldRender = false;
            }
        }
    }
}