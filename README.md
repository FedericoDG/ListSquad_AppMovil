# 📱 List Squad — Aplicación Android

**List Squad** es una aplicación móvil desarrollada en **Java** para Android, en el marco de la materia _Laboratorio de Programación III_ de la Universidad de La Punta.  
El proyecto sigue la arquitectura **MVVM** y utiliza **Android Studio**, con soporte para sincronización de datos y autenticación y notificaciones push mediante Firebase.

---

## 🧰 Tecnologías principales

- 🟡 **Lenguaje:** Java
- 🧩 **Arquitectura:** MVVM (Model–View–ViewModel)
- ⚙️ **Entorno:** Android Studio Narwhal o superior
- 🔧 **Gestor de dependencias:** Gradle Wrapper (incluido en el repositorio)
- ☁️ **Servicios externos:** Firebase Authentication y Firestore

---

## 🚀 Instalación y ejecución del proyecto

Seguí estos pasos para clonar y ejecutar **List Squad** correctamente 👇

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/FedericoDG/ListSquad_AppMovil.git
cd ListSquad_AppMovil
```

### 2️⃣ Abrir el proyecto

- Abrí **Android Studio**
- Seleccioná **File → Open...**
- Buscá la carpeta donde clonaste el proyecto y abrila
- Esperá a que Gradle sincronice automáticamente (puede tardar unos minutos la primera vez)

> ⚠️ Si Android Studio no sincroniza solo, hacé clic en:
> `File → Sync Project with Gradle Files`

---

## 🧱 Estructura del proyecto

```bash
ListSquad_AppMovil/
├── app/                     # Código fuente principal
│   ├── java/...             # Clases y lógica de la app (MVVM)
│   ├── res/                 # Recursos: layouts, strings, imágenes, etc.
│   └── AndroidManifest.xml
├── gradle/wrapper/          # Configuración del Gradle Wrapper
├── build.gradle             # Configuración de dependencias del proyecto
├── settings.gradle          # Módulos incluidos
├── gradlew                  # Script Gradle (Linux/Mac)
├── gradlew.bat              # Script Gradle (Windows)
└── README.md
```

---

## ⚙️ Configuración local necesaria

### 🌐 Archivo `.env`

Debés editar el archivo `.env` en la raíz del proyecto:

```
API_BASE_URL=https://tu-api.com
```

> Este archivo define la URL base del backend al que se conecta la aplicación.

### 🔐 Archivo `google-services.json` (Firebase)

Colocar el archivo `google-services.json` dentro de:

```
app/google-services.json
```

> Este archivo contiene credenciales del proyecto de Firebase, por lo tanto **no se incluye en GitHub**.  
> Cada desarrollador debe obtener su propia copia desde [Firebase Console](https://console.firebase.google.com).

---

## 🧩 Compilación del proyecto

Para compilar y ejecutar desde la terminal (opcional):

```bash
# Dar permisos al wrapper si es necesario
chmod +x gradlew

# Compilar el proyecto
./gradlew assembleDebug

# Ejecutar en dispositivo conectado o emulador
./gradlew installDebug
```

También podés hacerlo directamente desde Android Studio usando el botón ▶️ **"Run"**.

---
