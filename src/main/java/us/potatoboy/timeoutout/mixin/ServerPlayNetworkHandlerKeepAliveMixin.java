package us.potatoboy.timeoutout.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import us.potatoboy.timeoutout.TimeOutOut;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerKeepAliveMixin {
    @Shadow
    private long lastKeepAliveTime;

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Util;getMeasuringTimeMs()J",
                    ordinal = 1
            )
    )
    private long redirectKeepAliveTimeCheck() {
        // Return a value that makes the interval check use our configured timeout.
        // The check is: if (l - this.lastKeepAliveTime >= 15000L)
        // By returning lastKeepAliveTime + ourInterval as "l", we control when it fires.
        return this.lastKeepAliveTime + (TimeOutOut.getConfig().keepAliveTimeoutSeconds * 1000L);
    }
}