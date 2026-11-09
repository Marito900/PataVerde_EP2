package cl.duoc.pataverde.dao;
import cl.duoc.pataverde.model.Gato;
import cl.duoc.pataverde.model.Mascota;
import cl.duoc.pataverde.model.Perro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class MascotaDAO {
    private static final String ARCHIVO_PERROS = "perros.json";
    private static final String ARCHIVO_GATOS = "gatos.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

public List<Perro> cargarPerros() throws IOException {
    File archivo = new File(ARCHIVO_PERROS);
    if (!archivo.exists()) {
        return new ArrayList<>();
    }
    try (FileReader lector = new FileReader(archivo)) {
        List<Perro> perros = gson.fromJson(lector,new TypeToken<List<Perro>>(){}.getType());
        if (perros == null) {
            return new ArrayList<>();
        }
        return perros;
    }
}
public List<Gato> cargarGatos() throws IOException {
    File archivo = new File(ARCHIVO_GATOS);
    if (!archivo.exists()) {
        return new ArrayList<>();
    }
    try (FileReader lector = new FileReader(archivo)) {
        List<Gato> gatos = gson.fromJson(lector,new TypeToken<List<Gato>>(){}.getType());
        if (gatos == null) {
            return new ArrayList<>();
        }
        return gatos;
    }
}

public void guardarPerros(List<Perro> perros) throws IOException{
    try (FileWriter guardadoDeFirulais = new FileWriter(ARCHIVO_PERROS)) {
        gson.toJson(perros, guardadoDeFirulais);
    }
}

public void guardarGatos(List<Gato> gatos) throws IOException{
    try (FileWriter guardadoDeMichis = new FileWriter(ARCHIVO_GATOS)) {
        gson.toJson(gatos, guardadoDeMichis);
    }
}

}
