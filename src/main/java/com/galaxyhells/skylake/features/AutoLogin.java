package com.galaxyhells.skylake.features;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.ModConstants;
import com.galaxyhells.skylake.utils.OptionType;
import com.galaxyhells.skylake.utils.CryptoUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

public class AutoLogin {
    private static final Minecraft mc = Minecraft.getMinecraft();
    
    private static boolean waitingForLogin = false;
    private static boolean waitingForRegister = false;
    private static String currentPassword = null;
    private static int loginAttempts = 0;
    private static long lastLoginAttempt = 0;
    
    private static final ConcurrentHashMap<String, String> savedCredentials = new ConcurrentHashMap<>();
    
    public AutoLogin() {
        loadCredentials();
    }
    
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_LOGIN))) return;

        String originalMessage = event.message.getUnformattedText();
        String message = originalMessage.toLowerCase();
        
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.AUTO_LOGIN)) || mc.thePlayer == null) {
            return;
        }
        
        // Detecta solicitação de login
        if (message.contains(ModConstants.LOGIN_PROMPT)) {
            waitingForLogin = true;
            waitingForRegister = false;
            attemptAutoLogin();
            return;
        }
        
        // Detecta solicitação de registro
        if (message.contains(ModConstants.REGISTER_PROMPT)) {
            waitingForRegister = true;
            waitingForLogin = false;
            return;
        }
        
        // Detecta sucesso no login
        if (message.contains(ModConstants.LOGIN_SUCCESS)) {
            waitingForLogin = false;
            waitingForRegister = false;
            loginAttempts = 0;
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GREEN + ModConstants.AUTOLOGIN_PREFIX + "Login realizado com sucesso!"));
            return;
        }
        
        // Detecta sucesso no registro
        if (message.contains(ModConstants.REGISTER_SUCCESS)) {
            waitingForRegister = false;
            waitingForLogin = false;
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GREEN + ModConstants.AUTOLOGIN_PREFIX + "Registro realizado! Por favor, faça login manualmente uma vez para salvar sua senha."));
            return;
        }
        
        // Detecta falha no login
        if (message.contains("senha incorreta") || message.contains("login falhou")) {
            loginAttempts++;
            if (loginAttempts >= ModConstants.MAX_LOGIN_ATTEMPTS) {
                mc.thePlayer.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.RED + ModConstants.AUTOLOGIN_PREFIX + "Falha no login após " + ModConstants.MAX_LOGIN_ATTEMPTS + " tentativas. Desativando auto-login temporariamente."));
                waitingForLogin = false;
                loginAttempts = 0;
            }
        }
    }
    
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        if (waitingForRegister && currentPassword != null && System.currentTimeMillis() - lastLoginAttempt > ModConstants.LOGIN_COOLDOWN) {
            // Se está esperando registro e temos uma senha, envia comando de registro
            sendCommand("/register " + currentPassword + " " + currentPassword);
            waitingForRegister = false;
            lastLoginAttempt = System.currentTimeMillis();
        }
    }
    
    private void attemptAutoLogin() {
        if (!waitingForLogin) {
            return;
        }
        
        if (System.currentTimeMillis() - lastLoginAttempt < ModConstants.LOGIN_COOLDOWN) {
            return;
        }
        
        String playerName = mc.thePlayer.getName();
        String password = savedCredentials.get(playerName);
        
        if (password != null && !password.isEmpty()) {
            sendCommand("/login " + password);
            lastLoginAttempt = System.currentTimeMillis();
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.AQUA + ModConstants.AUTOLOGIN_PREFIX + "Enviando comando de login..."));
        } else {
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.YELLOW + ModConstants.AUTOLOGIN_PREFIX + "Nenhuma senha salva para este jogador. Faça login manualmente uma vez para salvar."));
            waitingForLogin = false;
        }
    }
    
    private void sendCommand(String command) {
        if (mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage(command);
        }
    }
    
    public static void saveCredentials(String playerName, String password) {
        savedCredentials.put(playerName, password);
        saveCredentialsToFile();
    }
    
    public static boolean hasCredentials(String playerName) {
        return savedCredentials.containsKey(playerName) && !savedCredentials.get(playerName).isEmpty();
    }
    
    public static void removeCredentials(String playerName) {
        savedCredentials.remove(playerName);
        saveCredentialsToFile();
    }
    
    private static void loadCredentials() {
        try {
            File file = new File(ModConstants.LOGIN_FILE);
            if (!file.exists()) {
                    return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String decryptedPassword = CryptoUtils.decrypt(parts[1]);
                    if (!decryptedPassword.isEmpty()) {
                        savedCredentials.put(parts[0], decryptedPassword);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("[SkyLake] Erro ao carregar credenciais: " + e.getMessage());
        }
    }
    
    private static void saveCredentialsToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ModConstants.LOGIN_FILE));
            for (String playerName : savedCredentials.keySet()) {
                String encryptedPassword = CryptoUtils.encrypt(savedCredentials.get(playerName));
                writer.write(playerName + ":" + encryptedPassword);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("[SkyLake] Erro ao salvar credenciais: " + e.getMessage());
        }
    }
    
    public static void setPasswordForAutoSave(String password) {
        currentPassword = password;
    }
}
