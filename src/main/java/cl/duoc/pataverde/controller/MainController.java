package cl.duoc.pataverde.controller;

import cl.duoc.pataverde.model.Perro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control. *;
import cl.duoc.pataverde.model.Mascota;
import cl.duoc.pataverde.model.Gato;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import cl.duoc.pataverde.dao.MascotaDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private TextField txtNombreDueño;
    @FXML
    private TextField txtRutDueño;
    @FXML
    private TextArea txtAntecedentes;
    @FXML
    private TableColumn<Mascota, Number> colId;
    @FXML
    private TableColumn<Mascota, String> colDueño;
    @FXML
    TableColumn<Mascota, String> colRut;
    @FXML
    private ComboBox<String> cmbTipo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtEdad;
    @FXML
    private TextField txtPeso;
    @FXML
    private TextField txtRaza;
    @FXML
    private CheckBox chkVacunado;
    @FXML
    private CheckBox chkAdopcion;
    @FXML
    private CheckBox chkExterior;
    @FXML
    private TextArea txtMotivo;
    @FXML
    private TextField txtMensaje;
    @FXML
    private TableView<Mascota> tblMascotas;
    @FXML
    private TableColumn<Mascota, String> colTipo;
    @FXML
    private TableColumn<Mascota, String> colNombre;
    @FXML
    private TableColumn<Mascota, Number> colEdad;
    @FXML
    private TableColumn<Mascota, Number> colPeso;
    @FXML
    private TableColumn<Mascota, Number> colCosto;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnEliminar;
    @FXML
    private void registrarPaciente() {
        String nombre = txtNombre.getText();
        String nombreDueño = txtNombreDueño.getText().trim();
        String rutDueño = txtRutDueño.getText().trim();

        if (nombreDueño.isEmpty() || rutDueño.isEmpty()) {
            txtMensaje.setText("Ingrese Nombre y Apellido del Dueño");
            return;
        }


        if (nombre.trim().isEmpty()) {
            txtMensaje.setText("Ingrese un nombre");
            return;
        }
        String motivo = txtMotivo.getText().trim();
        if (motivo.isEmpty()) {
            txtMensaje.setText("Ingrese Motivo de la consulta: ");
            return;
        }
        if (motivo.length() > 100) {
            txtMensaje.setText("El motivo no puede superar los 100 caracteres");
            return;
        }
        int edad;
        try {
            edad = Integer.parseInt(txtEdad.getText().trim());
        } catch (NumberFormatException e) {
            txtMensaje.setText("Ingrese la edad ");
            return;
        }
        if (edad < 0 || edad > 30) {
            txtMensaje.setText("La edad debe estar entre 0 y 30 años");
            return;
        }

        double peso;
        try {
            peso = Double.parseDouble(txtPeso.getText().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            txtMensaje.setText("El peso debe ser un numero");
            return;
        }
        if (!Double.isFinite(peso) || peso <= 0) {
            txtMensaje.setText("La peso debe ser mayor a 0");
            return;

        }

        String tipo = cmbTipo.getValue();
        Mascota mascota;
        if ("Perro".equals(tipo)) {
            String raza = txtRaza.getText().trim();
            if (raza.trim().isEmpty()) {
                txtMensaje.setText("Ingrese la raza del animal ");
                return;
            }
            Perro perro = new Perro(
                    nombre,
                    edad,
                    peso,
                    raza,
                    chkVacunado.isSelected()
            );
            if (chkAdopcion.isSelected()) {
                perro.marcarDisponibleParaAdopcion();
            }
            mascota = perro;
        } else if ("Gato".equals(tipo)) {
            mascota = new Gato(
                    nombre,
                    edad,
                    peso,
                    chkExterior.isSelected()
            );

        } else {
            txtMensaje.setText("Seleccione Perro o Gato");
            return;
        }
        mascota.setMotivoConsulta(motivo);
        mascota.setNombreDueño(txtNombreDueño.getText().trim());
        mascota.setRutDueño(txtRutDueño.getText().trim());
        mascota.setId(generarSiguienteId());
        mascotas.add(mascota);
        try {
            guardarMascotas();
            txtMensaje.setText("Paciente registrado : " + mascota.getNombre());
        } catch (IOException e) {
            txtMensaje.setText("Error al registrar Paciente");
        }
    }

    private ObservableList<Mascota> mascotas = FXCollections.observableArrayList();
    private MascotaDAO dao = new MascotaDAO();
    private int generarSiguienteId() {
        int mayorId = 0;
        for (Mascota mascota : mascotas) {
            if (mascota.getId() > mayorId) {
                mayorId = mascota.getId();
            }
        }
        return mayorId + 1;
    }

    @FXML
    private void eliminarPaciente() {
        Mascota seleccionada = tblMascotas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            txtMensaje.setText("Seleccione un paciente de la tabla");
            return;
        }
        mascotas.remove(seleccionada);
        try {
            guardarMascotas();
            txtMensaje.setText("Paciente eliminado : " + seleccionada.getNombre());
        } catch (IOException e) {
            mascotas.add(seleccionada);
            txtMensaje.setText("Error al eliminar Paciente");
        }
    }
        @FXML
        private void initialize () {
            cmbTipo.getItems().addAll("Perro", "Gato");
            cmbTipo.setOnAction(evento -> actualizarCampos());
            actualizarCampos();
            tblMascotas.setItems(mascotas);
            tblMascotas.getSelectionModel().selectedItemProperty().addListener
                    ((observable, anterior, seleccionada) -> {
                        if (seleccionada != null) {
                            txtAntecedentes.setText(seleccionada.getMotivoConsulta());
                        }
                    });
            cargarMascotas();
            colId.setCellValueFactory(dato -> new ReadOnlyObjectWrapper<Number>(dato.getValue().getId()));
            colDueño.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getNombreDueño()));
            colRut.setCellValueFactory(dato -> new SimpleStringProperty(dato.getValue().getRutDueño()));
            colTipo.setCellValueFactory(dato -> new
                    SimpleStringProperty(dato.getValue() instanceof Perro ? "Perro" : "Gato"));
            colNombre.setCellValueFactory(dato -> new
                    SimpleStringProperty(dato.getValue().getNombre()));
            colEdad.setCellValueFactory(dato -> new
                    ReadOnlyObjectWrapper<Number>(dato.getValue().getEdad()));
            colPeso.setCellValueFactory(dato -> new
                    ReadOnlyObjectWrapper<Number>(dato.getValue().getPeso()));
            colCosto.setCellValueFactory(dato -> new
                    ReadOnlyObjectWrapper<Number>(dato.getValue().calcularCostoConsulta())
            );
        }

        private void actualizarCampos () {
            String tipo = cmbTipo.getValue();
            boolean perro = "Perro".equals(tipo);
            boolean gato = "Gato".equals(tipo);

            txtRaza.setDisable(!perro);
            chkVacunado.setDisable(!perro);
            chkAdopcion.setDisable(!perro);
            chkExterior.setDisable(!gato);
        }

        private void guardarMascotas() throws IOException {

            List<Perro> perros = new ArrayList<>();
            List<Gato> gatos = new ArrayList<>();
            for (Mascota mascota : mascotas) {
                if (mascota instanceof Perro) {
                    perros.add((Perro) mascota);
                }
                if (mascota instanceof Gato) {
                    gatos.add((Gato) mascota);
                }
            }
            dao.guardarPerros(perros);
            dao.guardarGatos(gatos);
        }


        private void cargarMascotas () {
            try {
                List<Perro> perros = dao.cargarPerros();
                List<Gato> gatos = dao.cargarGatos();

                mascotas.setAll(perros);
                mascotas.addAll(gatos);
                int siguienteId = generarSiguienteId();
                boolean faltabanIds = false;
                for (Mascota mascota : mascotas) {
                    if (mascota.getId() == 0) {
                        mascota.setId(siguienteId);
                        siguienteId++;
                        faltabanIds = true;
                    }
                }
                    if (faltabanIds) {
                        guardarMascotas();

                }

            } catch (IOException e) {
                txtMensaje.setText("Error al cargar Pacientes");
            }
        }

}


