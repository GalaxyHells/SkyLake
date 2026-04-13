package com.galaxyhells.skylake.features.render.treasure;

import com.galaxyhells.skylake.data.TreasureLogger;
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
    private String currentFileName = "";

    // Carrega a lista organizada gerada
    public void startRoute(List<BlockPos> sortedCoords, String chatRequirement, String fileName) {
        this.currentFileName = fileName;
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
        BlockPos collectedPos = route.get(currentIndex);

        // SALVA NO ARQUIVO DE COLETADOS
        TreasureLogger.markAsCollected(currentFileName, collectedPos);

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

    /**
     * Renderiza todos os pontos do arquivo sem iniciar uma rota de coleta.
     */
    public void showAll(List<BlockPos> allCoords, String fileName) {
        this.stopRoute(); // Para qualquer rota ativa antes
        this.isActive = false; // Garante que não vai escutar o tick/chat da rota normal
        this.currentFileName = fileName; // Salva o nome do arquivo para gravarmos os cliques!

        TreasureWaypoint.INSTANCE.setMultipleTargets(allCoords);
        Minecraft.getMinecraft().thePlayer.addChatMessage(
                new ChatComponentText("§b[SkyLake] §aMostrando " + allCoords.size() + " tesouros não coletados!")
        );
    }

    // ================= EVENTOS DE CLIQUE ================= //

    // Se você clicar com o botão direito num bloco
    @SubscribeEvent
    public void onPlayerInteract(net.minecraftforge.event.entity.player.PlayerInteractEvent event) {
        if (event.action == net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            checkAndCollect(event.pos);
        }
    }

    // Se você clicar com o botão direito numa entidade (ex: ArmorStand de tesouro pirata)
    @SubscribeEvent
    public void onEntityInteract(net.minecraftforge.event.entity.player.EntityInteractEvent event) {
        if (event.target != null) {
            checkAndCollect(event.target.getPosition());
        }
    }

    // Lógica que verifica se você clicou num tesouro marcado
    private void checkAndCollect(BlockPos clickPos) {
        // Se não tiver nenhum arquivo de guia "Todos" rodando, ignora
        if (this.currentFileName == null || this.currentFileName.isEmpty()) return;

        List<BlockPos> targets = TreasureWaypoint.INSTANCE.getTargets();
        BlockPos toRemove = null;

        for (BlockPos target : targets) {
            // Verifica se o bloco clicado é o tesouro ou está muito perto (raio de 2 blocos)
            // Isso evita bugs caso a entidade esteja levemente fora do centro do bloco
            if (target.distanceSq(clickPos) <= 4.0) {
                toRemove = target;
                break;
            }
        }

        // Se achou o tesouro no clique, remove a caixa e salva no JSON
        if (toRemove != null) {
            TreasureWaypoint.INSTANCE.removeTarget(toRemove);
            TreasureLogger.markAsCollected(this.currentFileName, toRemove);
            Minecraft.getMinecraft().thePlayer.addChatMessage(
                    new ChatComponentText("§b[SkyLake] §aTesouro coletado e salvo!")
            );
        }
    }
}
