package net.mcbrawls.populus

import eu.pb4.polymer.virtualentity.api.ElementHolder
import eu.pb4.polymer.virtualentity.api.elements.TextDisplayElement
import net.minecraft.entity.decoration.DisplayEntity
import net.minecraft.text.Text
import net.minecraft.util.math.AffineTransformation
import org.joml.Vector3f

class NameTagElementHolder : ElementHolder() {
    /**
     * The base name tag offset from the passenger position.
     */
    var baseNameTagOffset: Float = 0.2f
        set(value) {
            field = value
            refreshElements()
        }

    /**
     * The name tag offset for additional lines.
     */
    var additionalNameTagOffset: Float = 0.25f
        set(value) {
            field = value
            refreshElements()
        }

    private val components: MutableList<Text> = mutableListOf()

    fun setText(text: Collection<Text>) {
        components.clear()
        components.addAll(text)

        refreshElements()
    }

    fun setText(vararg text: Text) {
        setText(text.toList())
    }

    /**
     * Creates the attached elements for the player's name tag.
     * @return null to use default name tag
     */
    fun createElements(): List<TextDisplayElement> {
        return components.reversed().mapIndexed { i, text ->
            val element = TextDisplayElement(text)

            val translation = Vector3f(0.0f, baseNameTagOffset + (additionalNameTagOffset * i), 0.0f)
            val transformation = AffineTransformation(translation, null, null, null)
            element.setTransformation(transformation)

            element.billboardMode = DisplayEntity.BillboardMode.CENTER
            element.teleportDuration = 1

            element
        }
    }

    fun refreshElements() {
        entityIds.forEach(attachedPassengerEntityIds::removeInt)

        if (attachedPassengerEntityIds.isEmpty()) {
            val elements = createElements()
            elements.forEach(::addPassengerElement)
        }
    }
}
