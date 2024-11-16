package build365.gst.emsigner

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import org.controlsfx.control.tableview2.TableColumn2
import org.controlsfx.control.tableview2.TableView2
import org.java_websocket.WebSocket

data class Cert(val c: String, val i: String, val s: String, val v: String)
class HelloController {
    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private lateinit var pan: TextField

    @FXML
    private lateinit var logo: ImageView

    @FXML
    private lateinit var cancelBtn: Button

    @FXML
    private lateinit var viewBtn: Button

    @FXML
    private lateinit var signBtn: Button

    @FXML
    private lateinit var certs: TableView2<Cert>


    internal lateinit var ws: WebSocket
    internal var items = SimpleListProperty<Cert>()
    var onBtnAction: () -> Unit = {}

    @FXML
    private fun sign() {
        ws.send("Ok")
        onBtnAction()
    }

    @FXML
    private fun cancel() {
        ws.send("Cancel")
        onBtnAction()
    }


    @FXML
    private fun initialize() {
        val resourceAsStream = this.javaClass.getResourceAsStream("logo.png")
        println("image? ${resourceAsStream != null}")
        logo.image = Image(resourceAsStream)
        logo.fitWidth = 50.0
        logo.fitHeight = 50.0
        logo.isPreserveRatio = true
        cancelBtn.styleClass.addAll("btn", "btn-secondary")
        signBtn.styleClass.addAll("btn", "btn-primary")
        viewBtn.styleClass.addAll("btn", "btn-primary")
        pan.isEditable = false
        certs.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        certs.columns.addAll(
            TableColumn2<Cert, String>("Common Name").apply {
                setCellValueFactory { p -> SimpleStringProperty(p.value.c) }
            },
            TableColumn2<Cert, String>("Issuer Name").apply {
                setCellValueFactory { p -> SimpleStringProperty(p.value.i) }
            },
            TableColumn2<Cert, String>("Serial Name").apply {
                setCellValueFactory { p -> SimpleStringProperty(p.value.s) }
            },
            TableColumn2<Cert, String>("Expiry Date").apply {
                setCellValueFactory { p -> SimpleStringProperty(p.value.v) }
            }
        )
        certs.items = items
    }

    fun setItems(i: List<Cert>) {
        println("setItems ${i.size}")
        certs.items = FXCollections.observableList(i)
        if (i.isNotEmpty()) {
            println("set pan ${i[0].c}")
            pan.text = i[0].c
        }
    }
}