package com.galaxyhells.skylake;

import com.galaxyhells.skylake.commands.SkyLakeCommand;
import com.galaxyhells.skylake.commands.AutoLoginCommand;
import com.galaxyhells.skylake.commands.OpenConfigCommand;
import com.galaxyhells.skylake.data.ItemDataManager;
import com.galaxyhells.skylake.features.OptionsService;
import com.galaxyhells.skylake.features.hud.FancyHUD.FancyHotbar;
import com.galaxyhells.skylake.features.hud.FancyHUD.FancyStatOverlay;
import com.galaxyhells.skylake.features.hud.Map.MapFeature;
import com.galaxyhells.skylake.features.hud.StatOverlay;
import com.galaxyhells.skylake.features.hud.timer.MagmaTimer;
import com.galaxyhells.skylake.features.hud.timer.MutantTimer;
import com.galaxyhells.skylake.features.inventory.InventoryCenter;
import com.galaxyhells.skylake.features.inventory.TooltipListener;
import com.galaxyhells.skylake.features.render.NametagRenderer;
import com.galaxyhells.skylake.features.render.treasure.TreasureClickHandler;
import com.galaxyhells.skylake.features.render.treasure.TreasureGui;
import com.galaxyhells.skylake.features.render.treasure.TreasureWaypoint;
import com.galaxyhells.skylake.features.render.MutantSpawnBoxes;
import com.galaxyhells.skylake.features.render.DragonHighlight;
import com.galaxyhells.skylake.features.movement.AutoSprint;
import com.galaxyhells.skylake.features.movement.AutoFishing;
//import com.galaxyhells.skylake.features.render.treasure.TreasureRadar;
import com.galaxyhells.skylake.features.AutoLogin;
import com.galaxyhells.skylake.features.itemlog.ItemLogFeature;
import com.galaxyhells.skylake.listener.ItemLogListener;
import com.galaxyhells.skylake.features.render.NametagHider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;

@Mod(
        modid = SkyLake.MODID,
        name = SkyLake.NAME,
        version = SkyLake.VERSION,
        acceptedMinecraftVersions = "[1.8.9]"
)
public class SkyLake {

    public static final String MODID = "skylake";
    public static final String NAME = "SkyLake";
    public static final String VERSION = "0.5.0";

    @Mod.Instance(MODID)
    public static SkyLake instance;
    
    public static OptionsService optionsService;

//    @Mod.EventHandler
//    public void preInit(){
//        // ESSAS DUAS LINHAS SÃO OBRIGATÓRIAS
//        MixinBootstrap.init();
//        Mixins.addConfiguration("mixins.skylake.json");
//    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // 1. Registro de Comandos
        ClientCommandHandler.instance.registerCommand(new SkyLakeCommand());
        ClientCommandHandler.instance.registerCommand(new AutoLoginCommand());
        ClientCommandHandler.instance.registerCommand(new OpenConfigCommand());

        // 2. Registro de Listeners Globais
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.listener.ChatListener());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.listener.LoginListener());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.listener.DragonDropAnnouncer());

        // 3. Registro de Features HUD
        // Timers
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new MutantTimer());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new MagmaTimer());
        // Overlays
        MinecraftForge.EVENT_BUS.register(new StatOverlay());
        MinecraftForge.EVENT_BUS.register(new FancyHotbar());
        MinecraftForge.EVENT_BUS.register(new FancyStatOverlay());
        // Item Log
        ItemLogFeature itemLogFeature = new ItemLogFeature();
        MinecraftForge.EVENT_BUS.register(itemLogFeature);
        MinecraftForge.EVENT_BUS.register(new ItemLogListener(itemLogFeature));
        // GUIs
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.hud.TabMenuHandler());
        MapFeature mapFeature = new MapFeature();
        MinecraftForge.EVENT_BUS.register(mapFeature);
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(mapFeature);

        // 4. Registro de Features Render
        MinecraftForge.EVENT_BUS.register(NametagHider.INSTANCE);
        MinecraftForge.EVENT_BUS.register(NametagRenderer.INSTANCE);
        // Treasure System
        MinecraftForge.EVENT_BUS.register(TreasureWaypoint.INSTANCE);
        MinecraftForge.EVENT_BUS.register(TreasureClickHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(TreasureGui.INSTANCE);
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(TreasureGui.INSTANCE);
        // Entity Highlights
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.render.MutantHighlight());
        MinecraftForge.EVENT_BUS.register(MutantSpawnBoxes.INSTANCE);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new DragonHighlight());

        // 5. Registro de Features Inventory
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.inventory.SlotLockFeature());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.inventory.RarityBackground());
        // Data Management
        ItemDataManager.loadLocalPrices();
        MinecraftForge.EVENT_BUS.register(new TooltipListener());
        MinecraftForge.EVENT_BUS.register(new InventoryCenter());

        // 6. Registro de Utilitários (Keybind Management - precisa ser antes das features que dependem dele)
        com.galaxyhells.skylake.utils.KeybindManager.register();
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(new com.galaxyhells.skylake.utils.KeybindManager());

        // 7. Registro de Features Movement
        MinecraftForge.EVENT_BUS.register(new AutoSprint());
        MinecraftForge.EVENT_BUS.register(new AutoFishing());

        // 8. Registro de Features Sistema
        MinecraftForge.EVENT_BUS.register(new AutoLogin());

        System.out.println("[" + NAME + "] Mod inicializado com sucesso no modo Raiz!");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        optionsService = new OptionsService();

        File configFile = new File(event.getModConfigurationDirectory(), "neonextgui.cfg");
        optionsService.init(configFile);
    }
}
