package net.mcbrawls.populus

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import eu.pb4.polymer.core.api.entity.PolymerEntity
import eu.pb4.polymer.virtualentity.api.ElementHolder
import eu.pb4.polymer.virtualentity.api.attachment.EntityAttachment
import eu.pb4.polymer.virtualentity.api.elements.TextDisplayElement
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.decoration.DisplayEntity
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Arm
import net.minecraft.util.math.AffineTransformation
import net.minecraft.world.GameMode
import net.minecraft.world.World
import org.joml.Vector3f
import xyz.nucleoid.packettweaker.PacketContext
import java.util.EnumSet
import java.util.function.Consumer

abstract class AbstractFakePlayerEntity(type: EntityType<out AbstractFakePlayerEntity>, world: World) : PathAwareEntity(type, world), PolymerEntity {
    /**
     * The raw skin texture data for this player's skin.
     */
    open val skinData: SkinData? = null

    /**
     * A byte mask of the model parts to display. (See Skin Customization)
     */
    open val modelParts: Byte = Byte.MAX_VALUE

    /**
     * The base name tag offset from the passenger position.
     */
    open val baseNameTagOffset: Float = 0.2f

    /**
     * The name tag offset for additional lines.
     */
    open val additionalNameTagOffset: Float = 0.25f

    /**
     * The generated profile name for this fake player.
     */
    val defaultProfileName: String by lazy { uuid.toString().substring(0..<16) }

    private var nameElementAttachment: EntityAttachment? = null

    init {
        refreshNameElements()
    }

    /**
     * Refreshes attached elements.
     */
    fun refreshNameElements() {
        nameElementAttachment?.destroy()

        val elements = createNameAttachmentElement()
        val holder = ElementHolder()
        elements.forEach(holder::addPassengerElement)
        nameElementAttachment = EntityAttachment.ofTicking(holder, this)
    }

    /**
     * Creates the text displayed above the player's head.
     * @return a list of text components to be displayed, first highest
     */
    open fun createDisplayNameText(): List<Text> {
        return emptyList()
    }

    /**
     * Creates the attached elements for the player's name tag.
     * @return null to use default name tag
     */
    open fun createNameAttachmentElement(): List<TextDisplayElement> {
        val components = createDisplayNameText()
        return components.reversed().mapIndexed { i, text ->
            val element = TextDisplayElement(text)

            val transformation = AffineTransformation(Vector3f(0.0f, baseNameTagOffset + (additionalNameTagOffset * i), 0.0f), null, null, null)
            element.setTransformation(transformation)

            element.billboardMode = DisplayEntity.BillboardMode.CENTER
            element.teleportDuration = 1

            element
        }
    }

    /**
     * Creates the player list entry for this player. Necessary for the player entity to spawn.
     */
    open fun createPlayerListEntry(): PlayerListS2CPacket.Entry {
        return PlayerListS2CPacket.Entry(
            uuid,
            createProfile(),
            false, // listed
            0, // latency
            GameMode.DEFAULT,
            null, // player list name
            true, // show hat layer
            Integer.MAX_VALUE, // list order
            null, // chat session
        )
    }

    /**
     * Creates the profile name for this player.
     * Will be displayed in tab completion & the social interaction menu.
     */
    open fun createProfileName(): String {
        return defaultProfileName
    }

    /**
     * Creates the complete profile for this player.
     */
    open fun createProfile(): GameProfile {
        val name = createProfileName()
        return GameProfile(uuid, name).apply {
            skinData?.also { data ->
                properties.put("textures", Property("textures", data.value, data.signature))
            }
        }
    }

    override fun onBeforeSpawnPacket(player: ServerPlayerEntity, consumer: Consumer<Packet<*>>) {
        // send fake player information
        val packet = PlayerListS2CPacket(EnumSet.of(PlayerListS2CPacket.Action.ADD_PLAYER), emptyList()).apply {
            val entry = createPlayerListEntry()
            entries = listOf(entry)
        }

        player.networkHandler.sendPacket(packet)
    }

    override fun modifyRawTrackedData(entries: MutableList<DataTracker.SerializedEntry<*>>, player: ServerPlayerEntity, initial: Boolean) {
        // add model part data
        if (initial) {
            entries.add(DataTracker.SerializedEntry.of(PlayerEntity.PLAYER_MODEL_PARTS, modelParts))
        }
    }

    override fun onStoppedTrackingBy(player: ServerPlayerEntity) {
        // remove fake player
        val packet = PlayerRemoveS2CPacket(listOf(uuid))
        player.networkHandler.sendPacket(packet)
    }

    override fun getMainArm(): Arm {
        return Arm.RIGHT
    }

    override fun getPolymerEntityType(context: PacketContext): EntityType<*> {
        return EntityType.PLAYER
    }

    data class SkinData(val value: String, val signature: String? = null)
}
