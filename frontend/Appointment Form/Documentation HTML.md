
**Archivo:** `index.html`  
**Propósito:** **Formulario web para programar citas** en el proyecto **Clínica**.  

---

## 1. Introducción a HTML

**HTML** (*HyperText Markup Language*) es el **lenguaje estándar** utilizado para crear la **estructura y contenido** de las páginas web. Define la organización de los elementos (texto, formularios, imágenes, enlaces, etc.) mediante **etiquetas** que el navegador interpreta para mostrar la información correctamente.

En este documento se analiza un archivo HTML que define una **página destinada a registrar citas médicas**, incluyendo campos básicos de información personal y detalles de la cita.

---

## 2. Estructura general del documento

El código HTML está compuesto por **tres secciones principales**:

1. **Declaración del tipo de documento (`<!DOCTYPE html>`)**  
   Indica al navegador que el archivo utiliza la **versión HTML5**, la más reciente y estándar.

2. **Elemento `<html lang="es-MX">`**  
   Representa el **elemento raíz del documento**.  
   - El atributo `lang="es-MX"` define el **idioma principal** (español de México), lo cual mejora la **accesibilidad y el SEO**.

3. **Estructura interna:**
   - **`<head>`:** Contiene **metadatos**, el **título** y **enlaces** a recursos externos.  
   - **`<body>`:** Contiene el **contenido visible** para el usuario, en este caso, un **formulario para agendar citas**.

---

## 3. Sección `<head>`

Contiene la información necesaria para la **visualización y funcionamiento** del documento:

- **`<meta charset="UTF-8">`**: Define la **codificación de caracteres** para admitir letras acentuadas y caracteres especiales.  
- **`<meta name="viewport" content="width=device-width, initial-scale=1.0">`**: Permite que la página sea **adaptable a diferentes dispositivos** (*diseño responsivo*).  
- **`<title>Programar Cita - Clínica Salud</title>`**: Define el **título de la pestaña del navegador**.  
- **`<link rel="stylesheet" href="style.css">`**: Vincula la **hoja de estilos externa**, que controla la **apariencia visual** del formulario.

---

## 4. Sección `<body>`

El cuerpo contiene el **contenido principal de la página**.  
En este caso, todo el contenido está organizado dentro de un contenedor denominado **`overlay`**, que probablemente aplica un **efecto visual de fondo o transparencia** mediante CSS.

### 4.1. Estructura de contenedores

- **`<div class="overlay">`**: Contenedor principal que engloba todo el contenido visible.  
- **`<div class="form-container">`**: Agrupa y centra el formulario en pantalla.  
  - Este diseño ayuda a **controlar la posición, márgenes y dimensiones** mediante CSS.

### 4.2. Encabezado principal

- **`<h1>Programar Cita</h1>`**  
  Define el **título principal del formulario**, indicando claramente su propósito al usuario.

---

## 5. Formulario `<form class="appointment-form">`

El formulario recopila la **información necesaria para programar una cita médica**.  
Cada conjunto de campo y etiqueta está dentro de un **`<div class="form-group">`**, lo que mejora la **organización visual y semántica** del formulario.

### 5.1. Campos de entrada

1. **Nombres**  
   - **`<label for="nombre">Nombres</label>`**: Describe el campo y mejora la **accesibilidad**.  
   - **`<input type="text" id="nombre" placeholder="Ingrese sus nombres" required>`**: Campo **obligatorio** para ingresar el nombre del paciente.

2. **Apellidos**  
   - Campo de texto **obligatorio** para los apellidos.  
   - El atributo **`required`** garantiza que el campo **no se deje vacío**.

3. **Teléfono**  
   - **`<input type="tel" id="telefono" placeholder="Ej: 999-123-4567" required>`**  
   - El tipo **`tel`** sugiere el uso de **teclado numérico** en dispositivos móviles.

4. **Correo electrónico**  
   - **`<input type="email" id="correo" placeholder="Ingrese su correo electrónico" required>`**  
   - El tipo **`email`** realiza una **validación automática del formato** del correo.

5. **Fecha**  
   - **`<input type="date" id="fecha" required>`**  
   - Permite **seleccionar una fecha** desde un calendario emergente.

6. **Hora**  
   - **`<input type="time" id="hora" required>`**  
   - Permite **elegir una hora específica** para la cita.

7. **Motivo**  
   - Campo de texto para **describir el motivo de la cita médica**.  
   - Incluye un **`placeholder`** que orienta al usuario.

8. **Comentarios adicionales**  
   - **`<textarea id="comentarios" class="input-text" rows="3" placeholder="Ingrese algún detalle importante..."></textarea>`**  
   - Campo de texto **no obligatorio**, útil para **detalles adicionales** o aclaraciones del paciente.

---

## 6. Botones de acción

Ubicados dentro de **`<div class="form-buttons">`**, se incluyen dos botones con funciones distintas:

- **`<button type="submit" class="btn-primary">Enviar</button>`**  
  Envía la información al proceso definido (por ejemplo, una base de datos o correo).  
  La clase **`btn-primary`** indica el **botón principal** y suele tener un **color más destacado**.

- **`<button type="reset" class="btn-secondary">Cancelar</button>`**  
  **Limpia todos los campos del formulario**, devolviéndolos a su estado inicial.  
  La clase **`btn-secondary`** se usa generalmente para **acciones secundarias o menos relevantes**.

---

## 7. Funcionamiento esperado

El formulario debe permitir al usuario:
- **Ingresar datos personales y detalles de la cita.**  
- **Enviar la información** mediante el botón **Enviar**.  
- **Cancelar o reiniciar** los campos con el botón **Cancelar**.

El procesamiento de datos dependerá de una futura **integración con JavaScript o un lenguaje del lado del servidor**, lo cual permitirá almacenar o enviar la información capturada.

---

## 8. Conclusión

Este código HTML define una **página funcional, clara y accesible** para la programación de citas médicas.  
Cumple con las **normas de estructura semántica**, **validación básica**, y **buenas prácticas de diseño web**.  
Para su funcionamiento completo, debe complementarse con **hojas de estilo (CSS)** y **scripts de validación o envío (JavaScript o servidor)**.

