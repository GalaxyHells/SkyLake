package com.galaxyhells.skylake.commands;

import com.galaxyhells.skylake.config.SkyLakeConfig;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.client.Minecraft;

public class SkyLakeCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "skylake";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/skylake";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // 0 permite que qualquer jogador use, mesmo sem OP
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // Força a permissão de uso
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @Override
            public void run() {
                // Fecha o chat antes de abrir a config
                Minecraft.getMinecraft().thePlayer.closeScreen();
                Minecraft.getMinecraft().displayGuiScreen(new SkyLakeConfig());
            }
        });
    }
}