package com.dynamicspawncoc
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.api.spawning.detail.SpawnDetail
import com.cobblemon.mod.common.api.spawning.influence.SpawningInfluence
import com.cobblemon.mod.common.util.toVec3d
import com.dynamicspawncoc.util.GetNearestPlayer.Companion.getNearestPlayer
import com.dynamicspawncoc.DynamicSpawnCoC.Companion.data

class CustomSpawnInfluence : SpawningInfluence {
    override fun affectSpawnable(detail: SpawnDetail, ctx: SpawningContext): Boolean {

        val world = ctx.world
        val pos = ctx.position

        val nearestPlayer = getNearestPlayer(world, pos.toVec3d())
        if (nearestPlayer == null) return true
        val playerPokeChanceMod = data?.getWeightMultDict()
        val pokeMod = playerPokeChanceMod?.get(nearestPlayer.uuid)?.get(detail.getName().string.lowercase())
        if(pokeMod == null ) return true

        if(!detail.weightMultipliers.contains(pokeMod)){
            detail.weightMultipliers.add(pokeMod)
        }

        return true
    }
}
