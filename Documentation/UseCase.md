# Especificación de Casos de Uso
Aquí se detallan los flujos que el sistema soporta.

## CU-01: Agendar Nueva Cita
**Actor:** Secretaria.

**Descripción:** Permite registrar una cita en el calendario validando disponibilidad.

**Precondiciones:** El backend debe estar activo.

**Flujo Básico:**
1. La secretaria selecciona "Agendar Cita".
2. Selecciona el "Tipo de Sesión".
3. Si es Evaluación Inicial: Introduce manualmente Nombre, Apellido y Teléfono.
4. Si es Cita de Terapia: Selecciona un paciente existente de la lista y sube comprobante de pago (validado en front).
5. Selecciona Terapeuta, Sala, Fecha y Hora.
6. El sistema valida reglas de negocio (Horario 9-16h, No fines de semana, Sin traslapes).
7. El sistema guarda la cita, genera/asigna folio y devuelve confirmación.

**Flujos Alternos:**
- **(Conflicto):** Si la sala o terapeuta están ocupados, el sistema muestra un error y no guarda la cita.
- **(Fin de semana):** Si la fecha es sábado/domingo, el sistema rechaza la operación.

## CU-02: Cancelar Cita
**Actor:** Secretaria.

**Descripción:** Libera un espacio en la agenda marcando una cita como cancelada sin borrar el registro histórico.

**Flujo Básico:**
1. La secretaria hace clic en una cita existente en el calendario.
2. Se abre el modal de "Detalles".
3. La secretaria presiona el botón "Cancelar Cita".
4. El sistema pide confirmación.
5. Al confirmar, el sistema actualiza el estado a CANCELADO.
6. El espacio queda libre para nuevas citas.

## CU-03: Consultar Agenda
**Actor:** Secretaria.

**Descripción:** Visualizar la carga de trabajo y disponibilidad.

**Flujo Básico:**
1. El sistema muestra por defecto la vista "Semanal".
2. La secretaria puede alternar a vista "Diaria".
3. La secretaria navega entre semanas o días anteriores/futuros.
4. El sistema renderiza las tarjetas de colores según el estado/tipo de cita.