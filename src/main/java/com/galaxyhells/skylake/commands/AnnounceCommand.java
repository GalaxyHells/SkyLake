package com.galaxyhells.skylake.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.Minecraft;

public class AnnounceCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "anunciar";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/anunciar <comprar|vender> <item>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            sender.addChatMessage(new ChatComponentText("§cUso: /anunciar <comprar|vender> <item>"));
            return;
        }

        String action = args[0].toLowerCase();
        
        // Junta todos os argumentos após o primeiro (nome do item)
        StringBuilder itemName = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (itemName.length() > 0) {
                itemName.append(" ");
            }
            itemName.append(args[i]);
        }

        String item = itemName.toString();
        
        // Cria a mensagem com & para o chat do servidor
        String message = createAnnounceMessage(action, item);
        message = "/g " + message;
        
        // Valida o tamanho total da mensagem (incluindo cores &)
        if (message.length() > 100) {
            sender.addChatMessage(new ChatComponentText("§cMensagem muito longa! Máximo 100 caracteres. Sua mensagem tem " + message.length() + " caracteres."));
            return;
        }

        // Envia o anúncio no chat (com & para o servidor)
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
        
        // Converte & para § apenas para a confirmação interna
        String convertedMessage = message.replace("&", "§");

        sender.addChatMessage(new ChatComponentText("§a[SkyLake] Anúncio enviado: " + convertedMessage));
    }

    private String createAnnounceMessage(String action, String item) {
        // Mensagens com & para o servidor
        if (action.equals("comprar")) {
            return "&6[&eCOMPRO&6] &a" + item + " &f- &b/tell!";
        } else if (action.equals("vender")) {
            return "&6[&eVENDO&6] &a" + item + " &f- &b/tell!";
        } else {
            return null;
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // Permitido para todos
    }
}
