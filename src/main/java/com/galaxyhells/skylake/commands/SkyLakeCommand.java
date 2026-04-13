package com.galaxyhells.skylake.commands;

import com.galaxyhells.skylake.config.SkyLakeConfig;
import com.galaxyhells.skylake.data.TreasureLogger;
import com.galaxyhells.skylake.features.render.treasure.TreasureGui;
import com.galaxyhells.skylake.features.render.treasure.TreasureWaypoint;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.List;

public class SkyLakeCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "skylake";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/skylake [guia/inspect/treasurewaypoints]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft.getMinecraft().displayGuiScreen(new SkyLakeConfig());
            });
            return;
        }

        // --- SUBCOMANDO: GUIA ---
        if (args[0].equalsIgnoreCase("guia")) {
            if (args.length < 2) {
                sender.addChatMessage(new ChatComponentText("§cUso: /skylake guia [tesouros/canarion/stop] <todos/ordem/reset>"));
                return;
            }

            String sub = args[1].toLowerCase();

            if (sub.equals("stop") || sub.equals("parar")) {
                TreasureGui.INSTANCE.stopRoute();
                sender.addChatMessage(new ChatComponentText("§b[SkyLake] §cGuia encerrado."));
                return;
            }

            String world = com.galaxyhells.skylake.utils.WorldUtils.getCurrentArea();
            String prefixoChat;
            String fileName;

            switch (sub) {
                case "tesouros":
                    fileName = world + ".json";
                    prefixoChat = "[Baú do Tesouro]";
                    break;
                case "canarion":
                    fileName = world + "_canarion.json";
                    prefixoChat = "[Canárion - " + world.toUpperCase() + "]";
                    break;
                default:
                    sender.addChatMessage(new ChatComponentText("§cTipo inválido! Use: tesouros, canarion ou stop."));
                    return;
            }

            // --- LÓGICA DE RESET ---
            if (args.length >= 3 && args[2].equalsIgnoreCase("reset")) {
                TreasureLogger.resetCollected(fileName);
                TreasureGui.INSTANCE.stopRoute();
                sender.addChatMessage(new ChatComponentText("§b[SkyLake] §7Progresso de §e" + world + " (" + sub + ") §7foi resetado!"));
                return;
            }

            // Carrega a lista original
            List<BlockPos> lista = TreasureLogger.getCoordsFromFile(fileName);

            if (lista.isEmpty()) {
                sender.addChatMessage(new ChatComponentText("§c[SkyLake] Nenhuma coordenada encontrada para: §e" + world));
                return;
            }

            // --- FILTRAGEM DE COLETADOS ---
            // Remove da lista o que já está salvo no arquivo _coletados.json
            List<BlockPos> coletados = TreasureLogger.getCoordsFromFile(fileName.replace(".json", "_coletados.json"));
            lista.removeIf(coletados::contains);

            if (lista.isEmpty()) {
                sender.addChatMessage(new ChatComponentText("§b[SkyLake] §aVocê já coletou todos os itens de: §e" + world));
                return;
            }

            // --- ARGUMENTOS EXTRAS (todos / ordem) ---
            if (args.length >= 3) {
                String modo = args[2].toLowerCase();

                if (modo.equals("todos") || modo.equals("all")) {
                    // MUDANÇA AQUI: Agora passa a variável 'fileName' também!
                    TreasureGui.INSTANCE.showAll(lista, fileName);
                    return;
                }

                if (modo.equals("ordem") || modo.equals("sort")) {
                    net.minecraft.entity.player.EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                    lista.sort((p1, p2) -> {
                        double d1 = player.getDistanceSq(p1.getX(), p1.getY(), p1.getZ());
                        double d2 = player.getDistanceSq(p2.getX(), p2.getY(), p2.getZ());
                        return Double.compare(d1, d2);
                    });
                    sender.addChatMessage(new ChatComponentText("§b[SkyLake] §7Lista reordenada por proximidade atual."));
                }
            }

            // Inicia a rota passando o fileName para o TreasureGui poder salvar o progresso depois
            TreasureGui.INSTANCE.startRoute(lista, prefixoChat, fileName);
            sender.addChatMessage(new ChatComponentText("§b[SkyLake] §aGuia iniciado: §e" + world + " §7(" + lista.size() + " restantes)"));
            return;
        }

        // --- SUBCOMANDO: INSPECT ---
        if (args[0].equalsIgnoreCase("inspect")) {
            com.galaxyhells.skylake.utils.SkullUtils.inspectNearby();
            return;
        }

        // --- SUBCOMANDO: WAYPOINTS ---
        if (args[0].equalsIgnoreCase("treasurewaypoints") && args.length >= 2) {
            if (args[1].equalsIgnoreCase("clear")) {
                TreasureWaypoint.INSTANCE.clear();
                sender.addChatMessage(new ChatComponentText("§b[SkyLake] §7Waypoints limpos."));
                return;
            }
        }

        sender.addChatMessage(new ChatComponentText("§cComando desconhecido. Use /skylake para abrir o menu."));
    }
}