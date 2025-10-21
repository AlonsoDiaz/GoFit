# GoFit - Tu App de Seguimiento de Entrenamiento 💪

**GoFit** es una aplicación para Android diseñada para ayudar a los usuarios a registrar y visualizar su progreso en el gimnasio.  
Permite a los usuarios seguir rutinas de entrenamiento predefinidas, registrar sus series, pesos y repeticiones, y ver un historial completo de sus sesiones completadas.

---

## 🚀 Funcionalidades Principales

- **Inicio de Sesión:** Pantalla de autenticación de usuario.  
- **Home Screen:** Un panel central para navegar a las principales secciones de la app.  
- **Lista de Rutinas:** Explora las rutinas de entrenamiento disponibles.  
- **Detalle de Rutina:** Visualiza los ejercicios que componen una rutina específica.  
- **Sesión de Entrenamiento Interactiva:**  
  - Registra en tiempo real las series, el peso levantado y las repeticiones para cada ejercicio.  
  - Añade series dinámicamente.  
- **Historial de Progreso:**  
  - Guarda cada entrenamiento completado.  
  - Muestra una lista cronológica de todas las sesiones, con el entrenamiento más reciente primero.  
  - Detalla la fecha, el nombre de la rutina y los ejercicios completados en cada sesión.

---

## 🛠️ Tecnologías y Arquitectura

Este proyecto está construido siguiendo las mejores prácticas recomendadas por Google para el desarrollo de aplicaciones Android modernas.

### Tecnologías Utilizadas

- **Lenguaje:** Kotlin 100%  
- **UI Toolkit:** Jetpack Compose para una interfaz de usuario declarativa y moderna.  
- **Navegación:** Jetpack Navigation for Compose para gestionar los flujos entre pantallas.  
- **Gestión de Estado:** StateFlow y ViewModel para manejar el estado de la UI de forma reactiva y eficiente.  
- **Asincronía:** Corrutinas de Kotlin para manejar operaciones en segundo plano sin bloquear la interfaz.

### Arquitectura de la App

El proyecto sigue una arquitectura **MVVM (Model-View-ViewModel)** limpia, separando las responsabilidades de la siguiente manera:

- **UI Layer (Vistas):**  
  Compuesta por funciones `@Composable` (ej. `ProgresoScreen`, `WorkoutSessionScreen`) que observan los datos del ViewModel y reaccionan a sus cambios.  
- **ViewModel Layer:**  
  Clases como `ProgresoViewModel` y `WorkoutViewModel` que contienen la lógica de negocio, gestionan el estado de la UI y se comunican con el Repository.  
- **Data Layer (Repositorio):**  
  La clase `ProgresoRepository` actúa como la única fuente de verdad (*Single Source of Truth*).  
  Actualmente simula una base de datos en memoria, pero está diseñada para ser reemplazada fácilmente por una solución persistente como Room.

El flujo de datos es **unidireccional**, lo que hace que la aplicación sea más predecible y fácil de depurar:  
`Repository → ViewModel → UI`

---

## 🔧 Cómo Empezar

Para clonar y ejecutar este proyecto localmente, sigue estos pasos:

1. **Clona el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/GoFit.git
