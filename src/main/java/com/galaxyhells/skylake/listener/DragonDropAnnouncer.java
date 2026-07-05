package com.galaxyhells.skylake.listener;

import com.galaxyhells.skylake.SkyLake;
import com.galaxyhells.skylake.utils.OptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DragonDropAnnouncer {
    
    private static final Pattern RANKING_PATTERN = Pattern.compile("Ranking de Dano Causado no Dragão");
    private static final Pattern DROP_PATTERN = Pattern.compile("Você recebeu (.+)");
    
    private boolean collectingDrops = false;
    private int linesToCheck = 0;
    private final List<String> collectedDrops = new ArrayList<>();
    
    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        // Verifica se a feature está ativa na config
        if (!Boolean.TRUE.equals(SkyLake.optionsService.get(OptionType.DRAGON_DROP_ANNOUNCER))) return;
        
        String message = event.message.getUnformattedText();
        
        // Verifica se é a mensagem de ranking do dragão
        if (RANKING_PATTERN.matcher(message).find()) {
            collectingDrops = true;
            linesToCheck = 12;
            collectedDrops.clear();
            return;
        }
        
        // Se está coletando drops, verifica se a mensagem contém "Você recebeu "
        if (collectingDrops && linesToCheck > 0) {
            Matcher dropMatcher = DROP_PATTERN.matcher(message);
            if (dropMatcher.find()) {
                String drop = dropMatcher.group(1);
                collectedDrops.add(drop);
            }
            
            linesToCheck--;
            
            // Quando terminar de verificar as linhas, envia a mensagem
            if (linesToCheck == 0) {
                sendDropAnnouncement();
                collectingDrops = false;
                collectedDrops.clear();
            }
        }
    }
    
    private void sendDropAnnouncement() {
        if (collectedDrops.isEmpty()) {
            return;
        }
        
        // Formata a mensagem com os drops coletados
        StringBuilder dropsMessage = new StringBuilder();
        for (int i = 0; i < collectedDrops.size(); i++) {
            if (i > 0) {
                dropsMessage.append(", ");
            }
            dropsMessage.append(collectedDrops.get(i));
        }
        
        String finalMessage = EnumChatFormatting.AQUA + "[SkyLake Drops] " + EnumChatFormatting.WHITE + dropsMessage.toString();
        
        // Envia a mensagem no chat
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(finalMessage));
        }
    }
}
