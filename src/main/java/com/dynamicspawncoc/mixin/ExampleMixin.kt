package com.dynamicspawncoc.mixin

import net.minecraft.server.MinecraftServer
import org.spongepowered.asm.mixin.Mixin

@Mixin(MinecraftServer::class)
class ExampleMixin {
    /*
    @Inject(at = At("HEAD"), method = "loadWorld")
    private fun init(info: CallbackInfo) {
        // This code is injected into the start of MinecraftServer.loadWorld()V
    }
    */
}