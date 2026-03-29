package com.galaxyhells.skylake.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ChatComponentText;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.init.Items;

public class SkullUtils {

    public static void inspectNearby() {
        Minecraft mc = Minecraft.getMinecraft();
        double radius = 5.0; // Raio de busca
        boolean found = false;

        mc.thePlayer.addChatMessage(new ChatComponentText("§b[SkyLake] §7Escaneando tesouros próximos..."));

        // 1. TENTA BUSCAR POR BLOCOS (TileEntities) NO CHÃO
        for (TileEntity te : mc.theWorld.loadedTileEntityList) {
            if (te instanceof TileEntitySkull) {
                double dist = mc.thePlayer.getDistanceSq(te.getPos());
                if (dist <= radius * radius) {
                    printSkullData("Bloco", ((TileEntitySkull) te).getPlayerProfile());
                    found = true;
                }
            }
        }

        // 2. TENTA BUSCAR POR ARMOR STANDS (Entidades que podem estar segurando a cabeça)
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityArmorStand) {
                double dist = mc.thePlayer.getDistanceSqToEntity(entity);
                if (dist <= radius * radius) {
                    // No 1.8.9, o slot 4 é a cabeça
                    ItemStack head = ((EntityArmorStand) entity).getEquipmentInSlot(4);
                    if (head != null && head.getItem() == Items.skull) {
                        GameProfile profile = getProfileFromStack(head);
                        if (profile != null) {
                            printSkullData("ArmorStand", profile);
                            found = true;
                        }
                    }
                }
            }
        }

        if (!found) {
            mc.thePlayer.addChatMessage(new ChatComponentText("§c[SkyLake] Nenhum tesouro encontrado no raio de 5m."));
        }
    }

    private static void printSkullData(String type, GameProfile profile) {
        if (profile == null) return;
        Minecraft mc = Minecraft.getMinecraft();

        mc.thePlayer.addChatMessage(new ChatComponentText("§6--- Achado (" + type + ") ---"));
        mc.thePlayer.addChatMessage(new ChatComponentText("§7Nome: §e" + profile.getName()));

        if (profile.getProperties().containsKey("textures")) {
            for (Property prop : profile.getProperties().get("textures")) {
                mc.thePlayer.addChatMessage(new ChatComponentText("§7Value: §f" + prop.getValue().substring(0, 20) + "..."));
                System.out.println("SkyLake Full Value: " + prop.getValue());
            }
        }
    }

    private static GameProfile getProfileFromStack(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("SkullOwner", 10)) {
            NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("SkullOwner");
            // Converte o NBT de volta para um GameProfile
            return net.minecraft.nbt.NBTUtil.readGameProfileFromNBT(nbt);
        }
        return null;
    }
}