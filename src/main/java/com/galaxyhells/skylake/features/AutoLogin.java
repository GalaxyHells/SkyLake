package com.galaxyhells.skylake.features;

import com.galaxyhells.skylake.config.ConfigHandler;
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
    private static final String LOGIN_FILE = "skylake_login.txt";
    private static final String LOGIN_PROMPT = "Utilize o comando /logar <senha>.";
    private static final String REGISTER_PROMPT = "registre-se com /register";
    private static final String LOGIN_SUCCESS = "Você logou com sucesso.";
    private static final String REGISTER_SUCCESS = "registrado com sucesso";
    
    private static boolean waitingForLogin = false;
    private static boolean waitingForRegister = false;
    private static String currentPassword = null;
    private static int loginAttempts = 0;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static long lastLoginAttempt = 0;
    private static final long LOGIN_COOLDOWN = 2000; // 2 segundos
    
    private static final ConcurrentHashMap<String, String> savedCredentials = new ConcurrentHashMap<>();
    
    public AutoLogin() {
        loadCredentials();
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GREEN + "[DEBUG] AutoLogin inicializado com " + savedCredentials.size() + " credenciais carregadas"));
        }
    }
    
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!ConfigHandler.autoLogin) return;

        // Debug: Mostrar todas as mensagens recebidas
        String originalMessage = event.message.getUnformattedText();
        String message = originalMessage.toLowerCase();
        
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GRAY + "[DEBUG] Mensagem recebida: " + originalMessage));
        }
        
        if (!ConfigHandler.autoLogin || mc.thePlayer == null) {
            if (mc.thePlayer != null) {
                mc.thePlayer.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.RED + "[DEBUG] AutoLogin desativado ou jogador nulo"));
            }
            return;
        }
        
        mc.thePlayer.addChatMessage(new ChatComponentText(
            EnumChatFormatting.AQUA + "[DEBUG] AutoLogin está ATIVADO"));
        
        // Detecta solicitação de login
        if (message.contains(LOGIN_PROMPT)) {
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.YELLOW + "[DEBUG] Prompt de login detectado!"));
            waitingForLogin = true;
            waitingForRegister = false;
            attemptAutoLogin();
            return;
        }
        
        // Debug: Verificar se contém partes do prompt
        if (message.contains("login") || message.contains("logar")) {
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.LIGHT_PURPLE + "[DEBUG] Mensagem contém 'login' ou 'logar'"));
        }
        
        // Detecta solicitação de registro
        if (message.contains(REGISTER_PROMPT)) {
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.YELLOW + "[DEBUG] Prompt de registro detectado!"));
            waitingForRegister = true;
            waitingForLogin = false;
            return;
        }
        
        // Detecta sucesso no login
        if (message.contains(LOGIN_SUCCESS)) {
            waitingForLogin = false;
            waitingForRegister = false;
            loginAttempts = 0;
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GREEN + "[SkyLake] AutoLogin: Login realizado com sucesso!"));
            return;
        }
        
        // Detecta sucesso no registro
        if (message.contains(REGISTER_SUCCESS)) {
            waitingForRegister = false;
            waitingForLogin = false;
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.GREEN + "[SkyLake] AutoLogin: Registro realizado! Por favor, faça login manualmente uma vez para salvar sua senha."));
            return;
        }
        
        // Detecta falha no login
        if (message.contains("senha incorreta") || message.contains("login falhou")) {
            loginAttempts++;
            if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                mc.thePlayer.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.RED + "[SkyLake] AutoLogin: Falha no login após " + MAX_LOGIN_ATTEMPTS + " tentativas. Desativando auto-login temporariamente."));
                waitingForLogin = false;
                loginAttempts = 0;
            }
        }
    }
    
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        if (waitingForRegister && currentPassword != null && System.currentTimeMillis() - lastLoginAttempt > LOGIN_COOLDOWN) {
            // Se está esperando registro e temos uma senha, envia comando de registro
            sendCommand("/register " + currentPassword + " " + currentPassword);
            waitingForRegister = false;
            lastLoginAttempt = System.currentTimeMillis();
        }
    }
    
    private void attemptAutoLogin() {
        mc.thePlayer.addChatMessage(new ChatComponentText(
            EnumChatFormatting.DARK_GREEN + "[DEBUG] attemptAutoLogin() chamado"));
            
        if (!waitingForLogin) {
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.RED + "[DEBUG] Não está esperando login"));
            return;
        }
        
        if (System.currentTimeMillis() - lastLoginAttempt < LOGIN_COOLDOWN) {
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.RED + "[DEBUG] Ainda em cooldown"));
            return;
        }
        
        String playerName = mc.thePlayer.getName();
        String password = savedCredentials.get(playerName);
        
        mc.thePlayer.addChatMessage(new ChatComponentText(
            EnumChatFormatting.DARK_AQUA + "[DEBUG] Jogador: " + playerName + ", Senha salva: " + (password != null ? "SIM" : "NÃO")));
        
        if (password != null && !password.isEmpty()) {
            sendCommand("/login " + password);
            lastLoginAttempt = System.currentTimeMillis();
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.AQUA + "[SkyLake] AutoLogin: Enviando comando de login..."));
        } else {
            mc.thePlayer.addChatMessage(new ChatComponentText(
                EnumChatFormatting.YELLOW + "[SkyLake] AutoLogin: Nenhuma senha salva para este jogador. Faça login manualmente uma vez para salvar."));
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
            File file = new File(LOGIN_FILE);
            if (!file.exists()) {
                System.out.println("[DEBUG] Arquivo de credenciais não existe: " + LOGIN_FILE);
                return;
            }
            
            System.out.println("[DEBUG] Carregando credenciais do arquivo: " + file.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    savedCredentials.put(parts[0], parts[1]);
                    System.out.println("[DEBUG] Credencial carregada: " + parts[0] + " -> ***");
                }
            }
            reader.close();
            System.out.println("[DEBUG] Total de credenciais carregadas: " + savedCredentials.size());
        } catch (IOException e) {
            System.err.println("[SkyLake] Erro ao carregar credenciais: " + e.getMessage());
        }
    }
    
    private static void saveCredentialsToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(LOGIN_FILE));
            for (String playerName : savedCredentials.keySet()) {
                writer.write(playerName + ":" + savedCredentials.get(playerName));
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
