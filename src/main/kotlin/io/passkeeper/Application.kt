package io.passkeeper

import io.passkeeper.views.PassView
import io.passkeeper.views.Styles
import tornadofx.App
import tornadofx.launch

class Application : App(PassView::class, Styles::class)

fun main(args: Array<String>) {
    launch<Application>(args)
}