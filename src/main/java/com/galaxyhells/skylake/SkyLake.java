package com.galaxyhells.skylake;

import com.galaxyhells.skylake.commands.SkyLakeCommand;
import com.galaxyhells.skylake.config.ConfigHandler;
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

        System.out.println("[" + NAME + "] Mod inicializado com sucesso no modo Raiz!");
    }
}