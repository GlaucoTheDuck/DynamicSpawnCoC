package com.dynamicspawncoc.util

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3

public class GetNearestPlayer {
    companion object{
        public fun getNearestPlayer(world: ServerLevel, pos: Vec3): ServerPlayer? {
            val allPlayers = world.server.playerList?.players ?: emptyList()

            val playersInSameWorld = allPlayers.filter{ player ->
                player.level() == world
            }

            val nearestPlayer = playersInSameWorld.minByOrNull { player ->
                player.position().distanceTo(pos)
            }

            return nearestPlayer
        }
    }

}