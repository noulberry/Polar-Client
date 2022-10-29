package dulkirmod.features.chat

import dulkirmod.DulkirMod
import dulkirmod.config.Config
import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

private val abiphoneFormat = "✆ (\\w+) ✆ ".toRegex()
private var lastRing: Long = 0

class AbiphoneDND {
    //BLOCK ABIPHONE SOUNDS
    @SubscribeEvent(receiveCanceled = false, priority = EventPriority.LOW)
    fun onSound(event: PlaySoundEvent) {
        if (!Config.abiDND) return
        if (System.currentTimeMillis() - lastRing < 5000) {
            if (event.name == "note.pling" && event.sound.volume == 0.69f && event.sound.pitch == 1.6666666f) {
                event.isCanceled = true
            }
        }
    }

    companion object {
        fun handle(event: ClientChatReceivedEvent, unformatted: String) {
            if (!Config.abiDND) return
            if (unformatted matches abiphoneFormat) {
                val matchResult = abiphoneFormat.find(unformatted)
                event.isCanceled = true;
                lastRing = System.currentTimeMillis()
                if (Config.abiCallerID) {
                    DulkirMod.mc.thePlayer.addChatMessage(
                        ChatComponentText(
                            "${DulkirMod.CHAT_PREFIX} §6Call blocked from ${
                                if (Math.random() < .001) "Breefing"
                                else matchResult?.groups?.get(1)?.value
                            }!"
                        )
                    )
                }
            }
            if (unformatted.startsWith("✆ Ring...") && unformatted.endsWith("[PICK UP]")
                && System.currentTimeMillis() - lastRing < 5000) {
                event.isCanceled = true;
            }
        }

    }
}