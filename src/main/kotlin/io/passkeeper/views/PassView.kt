package io.passkeeper.views

import io.passkeeper.model.entity.Password
import io.passkeeper.model.entity.password
import io.passkeeper.model.repository.SimpleInMemoryRepository
import javafx.geometry.Pos
import javafx.scene.image.Image
import tornadofx.*

class PassView : View("PassKeeper") {

    private val repository = SimpleInMemoryRepository()

    private val passwordModel = PasswordModel(password())
    private val passwordList = SortedFilteredList(repository.list().toMutableList().asObservable())

    init {
        passwordList.setAllPassThrough = true
        passwordList.sortedItems.comparator = Comparator<Password> { p1, p2 ->
            p1.domain.compareTo(p2.domain)
        }
    }

    override val root = borderpane {
        //TODO: rewrite with event bus
        addClass(Styles.passView)
        val leftList = listview<Password> {
            items = passwordList
            cellFormat {
                graphic = cache(it.domain) {
                    text(it.domain)
                }
            }
            contextmenu {
                item("Delete").action {
                    selectedItem?.apply {
                        println("Delete: ${this.domain}")
                        repository.delete(this.domain)
                        this@listview.asyncItems { repository.list() }
                        this@listview.refresh()
                    }
                }
            }
            passwordModel.rebindOnChange(this) {
                item = it ?: password()
            }
        }
        top {
            vbox {
                hbox {
                    imageview(Image("logo.png", 60.0, 120.0, true, true)) {
                        alignment = Pos.CENTER_LEFT
                        paddingHorizontal = 30
                    }
                    label(title).addClass(Styles.heading)
                }
                hbox {
                    addClass(Styles.content)
                    button("New") {
                        setOnAction {
                            val newPassword = password()
                            repository.createOrUpdate(newPassword)
                            passwordList.add(newPassword)
                            leftList.selectionModel.select(newPassword)
                            leftList.asyncItems { repository.list() }
                            leftList.refresh()
                        }
                    }
                    button("Refresh") {
                        setOnAction {
                            leftList.asyncItems { repository.list() }
                            leftList.refresh()
                        }
                    }
                }
            }
        }
        left = leftList
        center {
            vbox {
                button("Save") {
                    enableWhen { passwordModel.valid and passwordModel.dirty }
                    setOnAction {
                        passwordModel.commit()
                        repository.createOrUpdate(Password(
                                domain = passwordModel.website.value,
                                username = passwordModel.username.value,
                                password = passwordModel.password.value,
                                notes = passwordModel.notes.value
                        ))
                        runAsync {
                            runLater {
                                passwordList.setAll(repository.list().toMutableList())
                            }
                        }
                    }
                }
                form {
                    fieldset("Password Info") {
                        field("website") {
                            textfield(passwordModel.website).required()
                        }
                        field("username") {
                            textfield(passwordModel.username)
                        }
                        field("password") {
                            textfield(passwordModel.password)
                        }
                        field("notes") {
                            textarea(passwordModel.notes)
                        }
                    }
                }
            }
        }
    }
}

class PasswordModel(password: Password) : ItemViewModel<Password>(password) {
    val username = bind(Password::username)
    val password = bind(Password::password)
    val website = bind(Password::domain)
    val notes = bind(Password::notes)
}