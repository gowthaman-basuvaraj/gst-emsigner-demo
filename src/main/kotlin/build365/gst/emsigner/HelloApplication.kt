package build365.gst.emsigner

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import java.net.InetAddress
import java.net.InetSocketAddress
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class HelloApplication : Application() {
    private lateinit var primaryStage: Stage
    private lateinit var helloController: HelloController
    private val socketServer: WSServer by lazy {
        WSServer(InetSocketAddress(InetAddress.getLoopbackAddress(), 12345), handleConnect = {
            helloController.ws = it
        }) { ws, msg ->
            println("message from $ws => $msg")

            Platform.runLater {
                helloController.setItems(listOf(Cert(
                    c = msg.substring(2, 12),
                    i = "e-Mudhra Sub CA for ...",
                    s = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmm")),
                    v = LocalDate.now().plusDays(40).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )))

                primaryStage.isAlwaysOnTop = true
                primaryStage.isIconified = false
            }
        }
    }

    override fun start(stage: Stage) {
        this.primaryStage = stage
        val fxmlLoader = FXMLLoader(HelloApplication::class.java.getResource("hello-view.fxml"))

        val scene = Scene(fxmlLoader.load(), 800.0, 600.0)
        stage.title = "GST Digital Signature Signer"
        stage.scene = scene
        stage.show()

        helloController = fxmlLoader.getController()
        helloController.onBtnAction = {
            primaryStage.isAlwaysOnTop = false
            primaryStage.isIconified = true
        }

        thread(start = true) {
            socketServer.run()
        }
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}