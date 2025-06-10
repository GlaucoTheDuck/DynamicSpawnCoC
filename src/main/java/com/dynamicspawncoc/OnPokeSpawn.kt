package com.dynamicspawncoc

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.dynamicspawncoc.DynamicSpawnCoC.Companion.LOGGER
import com.cobblemon.mod.common.api.spawning.CobblemonSpawnPools
import com.cobblemon.mod.common.util.toVec3d
import com.dynamicspawncoc.util.GetNearestPlayer.Companion.getNearestPlayer
import net.minecraft.network.chat.Component
import com.dynamicspawncoc.DynamicSpawnCoC.Companion.data

class OnPokeSpawn {

    fun onInitialize() {
        // Aguardar inicialização do Cobblemon
        CobblemonEvents.DATA_SYNCHRONIZED.subscribe {

            initializeSpawnLogic()
        }
    }

    fun initializeSpawnLogic() {
        val spawnPool = CobblemonSpawnPools.WORLD_SPAWN_POOL
        CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe { evt ->

            val pokemon = evt.entity.pokemon
            if (pokemon.isPlayerOwned()) return@subscribe
            evt.entity.maxSpawnClusterSize
            val world = evt.ctx.world
            val pos = evt.ctx.position

            LOGGER.info(
            "\nPokemon Spawn \nName: SpW"+ pokemon.species.name +
            "\nWorld Seed: "+ world.seed+
            "\nPos: ("+pos.x+","+pos.y+","+pos.z+")")

            val filter = spawnPool.filter { spawn ->
                spawn.isSatisfiedBy(ctx = evt.ctx)
            }

            val nearestPlayer = getNearestPlayer(world, pos.toVec3d())

            if(nearestPlayer == null) return@subscribe

            filter.forEach { spawn ->
                val weight = spawn.weight // Peso do spawn

                data?.incrementMult(nearestPlayer.uuid, spawn.getName().string, 0.001f)

                data?.setMult(nearestPlayer.uuid, evt.entity.name.string, 1f)
                val multDict = data?.getWeightMultDict()
                val playerInfo = multDict?.get(nearestPlayer.uuid)
                // LOGGER.info("weight: ${detail.weight}")
                world.server.playerList.broadcastSystemMessage(
                    Component.literal(
                        "Pokémon possível: PoS:${spawn.getName()}" +
                                "\nPlayer: ${nearestPlayer.name}" +
                                "\nPeso: $weight" +
                                "\nPercentage: ${spawn.percentage}" +
                                "\nSpawn Mult: ${playerInfo?.get(spawn.getName().string.lowercase())?.multiplier}"
                    ),
                    false
                )
            }
        }
    }
}