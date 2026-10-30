Agricol - Aplicación Móvil Android

Agricol es una plataforma móvil desarrollada de forma nativa para Android. Está orientada al sector agrícola, facilitando la publicación de servicios/productos, la gestión de usuarios, una red de interacción mediante chat en tiempo real/mensajería interna y un sistema de notificaciones integradas.

Características Principales

Autenticación y Registro de Usuarios:

Flujo interactivo de inicio de sesión y registro en dos pasos (Login, Register, RegisterPart2).

Perfil de usuario personalizable (PantallaPerfil).

Publicaciones y Servicios:

Creación y gestión de publicaciones agrícolas (PantallaCrearPublicacion).

Exploración y visualización detallada de publicaciones (PantallaInicio, PantallaDetalle).

Búsqueda avanzada de contenidos (PantallaBuscar).

Mensajería Interna (Chat):

Lista de conversaciones activas (PantallaMensajes).

Interfaz de chat individual (PantallaChat).

Notificaciones:

Panel integrado para recepción de avisos e interacciones (PanelNotificaciones).

Cámara y Captura de Documentos:

Soporte para escaneo o captura de documentos dentro de la aplicación (CamaraDocumento).

🛠️ Arquitectura y Tecnologías

El proyecto sigue una arquitectura MVVM (Model-View-ViewModel) basada en los componentes de Android Jetpack, promoviendo una separación clara de responsabilidades:

Lenguaje: Kotlin

UI (Interfaz de Usuario): Jetpack Compose con Material Design 3.

Persistencia de Datos Local: Room Database (AppDataBase, UsuarioDao, PublicacionesDao, ChatDao).

Gestión de Estado: Android Architecture Components (ViewModel, Kotlin Coroutines & Flow).

Gestión de Dependencias: Gradle Version Catalogs (libs.versions.toml).

Estructura del Proyecto

cl.duoc.agricol/
├── model/                     # Modelos de datos (Usuario, Publicacion, Mensaje, Estado, etc.)
├── repository/                # Capa de datos y DAOs (Room DB Repositories)
├── ui/
│   ├── theme/                 # Componentes UI (Pantallas, Colores, Tipografías, Tema)
│   └── ...                    # Vistas principales (Inicio, Chat, Perfil, Buscar, etc.)
└── viewmodel/                 # Lógica de negocio (UsuarioViewModel, PublicacionViewModel, etc.)


Requisitos Previos

Android Studio: Ladybug / Iguana o superior.

JDK: Java 17 o superior.

SDK Mínimo: Android 8.0 (API Nivel 26) o superior.

Gradle: Version wrapper configurado en el proyecto.

Instalación y Configuración

Clonar el repositorio:

git clone https://github.com/tu-usuario/proyectoappmovil-Produccion.git
cd proyectoappmovil-Produccion


Abrir en Android Studio:

Abre Android Studio.

Selecciona Open an Existing Project y elige la carpeta raíz del proyecto.

Sincronizar Gradle:

Deja que Gradle descargue todas las dependencias requeridas automáticas (Sync Project with Gradle Files).

Ejecutar la aplicación:

Selecciona un emulador de Android (API 26+) o conecta un dispositivo físico mediante depuración USB.

Haz clic en Run 'app' (Shift + F10).
