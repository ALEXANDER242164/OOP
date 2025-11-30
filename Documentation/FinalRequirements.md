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
