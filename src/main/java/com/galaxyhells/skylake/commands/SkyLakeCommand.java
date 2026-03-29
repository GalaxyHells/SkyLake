package com.galaxyhells.skylake.commands;

import com.galaxyhells.skylake.config.SkyLakeConfig;
import com.galaxyhells.skylake.data.TreasureLogger;
import com.galaxyhells.skylake.features.render.TreasureGui;
import com.galaxyhells.skylake.features.render.TreasureWaypoint;
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
                sender.addChatMessage(new ChatComponentText("§cUso: /skylake guia [tesouros/canarion/stop]"));
                return;
            }

            String sub = args[1].toLowerCase();

            if (sub.equals("stop") || sub.equals("parar")) {
                TreasureGui.INSTANCE.stopRoute();
                sender.addChatMessage(new ChatComponentText("§b[SkyLake] §cGuia encerrado."));
                return;
            }

            // Identifica onde o player está para abrir a pasta certa
            String world = com.galaxyhells.skylake.utils.WorldUtils.getCurrentArea();
            List<BlockPos> lista;
            String prefixoChat;
            String fileName;



            switch (sub) {
                case "tesouros":
                    // O nome do arquivo passa a ser apenas o nome do mundo (ex: vilarejo.json)
                    fileName = world + ".json";
                    prefixoChat = "[Baú do Tesouro]";
                    break;
                case "canarion":
                    // Se canarion seguir a mesma lógica de arquivos únicos:
                    fileName = world + "_canarion.json";
                    prefixoChat = "[Canárion - " + world.toUpperCase() + "]";
                    break;
                default:
                    sender.addChatMessage(new ChatComponentText("§cTipo inválido! Use: tesouros, canarion ou stop."));
                    return;
            }

            lista = TreasureLogger.getCoordsFromFile(fileName);

            if (lista.isEmpty()) {
                sender.addChatMessage(new ChatComponentText("§c[SkyLake] Nenhuma coordenada encontrada em: §e" + world));
                return;
            }

            // Ordenação por proximidade
            net.minecraft.entity.player.EntityPlayer player = Minecraft.getMinecraft().thePlayer;
//            lista.sort((p1, p2) -> {
//                double d1 = player.getDistanceSq(p1.getX(), p1.getY(), p1.getZ());
//                double d2 = player.getDistanceSq(p2.getX(), p2.getY(), p2.getZ());
//                return Double.compare(d1, d2);
//            });

            TreasureGui.INSTANCE.startRoute(lista, prefixoChat);
            sender.addChatMessage(new ChatComponentText("§b[SkyLake] §aGuia iniciado no mundo: §e" + world + " §7(" + lista.size() + " locais)"));
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

        // Se não cair em nenhum comando acima
        sender.addChatMessage(new ChatComponentText("§cComando desconhecido. Use /skylake para abrir o menu."));
    }
}