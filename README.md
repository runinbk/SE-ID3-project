# Proyecto Final SE - Algoritmo ID3 en Java

Implementación del Algoritmo ID3 en Java

## 📋 Descripción

> Este proyecto implementa el algoritmo **ID3 (Iterative Dichotomiser 3)** para la construcción de árboles de decisión. Además, incluye una interfaz gráfica desarrollada en **Java Swing** que permite cargar, procesar y visualizar datos tabulares, mostrando tanto los cálculos intermedios (entropía y ganancia de información) como el árbol de decisión resultante.

## 📁 Estructura de Directorios

```
src/
├── gui/
│   ├── PanelTabla.java
│   └── VentanaPrincipal.java
├── model/
│   ├── Nodo.java
│   └── TablaID3.java
├── util/
│   ├── CargadorDatos.java
│   └── MathUtils.java
└── Main.java
```

---

## 🚀 Características Principales

1. **Cálculos Matemáticos:**
   - Entropía global del conjunto de datos.
   - Entropía por atributo.
   - Ganancia de información para seleccionar los mejores atributos.

2. **Interfaz Gráfica Intuitiva:**
   - Carga de datos desde archivos CSV.
   - Visualización de datos en una tabla interactiva.
   - Selección de la columna objetivo.
   - Generación y visualización gráfica del árbol de decisión.

3. **Validación de Datos:**
   - Verificación de la estructura y consistencia de los datos cargados.
   - Asegura que la columna objetivo sea categórica.

4. **Visualización Detallada:**
   - Estadísticas básicas de los datos cargados.
   - Destacado visual de la columna objetivo seleccionada.

---

## 📋 Requisitos del Sistema

- **Java Development Kit (JDK):** Version 23 o superior.
- **Apache NetBeans:** Version 24 o superior (opcional, pero recomendado para desarrollo).

---

## 🔧 Instalación

1. Clona este repositorio en tu máquina local:

   ```bash
   git clone https://github.com/usuario/proyecto-id3-java.git
   cd proyecto-id3-java
   ```

2. Abre el proyecto en tu IDE favorito (NetBeans recomendado).

3. Compila y ejecuta el proyecto desde la clase principal `ID3Main`.

---

## 🔑 Uso

### Carga de Datos
1. Abre la aplicación.
2. Haz clic en el botón "Cargar CSV" y selecciona un archivo de datos.
   - El archivo debe tener formato CSV, con la primera fila como encabezados de columna.

### Selección de Columna Objetivo
1. Usa el desplegable para seleccionar la columna que actuará como objetivo para el árbol de decisión.
2. La columna seleccionada se resaltará en la tabla.

### Generación del Árbol de Decisión
1. Haz clic en "Generar Árbol".
2. Visualiza el árbol generado en el panel correspondiente.
3. Explora los cálculos de entropía y ganancia realizados para cada nodo.

---

## 📝 Formato del Archivo CSV

Ejemplo de archivo compatible:

```csv
Cielo,Temperatura,Humedad,Viento,Jugar
Soleado,Alta,Alta,No,No
Soleado,Alta,Alta,Sí,No
Nublado,Alta,Alta,No,Sí
Lluvia,Media,Alta,No,Sí
Lluvia,Baja,Normal,No,Sí
```

---

## 💻 Arquitectura del Proyecto

El proyecto está dividido en varios paquetes:

- **id3.main:** Contiene la clase principal para ejecutar la aplicación.
- **id3.model:** Maneja las estructuras de datos como nodos del árbol y la tabla de datos.
- **id3.util:** Incluye las clases utilitarias para cálculos matemáticos y carga de archivos.
- **id3.gui:** Implementa la interfaz gráfica de usuario.

---

## 🛠 Tecnologías Utilizadas

- **Java Swing:** Para la interfaz gráfica.
- **JTable:** Para visualizar datos tabulares.
- **Gráficos 2D:** Para representar el árbol de decisión.

---

## 🔒 Limitaciones Conocidas

1. Solo admite columnas categóricas como columna objetivo.
2. La visualización del árbol puede ser compleja para conjuntos de datos muy grandes.
3. Actualmente, no admite atributos con valores nulos o faltantes.

---

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Por favor, sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una rama con tu función o corrección:
   ```bash
   git checkout -b mi-nueva-funcionalidad
   ```
3. Haz commit de tus cambios:
   ```bash
   git commit -m "Agrega nueva funcionalidad"
   ```
4. Envía un pull request.

---

## 📄 Licencia

Este proyecto está licenciado bajo la [MIT License](LICENSE). Puedes usarlo, modificarlo y distribuirlo libremente siempre que incluyas los créditos originales.

## ✍️ Autor

- **Kevin B. Gomoez R.** - *Software Developer* - [runinbk💻🔥](https://github.com/runinbk)

## 🎁 Agradecimientos

- Al esplendido equipo con el que trabajo ❤
---

## Capturas de Pantalla

### 1. Carga de Datos
![Carga de datos](docs/screenshots/carga_datos.png)

### 2. Selección de Columna Objetivo
![Selección de columna](docs/screenshots/seleccion_columna.png)

### 3. Visualización del Árbol de Decisión
![Árbol de decisión](docs/screenshots/arbol_decision.png)
