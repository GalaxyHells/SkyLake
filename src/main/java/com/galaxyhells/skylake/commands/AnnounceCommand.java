package com.galaxyhells.skylake.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AnnounceCommand extends CommandBase {

    private static String repeatTipo = null;
    private static String repeatMensagem = null;
    private static long lastAnnounceTime = 0;
    private static final long REPEAT_INTERVAL = 30 * 60 * 1000; // 30 minutos em milissegundos

    @Override
    public String getCommandName() {
        return "anunciar";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/anunciar <vender/comprar> [repetir] [mensagem] ou /anunciar cancelar";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        // Subcomando cancelar
        if (args.length > 0 && args[0].equalsIgnoreCase("cancelar")) {
            if (repeatTipo == null) {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Nenhum anúncio repetitivo ativo."));
                return;
            }
            repeatTipo = null;
            repeatMensagem = null;
            lastAnnounceTime = 0;
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Anúncio repetitivo cancelado."));
            return;
        }

        if (args.length < 2) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Uso: /anunciar <vender/comprar> [repetir] [mensagem] ou /anunciar cancelar"));
            return;
        }

        String tipo = args[0].toLowerCase();
        boolean repeat = false;
        int mensagemStartIndex = 1;

        // Verifica se o segundo argumento é "repetir"
        if (args.length > 1 && args[1].equalsIgnoreCase("repetir")) {
            repeat = true;
            mensagemStartIndex = 2;
        }

        if (mensagemStartIndex >= args.length) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Você precisa fornecer uma mensagem."));
            return;
        }

        StringBuilder mensagem = new StringBuilder();
        for (int i = mensagemStartIndex; i < args.length; i++) {
            if (i > mensagemStartIndex) {
                mensagem.append(" ");
            }
            mensagem.append(args[i]);
        }

        String prefixo;
        if (tipo.equals("vender")) {
            prefixo = EnumChatFormatting.GREEN + "[VENDO] " + EnumChatFormatting.WHITE;
        } else if (tipo.equals("comprar")) {
            prefixo = EnumChatFormatting.BLUE + "[COMPRANDO] " + EnumChatFormatting.WHITE;
        } else {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Tipo inválido! Use: vender ou comprar"));
            return;
        }

        String mensagemFinal = prefixo + mensagem.toString();
        
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage(mensagemFinal);
        }

        // Configura repetição se solicitado
        if (repeat) {
            repeatTipo = tipo;
            repeatMensagem = mensagem.toString();
            lastAnnounceTime = System.currentTimeMillis();
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "Anúncio configurado para repetir a cada 30 minutos. Use /anunciar cancelar para parar."));
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        if (repeatTipo == null || repeatMensagem == null) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAnnounceTime >= REPEAT_INTERVAL) {
            String prefixo;
            if (repeatTipo.equals("vender")) {
                prefixo = EnumChatFormatting.GREEN + "[VENDO] " + EnumChatFormatting.WHITE;
            } else {
                prefixo = EnumChatFormatting.BLUE + "[COMPRANDO] " + EnumChatFormatting.WHITE;
            }

            String mensagemFinal = prefixo + repeatMensagem;
            
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer != null) {
                mc.thePlayer.sendChatMessage(mensagemFinal);
            }
            
            lastAnnounceTime = currentTime;
        }
    }
}
