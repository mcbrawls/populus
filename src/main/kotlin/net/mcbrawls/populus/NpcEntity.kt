package net.mcbrawls.populus

import net.minecraft.entity.EntityType
import net.minecraft.entity.ai.goal.EscapeDangerGoal
import net.minecraft.entity.ai.goal.WanderAroundGoal
import net.minecraft.text.Text
import net.minecraft.world.World
import java.util.Base64

class NpcEntity(type: EntityType<NpcEntity>, world: World) : AbstractFakePlayerEntity(type, world) {
    override val skinData: SkinData = DEFAULT_SKIN

    override fun initGoals() {
        goalSelector.add(0, EscapeDangerGoal(this, 0.6))
        goalSelector.add(1, WanderAroundGoal(this, 0.3))
    }

    override fun createDisplayNameText(): List<Text> {
        val name = createProfileName()
        return listOf(Text.literal("Fake ($name)"))
    }

    companion object {
        val DEFAULT_SKIN = SkinData(
            encode(
                """
                    {
                      "timestamp" : 1755182191153,
                      "profileId" : "50c7e7c524074102875fe4b1f49ea61a",
                      "profileName" : "dvitski",
                      "signatureRequired" : true,
                      "textures" : {
                        "SKIN" : {
                          "url" : "http://textures.minecraft.net/texture/88aac6c902ac544377285161afa1d689ded118edf8330618d65c4649607a0585",
                          "metadata" : {
                            "model" : "slim"
                          }
                        },
                        "CAPE" : {
                          "url" : "http://textures.minecraft.net/texture/1de21419009db483900da6298a1e6cbf9f1bc1523a0dcdc16263fab150693edd"
                        }
                      }
                    }
                """.trimIndent()
            ),
            "MMfw9jGE8SRxGUPtXPsiqMv03hlFjZN4rfzDbScZxuIS+ugYK/ARU8839AcRVytfJgah8FBp3/FzB5dmW/8mh3gPPtm8wp//qM6vVcSifKu31T123H+u3+xNiDS3Ox+/+Fnd5t4x96v59/QcYoXIqLM2bxtMyBDEqeevUD41N5taRFpAs/2oS/p9ER3yInZchfoDwbAa3Dz1vBa2EKKNYQdxJLg86THqa6jq/ccgisEmzwHlSYN8/EAY7fYlN89oEUqxljLudp+8l48pt+nAo8+IOkWOF+T1UujsweoO5mYONM3v5dmpYGICFPUDPztZH1heSixYTz6N0k4XXmajZ/f7QeKy4O+sTHM39lbfAoQA9k311IweRkIkZuwc3eip/UIX+R/VS3YfBMCDZVPzqTMWhNf6FMirUAFL0WvzVK9qu+qR1Hpei3HZU+35c1ibKWKFiG3eoWvzB9eOvQCAtSx9NGeigUHEkzsfuTKfVHO+4w/MMjrqt8Be0CiE0BClBWjxjp5DzS14udCz0QXvHWF5os9HAss9oIUExQ9HOPxR7ZyvXKWGDcZdnvQoPqw/iOVwxt1nmjroxl3Sz9xCxp+YRRX2kUb+gYVlYor5ZPy9rJtMGWPQyMJVGAq/7dY55qR0Hw6lK6sO/EF688NiHjrD44LSAl6uqaobG0IYKww="
        )

        fun encode(string: String): String {
            return Base64.getEncoder().encodeToString(string.encodeToByteArray());
        }
    }
}
