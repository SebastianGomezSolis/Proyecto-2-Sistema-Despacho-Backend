# FarmaApp – Backend

Módulo servidor del sistema de gestión de recetas médicas FarmaApp. Contiene exclusivamente las capas de **datos**, **lógica de negocio** y **servicios TCP**, sin dependencias de JavaFX ni controladores de UI.

---

## Integrantes

- Sebastián Gómez Solís – [@SebastianGomezSolis](https://github.com/SebastianGomezSolis)
- Cinthya Barahona Guevara – [@aashh16](https://github.com/aashh16)
- Aslhi Gutierrez Romero – [@BCinthya](https://github.com/BCinthya)

---

## Tabla de contenidos

- [Descripción](#descripción)
- [Arquitectura interna](#arquitectura-interna)
- [Tecnologías](#tecnologías)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Base de datos](#base-de-datos)
- [Protocolo TCP – ServidorGeneral](#protocolo-tcp--servidorgeneral)
- [Servidor de chat](#servidor-de-chat)
- [Validaciones de negocio](#validaciones-de-negocio)
- [Instalación](#instalación)
- [Ejecución](#ejecución)

---

## Descripción

El backend expone dos servidores TCP independientes:

| Proceso | Clase | Puerto | Descripción |
|---|---|---|---|
| Servidor general | `ServidorGeneral` | 5000 | Recibe peticiones JSON, las enruta a la lógica de negocio y devuelve respuesta JSON |
| Servidor de chat | `HospitalChatServer` | 6000 | Chat en tiempo real con mensajes generales y privados |

Cada petición al `ServidorGeneral` abre y cierra su propio socket. El servidor lanza un hilo por conexión entrante, lo que permite múltiples frontends simultáneos. El servidor de chat mantiene conexiones persistentes por cliente.

---

## Arquitectura interna

```
                        ServidorGeneral :5000
                               │
              ┌────────────────┼────────────────┐
              │                │                │
         manejarMedico   manejarReceta    manejarDashboard  ...
              │                │
        MedicoLogica     RecetaLogica
              │           ┌───┴────────────┐
         MedicoDatos  RecetaDatos   RecetaDetalleDatos
              │           │                │
              └───────────┴────────────────┘
                          │
                   DataBase (HikariCP)
                          │
                     MySQL :3306
                    (BD: hospital)


                   HospitalChatServer :6000
                          │
               ┌──────────┴──────────┐
          UserHandler          UserHandler   ... (1 hilo/cliente)
               │
           UserChat  ← cliente usado por el frontend
```

**Flujo de una petición:**

1. Frontend abre socket a `:5000` y escribe JSON con `DataOutputStream.writeUTF()`.
2. `ServidorGeneral` deserializa la petición (`Peticion` DTO) con Gson.
3. Rutea por `p.tipo` → llama al método `manejar*()` correspondiente.
4. La lógica valida, llama al DAO y devuelve el resultado.
5. El resultado se serializa con Gson y se escribe de vuelta con `DataOutputStream.writeUTF()`.
6. El socket se cierra en ambos extremos.

---

## Tecnologías

| Componente | Tecnología | Versión |
|---|---|---|
| Lenguaje | Java | 23 |
| Comunicación | Sockets TCP (`DataInputStream` / `DataOutputStream`) | – |
| Serialización | Gson | 2.10.1 |
| Base de datos | MySQL | 8 |
| Pool de conexiones | HikariCP | 5.1.0 |
| Logging (chat) | `java.util.logging` + `FileHandler` | – |
| Logging (general) | SLF4J + Logback | 2.0.13 / 1.4.14 |
| Build | Maven Wrapper | 3.8.5 |

> `LocalDate` se serializa/deserializa con el adaptador `LocalDateAdapter` (ISO-8601: `yyyy-MM-dd`) registrado en `GsonProvider`.

---

## Estructura del proyecto

```
src/main/java/una/sistema/backend/proyecto2sistemadespachobackend/
│
├── model/                          # Entidades del dominio (POJOs)
│   ├── Usuario.java                # Clase abstracta: id, identificacion, clave
│   ├── Administrador.java
│   ├── Farmaceuta.java
│   ├── Medico.java
│   ├── Paciente.java               # Incluye LocalDate fechaNacimiento
│   ├── Medicamento.java
│   ├── Receta.java                 # Agrega Paciente + RecetaDetalle + estado
│   └── RecetaDetalle.java          # Agrega Medicamento + cantidad + indicaciones + dias
│
├── datos/                          # Capa de acceso a datos (JDBC puro)
│   ├── DataBase.java               # Singleton HikariCP, configurado desde db.properties
│   ├── AdministradorDatos.java
│   ├── FarmaceutaDatos.java
│   ├── MedicamentoDatos.java
│   ├── MedicoDatos.java
│   ├── PacienteDatos.java
│   ├── RecetaDatos.java            # findAll y findById resuelven FKs (paciente + detalle)
│   └── RecetaDetalleDatos.java     # findAll y findById resuelven FK (medicamento)
│
├── logic/                          # Lógica de negocio y validaciones
│   ├── AdministradorLogica.java
│   ├── FarmaceutaLogica.java       # Valida unicidad de identificacion
│   ├── MedicoLogica.java           # Valida unicidad + búsqueda en memoria por nombre/especialidad
│   ├── PacienteLogica.java         # Valida fecha no futura + unicidad
│   ├── MedicamentoLogica.java      # Valida unicidad de codigo
│   ├── RecetaLogica.java           # Inserta RecetaDetalle si id == 0; valida existencia de FK
│   └── DashBoardLogica.java        # Agrega métricas: totalRecetas, recetasPorEstado
│
└── servicios/
    ├── GsonProvider.java           # Instancia Gson compartida (singleton, thread-safe)
    ├── LocalDateAdapter.java       # JsonSerializer + JsonDeserializer para LocalDate
    ├── ServidorGeneral.java        # TCP :5000 — ruteo, manejo de errores, DTO Peticion
    └── chat/
        ├── HospitalChatServer.java # TCP :6000 — registro, broadcast, privados, lista usuarios
        ├── UserHandler.java        # Thread por cliente: parseo @destino, /usuarios
        └── UserChat.java          # Cliente del chat (usado por el módulo frontend)
```

---

## Base de datos

Script de creación: `hospital.sql`. Base de datos: `hospital`.

### Esquema

```sql
administrador  (id PK, identificacion, clave)
farmaceuta     (id PK, identificacion, clave, nombre)
medico         (id PK, identificacion, clave, nombre, especialidad)
paciente       (id PK, identificacion, nombre, fechaNacimiento DATE, telefono)
medicamento    (id PK, codigo, nombre, descripcion)

recetaDetalle  (id PK,
                medicamentoId FK → medicamento.id  ON UPDATE CASCADE ON DELETE RESTRICT,
                cantidad, indicaciones, diasDuracion)

receta         (id PK,
                identificacion,           -- autogenerado: REC001, REC002...
                pacienteId    FK → paciente.id       ON UPDATE CASCADE ON DELETE RESTRICT,
                recetaDetalleId FK → recetaDetalle.id ON UPDATE CASCADE ON DELETE CASCADE,
                fechaEntrega DATE,
                estado VARCHAR(30))       -- Confeccionada | Proceso | Lista | Entregada
```

### Configuración del pool

Crear `src/main/resources/db.properties` (ignorado por `.gitignore`):

```properties
db.url=jdbc:mysql://localhost:3306/hospital?useSSL=false&allowPublicKeyRetrieval=true&serverTimeZone=UTC
db.user=root
db.password=tu_clave
db.pool.size=20
```

El pool se inicializa en el bloque `static` de `DataBase`. Parámetros adicionales configurados:

```
minimumIdle      = 2
connectionTimeout = 10 000 ms
idleTimeout      = 60 000 ms
maxLifetime      = 1 800 000 ms
```

---

## Protocolo TCP – ServidorGeneral

### Formato de la petición

JSON serializado con Gson, enviado con `DataOutputStream.writeUTF()`:

```json
{
  "tipo": "medico",
  "op":   "findAll"
}
```

Campos del DTO `Peticion`:

| Campo | Tipo | Usado en |
|---|---|---|
| `tipo` | `String` | Siempre — determina la entidad |
| `op` | `String` | Siempre — determina la operación |
| `id` | `int` | `findById`, `deleteById` |
| `identificacion` | `String` | `findByIdentificacion` |
| `codigo` | `String` | `findByCodigo` (medicamento) |
| `data` | `JsonElement` | `create`, `update` — cuerpo del objeto |

### Operaciones por entidad

| `tipo` | Operaciones disponibles |
|---|---|
| `medico` | `findAll` `findById` `findByIdentificacion` `create` `update` `deleteById` |
| `paciente` | `findAll` `findById` `findByIdentificacion` `create` `update` `deleteById` |
| `farmaceuta` | `findAll` `findById` `findByIdentificacion` `create` `update` `deleteById` |
| `administrador` | `findAll` `findById` `findByIdentificacion` `create` `update` `deleteById` |
| `medicamento` | `findAll` `findById` `findByCodigo` `create` `update` `deleteById` |
| `receta` | `findAll` `findById` `create` `update` `deleteById` |
| `dashboard` | `cargarRecetas` `totalRecetas` `recetasPorEstado` |

### Respuestas

- **Éxito:** objeto o lista serializado en JSON.
- **Error de negocio / validación:** `{"error":"NombreExcepcion:mensaje"}`.
- **deleteById exitoso:** `{"ok":true}`.

### Ejemplo completo – crear receta

Petición:
```json
{
  "tipo": "receta",
  "op": "create",
  "data": {
    "paciente": { "id": 5 },
    "detalles": {
      "medicamento": { "id": 2 },
      "cantidad": 30,
      "diasDuracion": 10,
      "indicaciones": "1 tableta cada 8 horas"
    },
    "fechaEntrega": "2026-05-01",
    "estado": "Confeccionada"
  }
}
```

Si `detalles.id == 0`, `RecetaLogica` inserta el detalle antes de insertar la receta. La identificación (`REC001`, `REC002`…) se autogenera en `RecetaDatos.insert()` si llega vacía.

---

## Servidor de chat

`HospitalChatServer` escucha en el puerto **6000**. Comunicación con texto UTF-8 plano por líneas (`PrintWriter` / `BufferedReader`).

### Flujo de conexión

1. Cliente abre socket → servidor envía mensaje de bienvenida.
2. Cliente envía su nombre propuesto (primera línea).
3. `register()` garantiza unicidad: si el nombre ya existe, agrega sufijo `(2)`, `(3)`…
4. Se hace `broadcast("[SISTEMA] X se unió")` y se envía `[USUARIOS] a,b,c` a todos.
5. Loop de mensajes hasta que el cliente cierre la conexión.

### Comandos del cliente

| Formato | Resultado |
|---|---|
| `texto libre` | Mensaje general: `nombre: texto` a todos |
| `@destino mensaje` | Mensaje privado: `[PRIVADO] from: msg` al destino, `[PRIVADO a destino] msg` al emisor |
| `/usuarios` | Fuerza reenvío de la lista de usuarios activos |

### Lista de usuarios

El servidor emite `[USUARIOS] nombre1,nombre2,...` a todos los clientes en cada alta o baja. El cliente (`UserChat`) la intercepta antes de pasarla al callback de mensajes.

### Logging

Las conexiones, registros y desconexiones quedan en `hospital-server.log` (modo append) usando `java.util.logging.FileHandler` con `SimpleFormatter`.

---

## Validaciones de negocio

Todas las validaciones se lanzan como `IllegalArgumentException` y son capturadas por `ServidorGeneral`, que las serializa en el campo `error` de la respuesta.

| Entidad | Validaciones |
|---|---|
| `Medico` | identificacion, nombre, especialidad y clave obligatorios; clave ≥ 4 chars; identificacion única |
| `Farmaceuta` | identificacion, nombre y clave obligatorios; clave ≥ 4 chars; identificacion única |
| `Administrador` | identificacion y clave obligatorios; clave ≥ 4 chars |
| `Paciente` | identificacion, nombre y telefono obligatorios; fechaNacimiento no nula ni futura; identificacion única |
| `Medicamento` | codigo, nombre y descripcion obligatorios; codigo único |
| `Receta` | paciente y detalles no nulos; medicamento seleccionado; cantidad > 0; diasDuracion > 0; indicaciones no vacías; existencia de FK validada contra BD |

La unicidad se verifica cargando todos los registros en memoria con `findAll()` y comparando — sin query adicional de existencia.

---

## Instalación

### Prerrequisitos

- Java 23+
- Maven (o `./mvnw` incluido)
- MySQL 8+

### Pasos

```bash
# 1. Clonar
git clone <url-del-repositorio>
cd <nombre-del-proyecto>

# 2. Crear la base de datos
mysql -u root -p < hospital.sql

# 3. Crear db.properties
cat > src/main/resources/db.properties << EOF
db.url=jdbc:mysql://localhost:3306/hospital?useSSL=false&allowPublicKeyRetrieval=true&serverTimeZone=UTC
db.user=root
db.password=tu_clave
db.pool.size=20
EOF

# 4. Compilar
./mvnw compile
```

---

## Ejecución

Los dos procesos deben iniciarse **antes** del frontend.

### Servidor general (puerto 5000)

```bash
./mvnw exec:java -Dexec.mainClass="una.sistema.backend.proyecto2sistemadespachobackend.servicios.ServidorGeneral"
```

### Servidor de chat (puerto 6000)

```bash
./mvnw exec:java -Dexec.mainClass="una.sistema.backend.proyecto2sistemadespachobackend.servicios.chat.HospitalChatServer"
```

Ambos también pueden ejecutarse directamente desde el IDE corriendo su método `main()`.

> Si el frontend corre en otra máquina, modificar el `host` en `SocketService.java` (módulo frontend) para apuntar a la IP del servidor.
