package io.passkeeper.views


import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val passView by cssclass()
        val content by cssclass()
        val heading by cssclass()
    }

    init {
        passView {
            padding = box(10.px)
            backgroundColor += LinearGradient(
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    true,
                    CycleMethod.NO_CYCLE,
                    Stop(0.0, c("#1a195f")),
                    Stop(1.0, c("#613b72"))
            )
            heading {
                fontSize = 3.em
                textFill = Color.WHITE
                fontWeight = FontWeight.BOLD
            }
            content {
                padding = box(25.px)
                button {
                    fontSize = 22.px
                }
            }
            label {
                textFill = Color.WHITE
            }
        }
    }
}