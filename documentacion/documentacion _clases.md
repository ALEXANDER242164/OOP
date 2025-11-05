# 📚 Documentacionde las clases 

## 💬 descripcion 
tenemos 7 clases las cuales las 7 estan en su forma prmiliminar, tovia no estan implementadas al 100 % en la APi y algunas necesitan un cambio, ya que no tenfran implementacion como tal, solo seran un apoyo para las otras clases, les mostrare un poco como se tiene pensado la implementacion o el uso que tendra las clases 

## 👁️ MODELOS (Model) 
### BaseModel 
• Atributos: id, createdAt, updatedAt. 
• Métodos: validate(), save(), delete(). 
• Responsabilidad: lógica común de persistencia/validación; no maneja UI. 
### PatientModel 
• Datos: nombre, apellidos, fecha de nacimiento, contacto, referencia a 
Record. 
• Métodos clave: getAppointments() (obtiene citas asociadas), 
addRecordEntry(entry). 
• Responsabilidad: representar al paciente y operaciones de negocio simples 
(añadir entrada de expediente). 
### TherapistModel 
• Datos: nombre, especialidades, Schedule (estructura con disponibilidad). 
• Métodos: isAvailable(start,end), getAppointments(). 
• Responsabilidad: proveer consultas sobre la disponibilidad y sus datos. 
### RoomModel 
• Datos: nombre, capacidad. 
• Métodos: isAvailable(start,end). 
• Responsabilidad: validar disponibilidad física de sala. 
### AppointmentModel 
• Datos: referencias a patientId, therapistId, roomId, startTime, endTime, 
status. 
• Métodos: schedule(), reschedule(), cancel(), conflictsWith(...). 
• Responsabilidad: lógica de negocio de una cita (estado, reglas de conflicto). 
### RecordModel 
• Datos: lista de RecordEntry (nota clínica, fecha, autor). 
• Métodos: addEntry, getEntries. 
• Responsabilidad: almacenar historial clínico del paciente. 
### PaymentModel 
• Datos: monto, estado, referencia a cita. 
• Métodos: processPayment(). 
• Responsabilidad: registrar y procesar pagos asociados a citas. 
### UserModel 
• Datos: username, passwordHash, role. 
• Métodos: authenticate(), authorize(). 
• Responsabilidad: seguridad/autenticación y comprobación de roles. 
### ReportModel 
• Métodos: generateAppointmentsReport, generateFinancialReport. 
• Responsabilidad: encapsular la creación de reportes a partir de datos. 

