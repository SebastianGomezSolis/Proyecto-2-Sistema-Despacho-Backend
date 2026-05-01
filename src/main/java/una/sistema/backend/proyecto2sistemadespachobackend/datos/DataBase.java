package una.sistema.backend.proyecto2sistemadespachobackend.datos;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class DataBase {
    private static HikariDataSource ds;

    private DataBase() {}

    // Aqui vamos a configurar la conexion de la aplicacion a la base de datos
    static {
        try (InputStream in = DataBase.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            prop.load(in);

            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(prop.getProperty("db.url"));
            cfg.setUsername(prop.getProperty("db.user"));
            cfg.setPassword(prop.getProperty("db.password"));
            cfg.setMaximumPoolSize(Integer.parseInt(prop.getProperty("db.pool.size", "10")));
            cfg.setPoolName("MyAppPool");

            // Configuracion opcional pero recomendada por el profe
            cfg.setMinimumIdle(2);
            cfg.setConnectionTimeout(10000);
            cfg.setIdleTimeout(60000);
            cfg.setMaxLifetime(1800000);

            ds = new HikariDataSource(cfg);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo iniciar el pool de conexiones", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
