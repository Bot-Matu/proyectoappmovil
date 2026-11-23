# Agricolab — Documentación técnica

Aplicación Android que conecta a **trabajadores agrícolas** con **productores** que necesitan mano de obra de temporada (poda, riego, cosecha). Los productores publican ofertas, los trabajadores las exploran y conversan con el contratante por un chat interno.

Proyecto de la asignatura Desarrollo de Aplicaciones Móviles (DSY1105), Duoc UC.

---

## 1. Resumen técnico

| Ítem | Valor |
|---|---|
| Plataforma | Android (`minSdk 24`, `targetSdk 36`, `compileSdk 37.1`) |
| Lenguaje | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material 3 (Compose BOM 2026.02.01) |
| Arquitectura | MVVM con repositorios |
| Navegación | Navigation Compose 2.10.2 |
| Base de datos local | Room 2.7.0 (con KSP 2.3.11) |
| Backend | Xano (API REST) |
| Cliente HTTP | Retrofit 2.11.0 + Gson |
| Asincronía | Coroutines + Flow |
| Tests | JUnit 4, kotlinx-coroutines-test 1.9.0, MockWebServer 4.12.0 |
| Paquete | `cl.duoc.agricol` |

**Permisos** (`AndroidManifest.xml`): `INTERNET`, `CAMERA`, `POST_NOTIFICATIONS`. La cámara es opcional (`uses-feature required=false`) y usa un `FileProvider` para guardar la foto del documento.

---

## 2. Arquitectura

```
Pantalla (Compose) → ViewModel → Repository → DAO → Room      (datos locales)
                                      └──────→ API (Retrofit) → Xano   (datos remotos)
```

| Capa | Responsabilidad |
|---|---|
| `ui/theme` | Pantallas Compose. Dibujan el estado y avisan los eventos; no contienen lógica de negocio. |
| `viewmodel` | Validan formularios, mantienen el estado (`StateFlow`) y llaman a los repositorios. |
| `repository` | Única puerta de acceso a los datos. Deciden si leen/escriben en Room, en Xano o en ambos. |
| `remote` | Interfaces Retrofit (`AuthApi`, `PublicacionesApi`, `ChatApi`) y `RetrofitClient`. |
| `model` | Entidades de Room, modelos de formulario y modelos de la API. |

### Principio de diseño: con o sin servidor

Cada repositorio recibe su parte remota como parámetro **opcional**. Si es `null`, funciona solo con Room (como la primera versión del proyecto y como en los tests). `MainActivity` es el único lugar donde se conectan las piezas:

```kotlin
UsuarioRepository(db.usuariosDao(), AuthRepositoryImpl(RetrofitClient.crearAuthApi()))
PublicacionRepository(db.publicacionesDao(), PublicacionesRemotoRepositoryImpl(RetrofitClient.crearApi()))
MensajeRepositoryImpl(db.chatDao(), db.usuariosDao(), ChatRepositoryImpl(RetrofitClient.crearChatApi()))
```

Para volver a modo solo-local basta con quitar el tercer argumento.

---

## 3. Estructura del proyecto

```
app/src/main/java/cl/duoc/agricol/
├── MainActivity.kt              Crea Room, repositorios y ViewModels; lanza Navegacion
├── model/
│   ├── Usuario, Publicacion, Conversacion, Mensaje      Entidades de Room
│   ├── ConversacionConNombre, Estado                    Modelos de apoyo del chat
│   ├── UsuarioForm, PublicacionForm                     Formularios y sus errores
│   ├── ApiModels.kt                                     Login/Signup/Auth/Conversación/Mensaje remotos
│   └── PublicacionRemota.kt                             Publicación tal como viaja por la API
├── remote/
│   ├── AuthApi.kt, ChatApi.kt, PublicacionesApi.kt      Endpoints (Retrofit)
│   └── (RetrofitClient dentro de PublicacionesApi.kt)   URL base y fábricas de las APIs
├── repository/
│   ├── AppDataBase.kt                                   Base Room (versión 4)
│   ├── UsuarioDao, PublicacionesDao, ChatDao            Consultas SQL
│   ├── UsuarioRepository, PublicacionRepository        Lógica local + remota
│   ├── MensajeRepository.kt                             Chat (Room o Xano directo)
│   ├── AuthRepository, ChatRepository,                  Capa remota pura
│   │   PublicacionesRemotoRepository
│   └── NotificacionRepository.kt
├── viewmodel/
│   ├── UsuarioViewModel, PublicacionViewModel, BuscarViewModel
│   ├── ConversacionesViewModel, MensajeViewModel
│   ├── NotificacionViewModel, PublicacionesRemotasViewModel
└── ui/theme/                    Pantallas, tema y componentes (Login, Register*, Home,
                                 PantallaInicio/Buscar/Detalle/CrearPublicacion/Mensajes/Chat/Perfil,
                                 PanelNotificaciones, Notificador, CamaraDocumento, DialogAgricol…)
```

---

## 4. Navegación

`Navegacion.kt` define un `NavHost` con estas rutas:

| Ruta | Pantalla |
|---|---|
| `login` / `register` / `register2` | Autenticación y registro en dos pasos |
| `home` | Contenedor con barra inferior |
| `chat/{conversacionId}/{otroId}` | Chat entre dos usuarios |

El destino inicial es `home` si hay sesión guardada y `login` si no (se decide una vez, cuando `sesionCargada` pasa a `true`).

`Home` contiene 4 pestañas (**Inicio, Buscar, Mensajes, Perfil**) y, sobre ellas, el detalle de una publicación y el formulario de creación, que se muestran con estado local (`publicacionSeleccionada`, `mostrarCrear`) en vez de rutas.

---

## 5. Modelo de datos

### 5.1 Room (`agricolab_db`, versión 4)

La base se declara con `fallbackToDestructiveMigration()`: **si cambia el esquema, se borra y se recrea**. Es aceptable en desarrollo; antes de publicar hay que escribir migraciones reales.

| Entidad (tabla) | Campos principales |
|---|---|
| `Usuario` (`usuarios`) | `id`, `nombre`, `celular`, `correo`, `contrasena`, `rut`, `nro_documento`, `rol`, `sesionActiva`, `token` |
| `Publicacion` (`publicacion`) | `id`, `titulo`, `categoria`, `comuna`, `ciudad`, `fecha_inicio`, `fecha_termino`, `dias`, `tra_necesitados`, `descripcion`, `requisitos`, `info_extra`, `contratante`, `paga`, `creadorId`, `imagen` |
| `Conversacion` (`conversaciones`) | `conversacionId`, `usuario1Id`, `usuario2Id`, `titulo`, `ultimoMensaje`, `ultimoMensajeTiempo` |
| `Mensaje` (`mensajes`) | `id`, `conversacionId` (FK con `CASCADE`), `emisorId`, `contenido`, `tiempo`, `estadoMensaje` |

Notas:
- `Usuario.token` guarda el `authToken` de Xano. Con servidor, `contrasena` queda vacía (la contraseña no se almacena en el teléfono).
- `Publicacion.imagen` es un id de `res/drawable` elegido por categoría (Poda, Riego, Cosecha); **no viaja por la API**.
- `Mensaje.estadoMensaje` admite `ENVIANDO`, `ENVIADO`, `RECIBIDO`, `LEIDO`, `ERROR` (se expone como `Estado`).
- Para sincronizar con el servidor se usa `@Upsert`. En `Conversacion` no se usa `REPLACE`, porque al borrar la fila el `CASCADE` eliminaría sus mensajes.

### 5.2 Modelos de la API (`ApiModels.kt`, `PublicacionRemota.kt`)

Los nombres de campo coinciden con las columnas de las tablas de Xano. Si se renombra una columna en Xano, hay que renombrarla aquí (Gson mapea por nombre).

`LoginRequest`, `SignupRequest`, `AuthResponse(authToken)`, `UsuarioRemoto`, `ConversacionRemota`, `MensajeRemoto`, `PublicacionRemota`.

---

## 6. API (Xano)

URL base (`RetrofitClient`): `https://x8ki-letl-twmt.n7.xano.io/api:GOcHn4ic/`

| Método | Endpoint | Uso | Interfaz |
|---|---|---|---|
| POST | `auth/signup` | Crear cuenta (devuelve token) | `AuthApi` |
| POST | `auth/login` | Iniciar sesión con **correo** y contraseña (devuelve token) | `AuthApi` |
| GET | `auth/me` | Perfil del dueño del token (`Authorization: Bearer <token>`) | `AuthApi` |
| GET | `publicacion` | Listar publicaciones | `PublicacionesApi` |
| POST | `publicacion` | Crear publicación | `PublicacionesApi` |
| DELETE | `publicacion/{id}` | Eliminar publicación (sin botón en la UI aún) | `PublicacionesApi` |
| GET | `conversacion` | Listar conversaciones (se filtra por usuario en el cliente) | `ChatApi` |
| POST | `conversacion` | Crear conversación | `ChatApi` |
| GET | `mensaje` | Listar mensajes (se filtra por conversación en el cliente) | `ChatApi` |
| POST | `mensaje` | Enviar mensaje | `ChatApi` |

Solo `auth/me` envía el token. Los endpoints de publicaciones y chat **no están autenticados** en la app.

---

## 7. Flujos principales

### 7.1 Registro e inicio de sesión
1. `Register` y `RegisterPart2` llenan `UsuarioForm`; `UsuarioViewModel` valida cada campo (correo, celular de 8 dígitos, contraseña ≥ 6, términos, RUT chileno, nº de documento).
2. **Registro:** `UsuarioRepository.registrar` envía `SignupRequest` a `auth/signup`. Errores 4xx se muestran como "ya existe una cuenta con ese correo o RUT"; 5xx como error de servidor; falta de red como sin conexión.
3. **Login:** `auth/login` → token → `auth/me` → se construye el `Usuario` con el **id de Xano** (lo usan publicaciones y chat) y se guarda en Room con `upsert`. `guardarSesion` marca `sesionActiva`, lo que mantiene la sesión al reabrir la app.
4. Sin servidor, el login valida contra Room usando RUT y contraseña.

### 7.2 Publicaciones
- **Lectura:** al iniciar, `PublicacionViewModel.actualizar()` llama a `PublicacionRepository.sincronizar()`, que baja la lista de Xano, hace `upsert` en Room y borra lo que ya no existe en el servidor. Las pantallas observan el `Flow` de Room (Inicio y Buscar filtran en memoria). Sin red, se queda con lo que hay en Room.
- **Creación:** `PublicacionViewModel.publicar` valida (fechas `dd/MM/yyyy` reales, término posterior al inicio, ≥ 1 trabajador, pago > 0), envía a Xano y guarda en Room con el id que devuelve el servidor. Si falla, `PantallaCrearPublicacion` muestra un diálogo de error.
- Sin servidor, `sincronizar()` solo carga 3 publicaciones de ejemplo la primera vez.

### 7.3 Chat
Con servidor, **las conversaciones y los mensajes se leen directamente de Xano** y no pasan por Room (`MensajeRepositoryImpl` con `ChatRepository` remoto). Sin servidor, todo usa Room.

- **Abrir un chat:** `obtenerOCrearConversacion` busca en Xano una conversación entre los dos usuarios (en cualquier orden) y solo crea una si no existe, para evitar duplicados.
- **Chat abierto:** un `Flow` consulta `GET mensaje` y filtra por conversación cada **6 s**, y de inmediato tras enviar.
- **Enviar:** el mensaje aparece al instante como `ENVIANDO`, pasa a `ENVIADO` con el id de Xano al confirmar, o queda en `ERROR` si falla. Los mensajes locales se mezclan con los del servidor y se descartan por id cuando Xano ya los devuelve.
- **Lista de conversaciones:** se arma con `GET conversacion` + `GET mensaje` (2 peticiones) cada **20 s**, solo mientras la pantalla está visible. Una fila por conversación con al menos un mensaje, ordenada por el último.
- **Caché en memoria:** al volver a abrir un chat o la lista se muestra lo último bajado mientras llega lo nuevo.
- **No leídos:** Xano no tiene endpoint para cambiar el estado de un mensaje, así que la app recuerda en memoria hasta qué mensaje del otro se leyó (se pierde al cerrar la app). `marcarComoLeidos` usa los mensajes ya descargados y no hace peticiones.

### 7.4 Notificaciones
- `Notificador` muestra notificaciones del sistema (canal `agricolab_avisos`; en Android 13+ pide el permiso `POST_NOTIFICATIONS` al entrar a `Home`).
- `NotificacionViewModel` mantiene los avisos del panel de la campana. Hoy se genera un aviso al publicar una oferta.

### 7.5 Cámara
`CamaraDocumento` (en `RegisterPart2`) captura la foto del documento con el permiso `CAMERA` y un `FileProvider`. La foto **no se envía a Xano** (`SignupRequest` no la incluye).

---

## 8. Límites de peticiones (HTTP 429)

Xano limita las peticiones por ventana de tiempo, sobre todo en el plan gratis. Para no superarlo, el chat consulta por intervalos (no en tiempo real) y, si recibe `429`, espera **10 s** antes de reintentar.

Las constantes están al inicio de `MensajeRepositoryImpl` (`MensajeRepository.kt`):

| Constante | Valor | Efecto |
|---|---|---|
| `CADA_CHAT_MS` | 6 000 | Intervalo de consulta con un chat abierto |
| `CADA_LISTA_MS` | 20 000 | Intervalo de consulta de la lista de conversaciones |
| `ESPERA_429_MS` | 10 000 | Espera tras recibir `429` |

Bajar los intervalos acelera la llegada de mensajes pero aumenta el riesgo de `429`. Si varios teléfonos usan la misma cuenta de Xano, el consumo se suma. Para mensajes casi instantáneos haría falta tiempo real (por ejemplo, la función de tiempo real de Xano) o notificaciones push.

**Depuración:** Logcat con el filtro `AgricolChat` muestra cuántas conversaciones y mensajes devuelve Xano y los errores de red, HTTP o de formato de respuesta.

---

## 9. Tests

Pruebas unitarias en `app/src/test` (JUnit 4, sin emulador):

| Archivo | Qué prueba |
|---|---|
| `AuthRepositoryTest` | Login, registro y perfil contra un servidor simulado (MockWebServer) |
| `ChatRepositoryTest` | Conversaciones y mensajes contra MockWebServer |
| `PublicacionesRemotoRepositoryTest` | Listar, crear y eliminar publicaciones |
| `UsuarioViewModelTest` | Validación de registro y login con un DAO falso |
| `PublicacionViewModelTest` | Validación y guardado de publicaciones con un DAO falso |
| `PublicacionesRemotasViewModelTest` | Estados Cargando, Éxito y Error |

Los DAO falsos de los tests implementan las interfaces completas: si se agrega un método a `UsuarioDao` o `PublicacionesDao`, hay que agregarlo también al falso. Los tests del chat y de los ViewModels del chat no cubren la lectura directa desde Xano; la prueba hoy es manual.

Ejecutar: `./gradlew testDebugUnitTest`

---

## 10. Compilar y ejecutar

1. Android Studio reciente (el proyecto usa AGP 9.2.1 y Kotlin 2.2.10) y un SDK que incluya la API 37.
2. Abrir la carpeta del proyecto y sincronizar Gradle.
3. Ejecutar el módulo `app` en un emulador o dispositivo con Android 7.0 (API 24) o superior.
4. Se necesita conexión a internet para registro, login, publicaciones y chat.

---

## 11. Limitaciones conocidas y mejoras pendientes

- **Contactos:** la lista para iniciar un chat nuevo en Mensajes sale de Room y queda casi vacía con servidor. Falta un endpoint que liste usuarios.
- **Nombres en el chat:** el nombre de la otra persona sale de Room; si no está, se muestra "Usuario". Falta un endpoint para obtener un usuario por id.
- **Estado de mensajes:** no hay endpoint para marcar mensajes como leídos; el estado "leído" no se comparte entre teléfonos.
- **Filtrado en el cliente:** `GET conversacion` y `GET mensaje` devuelven todo y se filtra en la app. Conviene filtrar en Xano por usuario y por `conversacionId`.
- **Seguridad:** el `token` se guarda en Room sin cifrar; los endpoints de publicaciones y chat no exigen autenticación.
- **Login con Google** y **recuperar contraseña** son solo maquetas.
- **Eliminar publicación:** el repositorio y la API existen, pero no hay botón en la UI.
- **Foto del documento:** se captura pero no se sube.
- **Migraciones de Room:** reemplazar `fallbackToDestructiveMigration()` por migraciones antes de publicar la app.
