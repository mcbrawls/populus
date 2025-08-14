package net.mcbrawls.populus

import com.mojang.authlib.GameProfile
import eu.pb4.polymer.core.api.entity.PolymerEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Arm
import net.minecraft.world.GameMode
import net.minecraft.world.World
import xyz.nucleoid.packettweaker.PacketContext
import java.util.EnumSet
import java.util.function.Consumer

abstract class AbstractFakePlayerEntity(type: EntityType<out AbstractFakePlayerEntity>, world: World) : PathAwareEntity(type, world), PolymerEntity {
    open fun createSpawnEntry(): PlayerListS2CPacket.Entry {
        val uuid = uuid
        val defaultName = uuid.toString().substring(0..<16)
        return PlayerListS2CPacket.Entry(
            uuid,
            GameProfile(uuid, defaultName),
            false, // listed
            0, // latency
            GameMode.DEFAULT,
            null, // player list name
            true, // show hat layer
            Integer.MAX_VALUE, // list order
            null, // chat session
        )
    }

    override fun onBeforeSpawnPacket(player: ServerPlayerEntity, consumer: Consumer<Packet<*>>) {
        val packet = PlayerListS2CPacket(EnumSet.of(PlayerListS2CPacket.Action.ADD_PLAYER), emptyList()).apply {
            val entry = createSpawnEntry()
            entries = listOf(entry)
        }

        player.networkHandler.sendPacket(packet)
    }

    override fun onStoppedTrackingBy(player: ServerPlayerEntity) {
        val packet = PlayerRemoveS2CPacket(listOf(uuid))
        player.networkHandler.sendPacket(packet)
    }

    override fun getMainArm(): Arm {
        return Arm.RIGHT
    }

    override fun getPolymerEntityType(context: PacketContext): EntityType<*> {
        return EntityType.PLAYER
    }
}
