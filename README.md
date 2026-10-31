Agricolab

Aplicación móvil Android que conecta a trabajadores agrícolas con productores que necesitan mano de obra de temporada (poda, riego, cosecha). Los productores publican ofertas de trabajo y los trabajadores las exploran, las filtran y conversan con el contratante mediante un chat interno.

Proyecto desarrollado para la asignatura Desarrollo de Aplicaciones Móviles (DSY1105), Duoc UC.

Integrantes
Nombre	Rol en el proyecto
[Nombre 1]	[Ej: Registro, login y Room]
[Nombre 2]	[Ej: Chat y mensajería]
[Nombre 3]	[Ej: Publicaciones y búsqueda]

Tablero de tareas (Trello): [pegar enlace del tablero]

Funcionalidades

Cuenta de usuario

Registro en dos pasos con validación de cada campo: correo, celular (8 dígitos después del +56 9), contraseña de al menos 6 caracteres y su confirmación, aceptación de términos, RUT con formato chileno y número de documento.
Selección de rol: Trabajador o Productor.
Foto del documento de identidad con la cámara del teléfono.
Inicio de sesión con RUT y contraseña, validado contra la base de datos local, con una animación de carga y mensajes de error en un modal.
La sesión se mantiene al cerrar y volver a abrir la aplicación.

Publicaciones

Inicio con banner, filtros por categoría (Todos, Poda, Riego, Cosecha) y tarjetas con foto, precio, ubicación y duración.
Pantalla de detalle con descripción, requisitos, contratante y pago estimado.
Búsqueda por texto (título, lugar o requisitos) combinable con el filtro de categoría.
Creación de publicaciones con formulario validado: fechas reales en formato dd/mm/aaaa, fecha de término posterior a la de inicio, al menos 1 trabajador y pago mayor a 0.

Mensajería

Chat entre usuarios registrados, guardado en Room y actualizado en tiempo real.
Lista de conversaciones con buscador, hora del último mensaje e indicador de mensajes sin leer.
Botón de chat en el detalle de cada publicación, que abre la conversación con quien la publicó.

Otros

Panel de notificaciones desplegable desde la campana.
Perfil con los datos reales del usuario (nombre, RUT, correo, teléfono y rol) y cierre de sesión.
Cumplimiento de la rúbrica
Requisito	Dónde está
Formulario	Registro (Register, RegisterPart2), Login, PantallaCrearPublicacion
Validación de campos	UsuarioViewModel (registro y login) y PublicacionViewModel (publicaciones)
Imagen	Fotos de las publicaciones en res/drawable y logo en el Login
Animación	Indicador de carga en el botón de inicio de sesión
Modal	DialogAgricol, usado en el login, el registro y los avisos
Persistencia con Room	AppDatabase con las entidades Usuario, Publicacion, Conversacion y Mensaje
Navegación entre vistas	Navegacion (NavHost) y la barra inferior de Home
Recurso nativo	Cámara en CamaraDocumento (permiso CAMERA y FileProvider)
Arquitectura

El proyecto sigue el patrón MVVM. Cada capa tiene una única responsabilidad:

Pantalla (ui)  ->  ViewModel  ->  Repository  ->  DAO  ->  Room
 dibuja y avisa    valida y        única puerta     define      guarda en
 los eventos       decide          de entrada       consultas   el teléfono
Capa	Responsabilidad
Model	Define los datos (entidades de Room y modelos de formulario). No contiene lógica
Repository	Accede a Room mediante los DAO. Es lo único que conoce la base de datos
ViewModel	Valida, decide y expone el estado con StateFlow. No dibuja nada
UI	Pantallas en Jetpack Compose. Muestran el estado y avisan los eventos al ViewModel

El estado de cada pantalla se publica con StateFlow y las pantallas lo observan con collectAsState(), de modo que la interfaz se actualiza sola cuando cambian los datos. Las operaciones de base de datos son funciones suspend que se ejecutan dentro de viewModelScope.

Tecnologías
Herramienta	Versión
Lenguaje	Kotlin 2.2.10
Interfaz	Jetpack Compose con Material 3 (BOM 2026.02.01)
Base de datos	Room 2.7.0 (con KSP 2.3.11)
Navegación	Navigation Compose 2.10.2
Asincronía y estado	Coroutines y Flow
Gradle / AGP	Gradle 9.6.0 y Android Gradle Plugin 9.2.1
SDK de Android	minSdk 24, targetSdk 36, compileSdk 37.1
Estructura del proyecto
app/src/main/java/cl/duoc/agricol/
├── MainActivity.kt        # Crea la base de datos y arma la cadena Repository -> ViewModel
├── model/                 # Usuario, Publicacion, Conversacion, Mensaje, Estado,
│                          # Notificacion, y los formularios con sus errores
├── repository/            # AppDatabase, UsuarioDao, PublicacionesDao, ChatDao y sus repositorios
├── viewmodel/             # Usuario, Publicacion, Buscar, Conversaciones, Mensaje y Notificacion
└── ui/theme/              # Pantallas, componentes reutilizables, colores, tipografía y tema
    ├── Navegacion.kt      # Rutas de la aplicación
    ├── Home.kt            # Contenedor con la barra superior e inferior
    ├── Login / Register / RegisterPart2
    ├── PantallaInicio / PantallaDetalle / PantallaBuscar / PantallaCrearPublicacion
    ├── PantallaMensajes / PantallaChat / PantallaPerfil / PanelNotificaciones
    ├── DialogAgricol.kt   # Modal reutilizable
    └── CamaraDocumento.kt # Captura de la foto del documento
Cómo ejecutar el proyecto
Requisitos
Android Studio reciente, compatible con Android Gradle Plugin 9.2.1.
JDK 21. El proyecto lo solicita y Gradle intenta descargarlo solo si no lo encuentra.
Android SDK Platform 37.1, que Android Studio ofrece instalar al abrir el proyecto.
Conexión a internet en la primera sincronización, para descargar dependencias.
Un emulador con Android 7.0 (API 24) o superior, o un teléfono con la depuración USB activada.
Pasos
Clonar el repositorio:
bash
   git clone https://github.com/Bot-Matu/proyectoappmovil.git
Abrir la carpeta en Android Studio (File > Open).
Esperar a que termine Sync Project with Gradle Files.
Seleccionar un emulador o dispositivo y presionar Run (Shift + F10).
Guía rápida para probar la aplicación
Registra una primera cuenta con el rol Productor (en Registro, luego "Continuar" y completa la verificación).
Registra una segunda cuenta con el rol Trabajador.
Inicia sesión con la segunda cuenta y recorre las cuatro pestañas: Inicio, Buscar, Mensajes y Perfil.
Abre el detalle de una publicación y toca el botón de chat para escribirle al contratante.
Cierra la aplicación por completo y vuelve a abrirla: entrarás directo a Inicio, porque la sesión se guardó.
En el Perfil usa "Cerrar Sesión" para volver al Login.
Datos de ejemplo

La primera vez que se abre la aplicación se guardan tres publicaciones de ejemplo (poda, riego y cosecha), que quedan asociadas a la primera cuenta registrada. Por eso esa cuenta no puede abrir un chat con sus propias publicaciones.

Limitaciones conocidas
Todos los datos se guardan solo en el dispositivo, no hay servidor.
Las contraseñas se guardan sin cifrar en Room. En una aplicación real se guardaría un hash.
Las notificaciones y el mensaje de "Recuperar contraseña" son una simulación y no envían nada.
El botón "Continuar con Google" y el botón "Aplicar / Postular" todavía no tienen funcionalidad.
La foto del documento se muestra en pantalla, pero no se guarda ni se verifica.
El rol (Trabajador o Productor) hoy solo se muestra en el perfil, y ambos roles pueden publicar y chatear.
El retraso de un segundo en el inicio de sesión es una simulación del tiempo de verificación.
