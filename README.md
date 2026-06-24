# MarketLens 📊

**MarketLens** es una aplicación nativa para Android diseñada para el monitoreo y análisis en tiempo real de activos financieros (tanto acciones tradicionales como criptomonedas). La app integra herramientas avanzadas de visualización, guardado de favoritos sincronizado y generación de resúmenes informativos y de sentimiento de mercado impulsados por Inteligencia Artificial (Google Gemini).

---

## 🚀 Características Principales

* **Monitoreo en Tiempo Real:** Visualización de precios y porcentajes de variación diaria de criptomonedas y acciones.
* **Búsqueda y Filtros:** Acceso rápido a detalles de activos específicos por ticker (símbolo).
* **Gestión de Favoritos (Watchlist):** Permite marcar activos como favoritos, con persistencia y sincronización local/remota.
* **Sincronización Híbrida de Datos:**
  * **Persistencia Local:** Uso de base de datos SQLite mediante **Room** para caching sin conexión a internet.
  * **Sincronización en la Nube:** Uso de **Firebase Firestore** para sincronizar la lista de favoritos de manera multi-dispositivo y guardar los perfiles de usuario.
* **Resúmenes Inteligentes con IA (Gemini):** Análisis automático del sentimiento del mercado para cada activo a partir de noticias de última hora, indicando también un puntaje de confianza estimado.
* **Indicadores Macroeconómicos:** Sección dedicada a consultar la salud económica (PBI, Inflación, Tasa de Interés y Desempleo) mediante indicadores de FRED.
* **Seguridad:** Autenticación de usuarios mediante **Firebase Auth** (compatible con cuentas Google).

---

## 🛠️ Arquitectura y Tecnologías Utilizadas

La aplicación se construyó siguiendo las mejores prácticas recomendadas por Google para el desarrollo de Android moderno:

* **Lenguaje:** Kotlin 100%.
* **Interfaz de Usuario (UI):** **Jetpack Compose** (desarrollo de UI declarativo y reactivo).
* **Arquitectura:** **MVVM (Model-View-ViewModel)** complementada con principios de **Clean Architecture** (División clara entre capa de datos, negocio y presentación).
* **Inyección de Dependencias (DI):** **Dagger Hilt** (para un acoplamiento débil y facilidad de testing).
* **Acceso a Datos y Red:**
  * **Retrofit + Gson:** Consumo de APIs REST (CoinGecko, Finnhub, Tiingo, Stockdata, FRED).
  * **Kotlin Coroutines & Flow:** Programación asíncrona e ininterrumpida de flujos de datos.
  * **Room Database:** Base de datos relacional local optimizada.
* **Inteligencia Artificial:** **Google Generative AI SDK** utilizando el modelo `gemini-1.5-flash` (a través del alias de API `gemini-flash-latest`).
* **Backend como Servicio (BaaS):** Firebase Auth y Cloud Firestore.

---

## 📦 Estructura del Proyecto (Capa de Código)

La estructura está organizada modularmente para facilitar la escalabilidad:
* `data/`: Contiene los datasources (APIs, Firebase), la configuración de la base de datos (Room) y las implementaciones de los repositorios.
* `domain/`: Contiene los modelos de negocio puros, los mappers y las interfaces de los repositorios.
* `components/` o `ui/`: Contiene los composables de Jetpack Compose de las pantallas, la gestión del estado (UI State) y los ViewModels.
* `di/`: Módulos de Dagger Hilt para inyectar dependencias de red, base de datos y repositorios.

---

## 🧪 Pruebas Unitarias (Testing)

El proyecto cuenta con un set de pruebas unitarias locales para verificar el comportamiento de los ViewModels y Mappers de forma aislada, asegurando la consistencia lógica de la app.

Para correr las pruebas unitarias y generar un informe de calidad en HTML, ejecute el siguiente comando en la consola del proyecto:

```bash
./gradlew testDebugUnitTest
```

Una vez ejecutadas, puede abrir el reporte generado en su navegador en la siguiente ruta:
`app/build/reports/tests/testDebugUnitTest/index.html`

---

## 🔧 Configuración para Desarrollo Local

El proyecto lee sus claves de API de manera segura desde el archivo `local.properties`. 

Para compilar el proyecto localmente:
1. Cree o edite el archivo `local.properties` en la raíz de su proyecto.
2. Agregue las claves para los servicios externos correspondientes (consulte el archivo `INSTRUCCIONES.txt` incluido en la entrega para obtener más detalles y las claves de prueba asignadas).
