package una.sistema.backend.proyecto2sistemadespachobackend.servicios;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.time.LocalDate;

public final class GsonProvider {
    private static final Gson GSON = new GsonBuilder().
            registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).
            excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
            .serializeNulls()
            .enableComplexMapKeySerialization()
            .disableJdkUnsafe()
            .setPrettyPrinting()
            .create();

    public static Gson get() { return GSON; }
}
