package com.galaxyhells.skylake;

import com.galaxyhells.skylake.commands.SkyLakeCommand;
import com.galaxyhells.skylake.config.ConfigHandler;
import com.galaxyhells.skylake.data.ItemDataManager;
import com.galaxyhells.skylake.features.hud.StatOverlay;
import com.galaxyhells.skylake.features.inventory.TooltipListener;
import com.galaxyhells.skylake.features.render.TreasureGui;
import com.galaxyhells.skylake.features.render.TreasureWaypoint;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;

@Mod(
        modid = SkyLake.MODID,
        name = SkyLake.NAME,
        version = SkyLake.VERSION,
        acceptedMinecraftVersions = "[1.8.9]"
)
public class SkyLake {

    public static final String MODID = "skylake";
    public static final String NAME = "SkyLake";
    public static final String VERSION = "0.1.0";

    @Mod.Instance(MODID)
    public static SkyLake instance;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Carrega as configurações do arquivo .cfg
        ConfigHandler.loadConfig();

        // Registro do Comando
        ClientCommandHandler.instance.registerCommand(new SkyLakeCommand());

        // REGISTRO DO LISTENER
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.listener.ChatListener());

        // Registro da feature: Treasure Waypoint
        MinecraftForge.EVENT_BUS.register(TreasureWaypoint.INSTANCE);

        // Registro do feature:Guia Pirata (Tick e Chat)
        MinecraftForge.EVENT_BUS.register(TreasureGui.INSTANCE);
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(TreasureGui.INSTANCE);

        // Registro da feature: Slot Lock
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.inventory.SlotLockFeature());

        // Registro da feature: Rarity Background
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.inventory.RarityBackground());

        // Registro da feature: Stat Overlay
        MinecraftForge.EVENT_BUS.register(new StatOverlay());

        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.hud.TabMenuHandler());
        //
        ItemDataManager.loadLocalPrices();
        MinecraftForge.EVENT_BUS.register(new TooltipListener());
        //

        // Registro da feature: Mutant Timer
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.hud.MutantTimer());

        // Registro da feature: Magma Timer
        MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.hud.MagmaTimer());

        // Registro da feature: Mutant Highlight
        MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.render.MutantHighlight());

        // 1. Registra a tecla visualmente nas opções de controles do Minecraft
        com.galaxyhells.skylake.utils.KeybindManager.register();

        // 2. Registra o ouvinte para detectar quando você aperta a tecla
        // ATENÇÃO: No 1.8.9, eventos de teclado ficam no FMLCommonHandler, e não no EVENT_BUS padrão do Forge!
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(new com.galaxyhells.skylake.utils.KeybindManager());

        // FEATURE: Radar
        MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.render.TreasureRadar());

        System.out.println("[" + NAME + "] Mod inicializado com sucesso no modo Raiz!");
    }
}