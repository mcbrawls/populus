package net.mcbrawls.populus

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.EscapeDangerGoal
import net.minecraft.entity.ai.goal.WanderAroundGoal
import net.minecraft.world.World

class NpcEntity(type: EntityType<NpcEntity>, world: World) : AbstractFakePlayerEntity(type, world) {
    override fun initGoals() {
        goalSelector.add(0, EscapeDangerGoal(this, 0.6))
        goalSelector.add(1, WanderAroundGoal(this, 0.3))
    }
}
