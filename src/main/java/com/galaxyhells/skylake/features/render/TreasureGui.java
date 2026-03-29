package com.galaxyhells.skylake.features.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.ArrayList;
import java.util.List;

public class TreasureGui {

    public static final TreasureGui INSTANCE = new TreasureGui();

    private List<BlockPos> route = new ArrayList<>();
    private int currentIndex = -1;
    private boolean isActive = false;
    private String chatRequirement = "";

    // Carrega a lista organizada gerada
    public void startRoute(List<BlockPos> sortedCoords, String chatRequirement) {
        this.route = sortedCoords;
        this.chatRequirement = chatRequirement; // Define o que procurar no chat
        this.currentIndex = 0;
        this.isActive = true;
        sendProgressMessage();
    }

    /**
     * Interrompe a rota do guia atual, limpa os dados e desativa o waypoint.
     */
    public void stopRoute() {
        // 1. Desativa a lógica de escuta do Tick e do Chat
        this.isActive = false;

        // 2. Limpa os dados da rota (opcional, mas boa prática)
        if (this.route != null) {
            this.route.clear();
        }
        this.currentIndex = -1;
        this.chatRequirement = "";

        // 3. IMPORTANTE: Diz ao renderizador para parar de mostrar o quadrado
        TreasureWaypoint.INSTANCE.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!isActive || currentIndex >= route.size() || event.phase != TickEvent.Phase.START) return;

        // Renderiza o Waypoint da coordenada atual usando seu RenderUtils
        BlockPos current = route.get(currentIndex);
        // Aqui você usa a lógica que já funciona no seu mod para mostrar o "X" ou Box
        TreasureWaypoint.INSTANCE.setTarget(current);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (!isActive || currentIndex >= route.size()) return;

        String msg = event.message.getUnformattedText();

        // Agora ele verifica o requisito dinâmico
        if (msg.contains(chatRequirement)) {
            BlockPos target = route.get(currentIndex);
            double distSq = Minecraft.getMinecraft().thePlayer.getDistanceSq(target);

            if (distSq < 16.0) { // Raio de 4 bloco (3^2 = 16) // Raio de 3 blocos (3^2 = 9)
                markAsCollected();
            }
        }
    }
    //if (msg.startsWith("[Canárion] Você achou um Presente do Canário!") || msg.startsWith("[Canárion] Parece que você já coletou este Presente do Canário.")) {

    private void markAsCollected() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(
                new ChatComponentText("§b[SkyLake] §6Tesouro #" + (currentIndex + 1) + " coletado!")
        );

        currentIndex++;

        if (currentIndex < route.size()) {
            sendProgressMessage();
        } else {
            isActive = false;
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new ChatComponentText("§b[SkyLake] §a§lTodos os tesouros foram coletados!")
            );
            TreasureWaypoint.INSTANCE.clear();
        }
    }

    private void sendProgressMessage() {
        BlockPos next = route.get(currentIndex);
        Minecraft.getMinecraft().thePlayer.addChatMessage(
                new ChatComponentText("§b[SkyLake] §7Próximo tesouro em: §e" +
                        next.getX() + ", " + next.getY() + ", " + next.getZ() +
                        " §8(" + (currentIndex + 1) + "/" + route.size() + ")")


        );
    }
}