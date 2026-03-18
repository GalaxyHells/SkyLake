package com.galaxyhells.skylake.commands;

import com.galaxyhells.skylake.config.SkyLakeConfig;
import com.galaxyhells.skylake.features.render.TreasureWaypoint;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class SkyLakeCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "skylake";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/skylake [treasurewaypoints clear]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        // Se digitar apenas /skylake, abre o menu
        if (args.length == 0) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft.getMinecraft().displayGuiScreen(new SkyLakeConfig());
            });
            return;
        }

        // Lógica para /skylake treasurewaypoints clear
        if (args.length >= 2 && args[0].equalsIgnoreCase("treasurewaypoints")) {
            if (args[1].equalsIgnoreCase("clear")) {
                TreasureWaypoint.INSTANCE.clear();
                sender.addChatMessage(new ChatComponentText("§b[SkyLake] §7Waypoints limpos com sucesso!"));
                return;
            }
        }

        sender.addChatMessage(new ChatComponentText("§c[SkyLake] Uso: /skylake treasurewaypoints clear"));
    }
}