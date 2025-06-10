package com.dynamicspawncoc.client.mixin

import net.minecraft.client.Minecraft
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Minecraft::class)
class ExampleClientMixin {
    @Inject(at = [At("HEAD")], method = ["run"])
    private fun init(info: CallbackInfo?) {
        // This code is injected into the start of MinecraftClient.run()V
    }
}