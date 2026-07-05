package com.galaxyhells.skylake.commands;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.hud.ConfigUI;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class OpenConfigCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "config";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/config - Abre o menu de configuração do SkyLake";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (SkyLake.instance.optionsService != null) {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigUI(SkyLake.instance.optionsService));
        } else {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "OptionsService não inicializado!"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
