package net.mcbrawls.populus.test

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback

object PopulusTest : ModInitializer {
    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            PopulusCommand.register(dispatcher)
        }
    }
}
