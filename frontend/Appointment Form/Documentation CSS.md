**Archivo:** `style.css`  
**Propósito:** Definir la **apariencia visual** y el **comportamiento estético** del formulario de programación de citas en la página principal del proyecto *Clínica*.  

---

## 1. Introducción al CSS

**CSS (Cascading Style Sheets)** es el lenguaje que controla la **presentación visual** de los elementos definidos en un documento HTML.  
Permite aplicar **colores, tipografías, márgenes, tamaños, bordes, posiciones y efectos interactivos**, garantizando una experiencia visual más atractiva y coherente en distintos dispositivos.

Este archivo define el **diseño flotante y responsivo** de un formulario médico, utilizando **buenas prácticas de legibilidad, accesibilidad y adaptación** a pantallas pequeñas.

---

## 2. Estilos generales (`body`)

El selector **`body`** establece los estilos globales de la página:

- **Fuente:** `'Segoe UI', Tahoma, Geneva, Verdana, sans-serif` — una tipografía moderna y legible.  
- **Fondo:** imagen de un **pasillo de clínica**, centrada y ajustada completamente con `background: url(...) center/cover no-repeat;`.  
- **Tamaño:** `height: 100vh` asegura que el contenido ocupe toda la altura visible de la ventana.  
- **Diseño flexible:** `display: flex; align-items: center; justify-content: center;` centra el formulario tanto vertical como horizontalmente.  
- **Margen:** `margin: 0;` elimina espacios por defecto del navegador.

> **Nota:** el comentario dentro del bloque (`/*linear-gradient(...)*/`) muestra una posible futura implementación de un **fondo degradado alternativo**, actualmente deshabilitado.

---

## 3. Contenedor principal del formulario (`.form-container`)

El bloque **`.form-container`** define la estructura visual del formulario:

- **Fondo blanco (`#fff`)** para contrastar con el fondo exterior.  
- **Dimensiones:** `width: 420px; max-height: 85vh;` — asegura que el formulario no exceda la pantalla y pueda desplazarse.  
- **Bordes y sombras:** `border-radius: 16px; box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);` — da efecto de **ventana flotante** con bordes suaves.  
- **Distribución interna:** `display: flex; flex-direction: column;` — organiza los elementos en una sola columna.  
- **Desplazamiento:** `overflow-y: auto;` habilita el scroll vertical si el contenido supera el alto máximo.  
- **Personalización del scroll:** se definen colores y estilos mediante `scrollbar-width` y pseudoelementos `::-webkit-scrollbar`.

---

## 4. Estilo del scroll personalizado

Estos selectores controlan la apariencia del **scrollbar vertical** del formulario:

- **`::-webkit-scrollbar`** → define el ancho (8px).  
- **`::-webkit-scrollbar-thumb`** → color base `#b0c4de` y bordes redondeados.  
- **`::-webkit-scrollbar-thumb:hover`** → color más oscuro `#7da1c4` al pasar el cursor, mejorando la interacción visual.

---

## 5. Encabezado del formulario (`.form-container h1`)

- **Centrado del texto:** `text-align: center;`  
- **Color:** `#2c3e50` (gris oscuro, profesional).  
- **Tamaño de fuente:** `1.8rem`, suficiente para destacar sin saturar.  
- **Espaciado inferior:** `margin-bottom: 25px;` separa el título del contenido.

Este encabezado sirve como **identificador visual principal** del formulario.

---

## 6. Formulario y grupos de campos (`.appointment-form` y `.form-group`)

### `.appointment-form`
- **Diseño flexible:** `display: flex; flex-direction: column;`  
- **Separación entre campos:** `gap: 18px;` garantiza una distancia uniforme.

### `.form-group`
- Agrupa cada campo (`label` + `input`/`textarea`) en una columna vertical.  
- Facilita la **alineación limpia** y mejora la **legibilidad visual**.

---

## 7. Etiquetas de texto (`label`)

- **Peso de fuente:** `font-weight: 600;` — resalta los títulos de campo.  
- **Color:** `#2c3e50` — coherente con el encabezado.  
- **Margen inferior:** `6px;` — separa visualmente la etiqueta del campo de entrada.

Las etiquetas ayudan a identificar correctamente el propósito de cada campo.

---

## 8. Campos de entrada (`input`, `textarea`, `.input-text`)

Estos estilos se aplican a todos los campos del formulario:

- **Relleno interno:** `padding: 10px 12px;` — mejora la comodidad de escritura.  
- **Bordes:** `border: 1px solid #ccc; border-radius: 8px;` — apariencia limpia y moderna.  
- **Tipografía:** tamaño `0.95rem`, color de texto `#333`.  
- **Transiciones suaves:** `transition: border-color 0.3s ease, box-shadow 0.3s ease;` mejora la experiencia al interactuar.  
- **Ancho completo:** `width: 100%;` garantiza la alineación con el contenedor.

### `.input-text`
Define el estilo específico para el campo de comentarios:
- **Fuente coherente con el resto del formulario.**  
- **Color:** `#333` para buen contraste.  
- **Sin redimensionamiento:** `resize: none;` mantiene el tamaño fijo.

### Estados de enfoque (`:focus`)
Cuando el usuario selecciona un campo:
- **Color de borde:** cambia a `#2980b9`.  
- **Sombra azul tenue:** `box-shadow: 0 0 5px rgba(41, 128, 185, 0.3);` para dar efecto de enfoque activo.

### Área de texto (`textarea`)
- Se define una **altura fija (80px)** y se desactiva el redimensionamiento manual.

---

## 9. Botones de acción (`.form-buttons`, `.btn-primary`, `.btn-secondary`)

### `.form-buttons`
- **Distribución:** `display: flex; justify-content: space-between; gap: 12px;`  
- **Margen superior:** `10px;` separa los botones del último campo.

### Estilos generales de botones
- **Relleno:** `10px;`  
- **Bordes redondeados:** `border-radius: 8px;`  
- **Color de texto:** blanco (`#fff`).  
- **Peso:** `font-weight: 600;` para resaltar la acción.  
- **Transición suave:** `transition: background 0.3s ease;` mejora la interacción visual.  
- **Cursor:** `pointer;` cambia a mano al pasar el mouse.

### `.btn-primary`
- **Color principal:** `#3498db` (azul).  
- **Hover:** se oscurece a `#2980b9` — mejora la percepción de clic.

### `.btn-secondary`
- **Color base:** `#e74c3c` (rojo, para cancelar o limpiar).  
- **Hover:** tono más oscuro `#c0392b`.

> **Importante:** se usa contraste entre botones para **distinguir acciones principales y secundarias**.

---

## 10. Adaptación a dispositivos móviles (`@media (max-width: 480px)`)

Define ajustes para **pantallas pequeñas** (teléfonos):

- El ancho del formulario se reduce a **90%** del viewport.  
- Se disminuyen los **rellenos (`padding: 25px`)** para aprovechar mejor el espacio.  
- Garantiza que el formulario siga siendo **usable y legible** en pantallas reducidas.

---

## 11. Fondo de superposición (`.overlay`)

Este contenedor crea el **efecto de fondo bloqueado o difuminado** cuando se muestra el formulario:

- **Posición fija (`fixed`)** — cubre toda la ventana.  
- **Tamaño completo:** `width: 100%; height: 100%;`  
- **Color semitransparente:** `background-color: rgba(0, 0, 0, 0.2);` — genera una sombra tenue sobre el fondo.  
- **Centrado del contenido:** `display: flex; align-items: center; justify-content: center;`  
- **Z-index elevado (`1000`)** para mantenerse por encima del resto del contenido.  
- **Desplazamiento habilitado:** `overflow: auto;`

### Comentario adicional
Esta clase puede usarse en una **implementación futura** donde el fondo se bloquea completamente al mostrar el formulario flotante, **manteniendo solo la ventana activa** para interactuar.

---

## 12. Conclusión

Este archivo CSS define una **interfaz moderna, limpia y profesional** para el formulario de citas médicas.  
Utiliza técnicas actuales de **flexbox, responsividad, sombras suaves y personalización de scrollbars**, asegurando una **experiencia visual intuitiva y adaptable**.  
La estructura está preparada para posibles ampliaciones, como la **activación del modo de superposición completa (`.overlay`)**, que bloquearía la interacción con el fondo al abrir el formulario flotante.