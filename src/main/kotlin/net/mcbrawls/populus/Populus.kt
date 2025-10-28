package net.mcbrawls.populus

import eu.pb4.polymer.core.api.entity.PolymerEntityUtils
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier

object Populus : ModInitializer {
    const val MOD_ID = "populus"

    val TYPE: EntityType<NpcEntity> = registerEntity(
        "npc",
        EntityType.Builder.create(::NpcEntity, SpawnGroup.MISC)
            .dimensions(0.6F, 1.8F)
            .eyeHeight(1.62F)
            .vehicleAttachment(PlayerEntity.VEHICLE_ATTACHMENT)
            .maxTrackingRange(32)
            .trackingTickInterval(2)
        )

    override fun onInitialize() {
        FabricDefaultAttributeRegistry.register(TYPE, MobEntity.createMobAttributes())
    }

    private fun <E : Entity> registerEntity(id: String, builder: EntityType.Builder<E>): EntityType<E> {
        val key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID, id))
        val type = builder.build(key)
        PolymerEntityUtils.registerType(type)
        return Registry.register(Registries.ENTITY_TYPE, key, type)
    }
}
