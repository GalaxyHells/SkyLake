package com.galaxyhells.skylake.listener;

import com.galaxyhells.skylake.features.AutoLogin;
import com.galaxyhells.skylake.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginListener {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final Pattern LOGIN_COMMAND_PATTERN = Pattern.compile("^/login\\s+(.+)$");
    private static final Pattern REGISTER_COMMAND_PATTERN = Pattern.compile("^/register\\s+(\\S+)\\s+(\\S+)$");
    private static boolean lastCommandWasLogin = false;
    private static String lastPassword = null;
    
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!ConfigHandler.autoLogin || mc.thePlayer == null) {
            return;
        }
        
        String message = event.message.getUnformattedText();
        
        // Detecta se o jogador enviou um comando de login
        if (message.startsWith("/login ")) {
            Matcher matcher = LOGIN_COMMAND_PATTERN.matcher(message);
            if (matcher.matches()) {
                lastPassword = matcher.group(1);
                lastCommandWasLogin = true;
                return;
            }
        }
        
        // Detecta se o jogador enviou um comando de registro
        if (message.startsWith("/register ")) {
            Matcher matcher = REGISTER_COMMAND_PATTERN.matcher(message);
            if (matcher.matches()) {
                String password = matcher.group(1);
                String confirmPassword = matcher.group(2);
                if (password.equals(confirmPassword)) {
                    lastPassword = password;
                    AutoLogin.setPasswordForAutoSave(lastPassword);
                }
                return;
            }
        }
        
        // Detecta mensagem de sucesso no login após comando manual
        if (lastCommandWasLogin && lastPassword != null) {
            String lowerMessage = message.toLowerCase();
            if (lowerMessage.contains("login realizado com sucesso") || 
                lowerMessage.contains("logado com sucesso") ||
                lowerMessage.contains("login successful")) {
                
                String playerName = mc.thePlayer.getName();
                AutoLogin.saveCredentials(playerName, lastPassword);
                
                mc.thePlayer.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.GREEN + "[SkyLake] Senha salva automaticamente para " + playerName + 
                    "! Use /autologin remove para remover."));
                
                lastCommandWasLogin = false;
                lastPassword = null;
            } else if (lowerMessage.contains("senha incorreta") || 
                      lowerMessage.contains("login falhou") ||
                      lowerMessage.contains("login failed")) {
                lastCommandWasLogin = false;
                lastPassword = null;
            }
        }
    }
}
