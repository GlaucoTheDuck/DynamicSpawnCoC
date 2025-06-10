package com.dynamicspawncoc.dataSavers

import com.cobblemon.mod.common.api.spawning.multiplier.WeightMultiplier
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.util.datafix.DataFixTypes
import java.util.UUID
import com.dynamicspawncoc.DynamicSpawnCoC.Companion.LOGGER
import kotlin.collections.set

class SpawnModDictionary : SavedData() {

    private val dict: MutableMap<UUID, MutableMap<String, Float>> = mutableMapOf()

    companion object {

        val FACTORY: Factory<SpawnModDictionary> = Factory(
            { SpawnModDictionary() },
            { nbt, _ ->
                load(nbt)
            },
            DataFixTypes.LEVEL
        )

        @JvmStatic
        fun getId(): String = "mod_spawn_dict_data"
        // fun getId(): ResourceLocation = ResourceLocation.bySeparator(MOD_ID, "dict_data")

        @JvmStatic
        fun load(nbt: CompoundTag): SpawnModDictionary{
            LOGGER.info("Método load() chamado")
            val data = SpawnModDictionary()

            val outerTag = nbt.getCompound("dict")
            LOGGER.info("Carregando dict com ${outerTag.allKeys.size} jogadores")

            for (outerKey in outerTag.allKeys){
                LOGGER.info("Carregando dados do jogador: $outerKey")
                val innerTag = outerTag.getCompound(outerKey)
                val innerMap = mutableMapOf<String, Float>()

                for (key in innerTag.allKeys){
                    val value = innerTag.getFloat(key)
                    innerMap[key] = value
                    LOGGER.info("  $key: $value")
                }
                data.dict[UUID.fromString(outerKey)] = innerMap
            }

            LOGGER.info("Load concluído com ${data.dict.size} jogadores")
            return data
        }
    }

    fun incrementMult(player: UUID, pokeName: String, mult: Float){
        val playerInfo = dict.getOrPut(player) { mutableMapOf() }
        val pokeName = pokeName.lowercase()

        if (playerInfo[pokeName] == null) {
            playerInfo[pokeName] = 1f
        } else {
            playerInfo[pokeName] = playerInfo[pokeName]!! + mult
        }

    }

    fun setMult(player: UUID, pokeName: String, mult: Float){
        val playerInfo = dict.getOrPut(player) { mutableMapOf() }
        playerInfo[pokeName.lowercase()] = mult.coerceAtLeast(1f)
    }

    fun contains(player: UUID, pokeName: String): Boolean {
        val pokeMod = dict[player]?.get(pokeName.lowercase())
        return pokeMod != null
    }

    override fun save(tag: CompoundTag, provider: HolderLookup.Provider): CompoundTag {
        LOGGER.info("=== SAVE CHAMADO ===")
        LOGGER.info("Dict size: ${dict.size}")
        LOGGER.info("Dict contents: $dict")

        val outerTag = CompoundTag()
        dict.forEach { (uuid, innerMap) ->
            LOGGER.info("Salvando dados do jogador: $uuid")
            val innerTag = CompoundTag()
            innerMap.forEach { (key, wm) ->
                innerTag.putFloat(key, wm)
                LOGGER.info("  $key: $wm")
            }
            outerTag.put(uuid.toString(), innerTag)
        }
        tag.put("dict", outerTag)

        LOGGER.info("Save concluído")
        return tag
    }


    fun getWeightMultDict(): MutableMap<UUID, MutableMap<String, WeightMultiplier>> {
        val dictWeight = dict.mapValues { (_, innerMap) ->
            innerMap.mapValues { (_, multiplierFloat) ->
                val weight = WeightMultiplier()
                weight.multiplier = multiplierFloat
                weight
            }.toMutableMap()
        }.toMutableMap()

        return dictWeight

    }

}