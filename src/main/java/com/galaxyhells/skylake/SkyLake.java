package com.galaxyhells.skylake;

import com.galaxyhells.skylake.commands.SkyLakeCommand;
import com.galaxyhells.skylake.commands.AutoLoginCommand;
import com.galaxyhells.skylake.config.ConfigHandler;
import com.galaxyhells.skylake.data.ItemDataManager;
import com.galaxyhells.skylake.features.hud.AutoAFK;
import com.galaxyhells.skylake.features.hud.FancyHUD.FancyHotbar;
import com.galaxyhells.skylake.features.hud.FancyHUD.FancyStatOverlay;
import com.galaxyhells.skylake.features.hud.MapFeature;
import com.galaxyhells.skylake.features.hud.StatOverlay;
import com.galaxyhells.skylake.features.hud.timer.MagmaTimer;
import com.galaxyhells.skylake.features.hud.timer.MutantTimer;
import com.galaxyhells.skylake.features.inventory.TooltipListener;
import com.galaxyhells.skylake.features.render.treasure.TreasureClickHandler;
import com.galaxyhells.skylake.features.render.treasure.TreasureGui;
import com.galaxyhells.skylake.features.render.treasure.TreasureWaypoint;
import com.galaxyhells.skylake.features.render.MutantSpawnBoxes;
import com.galaxyhells.skylake.features.movement.AutoSprint;
import com.galaxyhells.skylake.features.render.treasure.TreasureRadar;
import com.galaxyhells.skylake.features.AutoLogin;
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

        // 1. Registro de Comandos
        ClientCommandHandler.instance.registerCommand(new SkyLakeCommand());
        ClientCommandHandler.instance.registerCommand(new com.galaxyhells.skylake.commands.AnnounceCommand());
        ClientCommandHandler.instance.registerCommand(new AutoLoginCommand());

        // 2. Registro de Listeners Globais
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.listener.ChatListener());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.listener.LoginListener());

        // 3. Registro de Features HUD
        // Timers
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new MutantTimer());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new MagmaTimer());
        // Overlays
        MinecraftForge.EVENT_BUS.register(new StatOverlay());
        MinecraftForge.EVENT_BUS.register(new AutoAFK());
        MinecraftForge.EVENT_BUS.register(new FancyHotbar());
        MinecraftForge.EVENT_BUS.register(new FancyStatOverlay());
        // GUIs
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.hud.TabMenuHandler());
        MapFeature mapFeature = new MapFeature();
        MinecraftForge.EVENT_BUS.register(mapFeature);
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(mapFeature);

        // 4. Registro de Features Render
        // Treasure System
        MinecraftForge.EVENT_BUS.register(TreasureWaypoint.INSTANCE);
        MinecraftForge.EVENT_BUS.register(TreasureClickHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(TreasureGui.INSTANCE);
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(TreasureGui.INSTANCE);
        MinecraftForge.EVENT_BUS.register(new TreasureRadar());
        // Entity Highlights
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.render.MutantHighlight());
        MinecraftForge.EVENT_BUS.register(MutantSpawnBoxes.INSTANCE);

        // 5. Registro de Features Inventory
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.inventory.SlotLockFeature());
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new com.galaxyhells.skylake.features.inventory.RarityBackground());
        // Data Management
        ItemDataManager.loadLocalPrices();
        MinecraftForge.EVENT_BUS.register(new TooltipListener());

        // 6. Registro de Features Movement
        MinecraftForge.EVENT_BUS.register(new AutoSprint());

        // 7. Registro de Features Sistema
        MinecraftForge.EVENT_BUS.register(new AutoLogin());

        // 8. Registro de Utilitários
        // Keybind Management
        com.galaxyhells.skylake.utils.KeybindManager.register();
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(new com.galaxyhells.skylake.utils.KeybindManager());

        System.out.println("[" + NAME + "] Mod inicializado com sucesso no modo Raiz!");
    }
}
