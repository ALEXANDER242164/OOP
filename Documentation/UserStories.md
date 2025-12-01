# Historias de Usuario (Backlog Implementado)

## HU-01: Registro de Paciente Nuevo (Evaluación Inicial)
Como secretaria, quiero registrar los datos básicos de un paciente nuevo al agendar su primera cita, para no tener que ir a otra pantalla de "registro de pacientes" antes de agendar.

**Criterios de Aceptación:**
- El formulario permite ingresar Nombre, Apellido, Teléfono y Email.
- Al guardar, el sistema genera automáticamente un Folio único de 6 dígitos.
- El paciente se guarda en base de datos automáticamente al crear la cita.

## HU-02: Agendar Cita de Terapia (Paciente Recurrente)
Como secretaria, quiero seleccionar a un paciente de una lista desplegable, para agendarle una cita de seguimiento rápidamente sin reescribir sus datos.

**Criterios de Aceptación:**
- El sistema muestra un combo-box con todos los pacientes registrados.
- Al seleccionar al paciente, el campo "Folio" se rellena automáticamente (solo lectura).
- El sistema valida que el paciente no tenga ya otra cita ese mismo día.

## HU-03: Validación de Disponibilidad (Anti-traslape)
Como coordinador, quiero que el sistema me impida guardar una cita si la sala o el terapeuta ya están ocupados, para evitar tener a dos pacientes en el mismo consultorio a la misma hora.

**Criterios de Aceptación:**
- Si intento agendar en la Sala 1 a las 10:00 AM, y ya existe una cita activa en esa sala/hora, el sistema debe mostrar un error.
- Si intento agendar con el Dr. Juan a las 10:00 AM, y él ya tiene paciente, el sistema debe mostrar un error.
- Las citas con estado "CANCELADO" no deben contar como ocupación (deben permitir sobreescribir).

## HU-04: Visualización de Estado de Citas
Como secretaria, quiero distinguir visualmente en el calendario qué citas están activas o canceladas, para tener un panorama rápido de la agenda.

**Criterios de Aceptación:**
- Las citas canceladas deben verse en color rojo o con un indicador claro visual.
- Las citas de terapia deben tener un color distinto (azul) para diferenciarlas.
- Al dar clic en la cita, debo poder ver el detalle completo (sala, duración, folio).

## HU-05: Restricción de Horario Laboral
Como administrador de la clínica, quiero que el sistema bloquee intentos de cita fuera del horario de atención y fines de semana, para asegurar que se respeten los horarios del personal.

**Criterios de Aceptación:**
- No se permite guardar citas en Sábado o Domingo.
- No se permite guardar citas antes de las 09:00 AM.
- No se permite guardar citas que inicien después de las 16:00 PM (para cerrar a las 17:00).