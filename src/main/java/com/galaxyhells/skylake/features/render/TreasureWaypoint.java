package com.galaxyhells.skylake.features.render;

import com.galaxyhells.skylake.utils.RenderUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreasureWaypoint {

    // Instância única para o comando conseguir acessar
    public static final TreasureWaypoint INSTANCE = new TreasureWaypoint();

    private double targetX, targetY, targetZ;
    private boolean hasWaypoint = false;
    private static final Pattern COORD_PATTERN = Pattern.compile("at (-?\\d+), (\\d+), (-?\\d+)");

    // Método para limpar o ponto
    public void clear() {
        this.hasWaypoint = false;
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        Matcher matcher = COORD_PATTERN.matcher(msg);

        if (matcher.find()) {
            this.targetX = Double.parseDouble(matcher.group(1));
            this.targetY = Double.parseDouble(matcher.group(2));
            this.targetZ = Double.parseDouble(matcher.group(3));
            this.hasWaypoint = true;
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!hasWaypoint) return;
        RenderUtils.drawCustomBox(targetX, targetY, targetZ, 1.0f, 2.0f, 0xFF00FFFF);
    }
}