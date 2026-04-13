package com.galaxyhells.skylake.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.Minecraft;

public class BuyCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "comprar";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/comprar <item>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("§cUso: /comprar <item>"));
            return;
        }

        // Junta todos os argumentos em uma string
        StringBuilder itemName = new StringBuilder();
        for (String arg : args) {
            if (itemName.length() > 0) {
                itemName.append(" ");
            }
            itemName.append(arg);
        }

        String item = itemName.toString();
        
        // Valida o tamanho total da mensagem (incluindo cores)
        String message = createBuyMessage(item);
        if (message.length() > 100) {
            sender.addChatMessage(new ChatComponentText("§cMensagem muito longa! Máximo 100 caracteres. Sua mensagem tem " + message.length() + " caracteres."));
            return;
        }

        // Envia o anúncio no chat
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message);

        // Converte os simbolos de cores da mensagem
        String convertedMessage = message.replace("&", "§");
        
        // Confirmação para o jogador
        sender.addChatMessage(new ChatComponentText("§a[SkyLake] Anúncio enviado: " + convertedMessage));
    }

    private String createBuyMessage(String item) {
        // Mensagem colorida de compra
        // §6 = Dourado, §e = Amarelo, §a = Verde, §b = Ciano, §f = Branco
        return "&6[&eCOMPRO&6] &a" + item + " &f- &b/tell!";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // Permitido para todos
    }
}
