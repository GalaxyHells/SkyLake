package com.galaxyhells.skylake.commands;

import com.galaxyhells.skylake.features.AutoLogin;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class AutoLoginCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "autologin";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/autologin <salvar|remover|status> [senha]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText(
                EnumChatFormatting.RED + "Uso: /autologin <salvar|remover|status> [senha]"));
            return;
        }

        String playerName = Minecraft.getMinecraft().thePlayer.getName();
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "salvar":
                if (args.length < 2) {
                    sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.RED + "Uso: /autologin salvar <senha>"));
                    return;
                }
                String password = args[1];
                AutoLogin.saveCredentials(playerName, password);
                sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GREEN + "[SkyLake] Senha salva para o jogador " + playerName +
                    ". Use /autologin remover para remover."));
                break;

            case "remover":
                if (AutoLogin.hasCredentials(playerName)) {
                    AutoLogin.removeCredentials(playerName);
                    sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.YELLOW + "[SkyLake] Senha removida para o jogador " + playerName));
                } else {
                    sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GRAY + "[SkyLake] Nenhuma senha salva para o jogador " + playerName));
                }
                break;

            case "status":
                if (AutoLogin.hasCredentials(playerName)) {
                    sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GREEN + "[SkyLake] Senha salva para o jogador " + playerName));
                } else {
                    sender.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.RED + "[SkyLake] Nenhuma senha salva para o jogador " + playerName));
                }
                break;

            default:
                sender.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.RED + "Subcomando inválido. Use: save, remove ou status"));
                break;
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // Permitido para todos os jogadores
    }
}
