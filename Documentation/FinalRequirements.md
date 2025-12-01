# Requisitos Funcionales (RF)
Los requisitos funcionales describen las operaciones y comportamientos específicos que el sistema debe realizar.

## 1-1. RF - **Creación de cita**
El sistema debe permitir a los usuarios previamente autorizados, como secretarias y coordinadores generales, registrar una nueva cita en el sistema de programación.

### Criterios de aceptación
- **Formulario de creación:** Debe existir un formulario que recopile la siguiente información:

    - Tipo de sesión (Evaluación integral inicial o cita terapéutica)
    - Paciente
    - Terapeuta
    - Sala
    - Fecha de la cita (hora de inicio y fin en formato de 24 horas, día/mes/año)

- Generación de folio: Al crear la primera cita de un paciente, el sistema debe generar un folio único y asociarlo al paciente. Este folio permanecerá constante para el paciente.

- **Validación de reglas de negocio:** El sistema no debe permitir agendar una cita si se cumple alguno de estos puntos:
    - La fecha seleccionada es fin de semana (sábado o domingo).
    - El horario está fuera del horario laboral (09:00 - 17:30).
    - Existe conflicto de horario (traslape) para el paciente, terapeuta o sala seleccionada.

- **Confirmación de cita:** Al guardar exitosamente, el sistema debe mostrar un mensaje de confirmación y la cita debe aparecer en el calendario visual.


## 1-2. RF - **Visualización de cita**

El sistema debe proporcionar una interfaz adaptable e intuitiva para consultar las citas programadas.

### Criterios de aceptación

- **Vista:** Debe presentarse una vista tipo calendario (agenda).
- **Filtros de visualización:** El usuario debe poder filtrar la vista por:
  - **Día:** Muestra las citas de un solo día.
  - **Semana:** Muestra las citas de lunes a viernes de una semana.
- **Ver detalles:** Al hacer clic en una cita, el sistema debe mostrar una vista con todos sus detalles (paciente, terapeuta, sala, hora, duración, etc.).

## 1-3. RF - **Actualización de cita** Por definir


## 1-4. RF - **Eliminación de cita**
El sistema debe permitir a los usuarios autorizados (secretaria) eliminar una cita existente en el sistema.

**Criterios de aceptación**
- Estatus en expediente del paciente: La cita debe marcarse como "suspendida" en el expediente del paciente.
- Eliminación del sistema: La cita eliminada solo puede ser accesada para ver la información referente a la misma.
- Criterio de eliminación: El paciente debe haber solicitado la anulación de la cita por medio del sistema o con ayuda del personal autorizado (secretarias).

## 1-5. RF - **Recordatorio de cita** Por definir


## 2. Requisitos No Funcionales (RNF)
Los requisitos no funcionales describen la calidad, rendimiento y estándares de experiencia del usuario del sistema.

### 2-1. RNF - **Usabilidad**
El sistema debe ser fácil de aprender y usar para el personal administrativo.

**Criterios de aceptación**
- Un usuario nuevo debe poder programar y consultar una cita en menos de 15 minutos sin recibir instrucciones sobre cómo funciona el sistema.
- El usuario debe poder usar correctamente el sistema para realizar las siguientes acciones: ver una cita, crear una cita y reprogramar una cita.

### 2-2. RNF - **Retroalimentación del sistema**
El sistema debe proporcionar a los usuarios autorizados (secretarias y coordinadores) retroalimentación clara e inmediata.

**Criterios de aceptación**
- Todas las operaciones CRUD (Crear, Ver, Guardar, Reprogramar) deben mostrar un mensaje de éxito o error en menos de 1 segundo.
- Cada operación fundamental (creación, visualización, guardado y reprogramación) debe mostrar un mensaje de confirmación o error indicando la finalización de la acción, en el menor tiempo posible.
- Los mensajes de error del sistema para validar operaciones clave deben ser claros, concisos y específicos.

### 2-3. RNF - **Rendimiento**
La interfaz debe ser rápida y fluida.

**Criterios de aceptación**
- La visualización de la agenda del sistema en ambos modos (diario y semanal) debe cargarse en menos de 2 segundos.
- Abrir modales/formularios para crear o editar una cita debe ser instantáneo (< 500 ms).

### 2-4. RNF - **Diseño consistente y adaptable**
La interfaz debe ser consistente y funcionar en diferentes dispositivos.

**Criterios de aceptación**
- Todos los botones, formularios y colores deben seguir la misma guía de estilo en todo el módulo.
- La aplicación debe ser completamente funcional y visualmente correcta en resoluciones de escritorio (1920px) y tablet (768px), sin generar barras de desplazamiento horizontal.
