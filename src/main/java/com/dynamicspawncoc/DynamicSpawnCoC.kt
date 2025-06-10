package com.dynamicspawncoc

import com.cobblemon.mod.common.api.spawning.spawner.PlayerSpawnerFactory
import com.cobblemon.mod.common.config.CobblemonConfig
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.Level
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import com.dynamicspawncoc.dataSavers.SpawnModDictionary
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents

class DynamicSpawnCoC : ModInitializer {


    companion object {
        private fun loadCfg(): CobblemonConfig {
            val configDir: Path = FabricLoader.getInstance().configDir
            val cobblemonDir = configDir.resolve("cobblemon")
            val mainJson = cobblemonDir.resolve("main.json")

            val jsonString = Files.readString(mainJson)

            return CobblemonConfig.GSON.fromJson(jsonString, CobblemonConfig::class.java)
        }

        const val MOD_ID = "dynamicspawncoc"
        val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
        //val cfg = loadCfg()
        var data: SpawnModDictionary? = null
    }


    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ServerWorldEvents.LOAD.register { server: MinecraftServer, world ->
            if (world.dimension() == Level.OVERWORLD) {
                data = world.dataStorage.computeIfAbsent(
                    SpawnModDictionary.FACTORY,
                    SpawnModDictionary.getId().toString()
                )
            }

            LOGGER.info("Dados carregados do mundo!")

        }

        ServerTickEvents.END_SERVER_TICK.register { server: MinecraftServer ->
            server.getLevel(Level.OVERWORLD)
                ?.dataStorage
            data?.setDirty()
        }

        OnPokeSpawn().onInitialize()
        PlayerSpawnerFactory.influenceBuilders.add{ player ->
            CustomSpawnInfluence() }
        LOGGER.info("'Dynamic Spawn CoC' iniciado em Fabric 1.21.1!")
    }
}