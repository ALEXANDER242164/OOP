classDiagram
direction BT
class Appointment {
  - AppointmentStatus appointmentStatus
  - String comments
  - LocalDateTime endDateTime
  - Long id
  - Patient patient
  - String paymentProofPath
  - Room room
  - SessionType sessionType
  - LocalDateTime startDateTime
  - Therapist therapist
  + getAppointmentStatus() AppointmentStatus
  + getComments() String
  + getEndDateTime() LocalDateTime
  + getId() Long
  + getPatient() Patient
  + getPaymentProofPath() String
  + getRoom() Room
  + getSessionType() SessionType
  + getStartDateTime() LocalDateTime
  + getTherapist() Therapist
  + setAppointmentStatus(AppointmentStatus) void
  + setComments(String) void
  + setEndDateTime(LocalDateTime) void
  + setId(Long) void
  + setPatient(Patient) void
  + setPaymentProofPath(String) void
  + setRoom(Room) void
  + setSessionType(SessionType) void
  + setStartDateTime(LocalDateTime) void
  + setTherapist(Therapist) void
}
class AppointmentCalendarItem {
  - LocalDateTime end
  - Long id
  - String patientFolio
  - String patientNombre
  - String roomNombre
  - SessionType sessionType
  - LocalDateTime start
  - String status
  - String therapistNombre
  + getEnd() LocalDateTime
  + getId() Long
  + getPatientFolio() String
  + getPatientNombre() String
  + getRoomNombre() String
  + getSessionType() SessionType
  + getStart() LocalDateTime
  + getStatus() String
  + getTherapistNombre() String
}
class AppointmentController {
  - AppointmentService appointmentService
  + cancelAppointment(Long) ResponseEntity~AppointmentResponse~
  + create(AppointmentRequest) ResponseEntity~AppointmentResponse~
  + day(LocalDate) List~AppointmentCalendarItem~
  + getAll() ResponseEntity~List~AppointmentResponse~~
  + handleValidationExceptions(MethodArgumentNotValidException) Map~String, String~
  + info() String
  + updateStatus(Long, String) ResponseEntity~AppointmentResponse~
  + week(LocalDate) List~AppointmentCalendarItem~
}
class AppointmentRepository {
<<Interface>>
  + countActiveAppointmentsInTimeRange(LocalDateTime, LocalDateTime) long
  + countAppointmentsByPatientAndDate(Long, LocalDateTime, LocalDateTime) long
  + findByStartDateTimeBetween(LocalDateTime, LocalDateTime) List~Appointment~
  + findOverlappingAppointments(LocalDateTime, LocalDateTime, Long, Long) List~Appointment~
}
class AppointmentRequest {
  - Double amountMx
  - String comments
  - LocalDate date
  - Integer durationMinutes
  - String folio
  - String patientApellido
  - String patientEmail
  - Long patientId
  - String patientNombre
  - String patientTelefono
  - String paymentProof
  - Long roomId
  - SessionType sessionType
  - LocalTime startTime
  - Long therapistId
  + getAmountMx() Double
  + getComments() String
  + getDate() LocalDate
  + getDurationMinutes() Integer
  + getFolio() String
  + getPatientApellido() String
  + getPatientEmail() String
  + getPatientId() Long
  + getPatientNombre() String
  + getPatientTelefono() String
  + getPaymentProof() String
  + getRoomId() Long
  + getSessionType() SessionType
  + getStartTime() LocalTime
  + getTherapistId() Long
  + setAmountMx(Double) void
  + setComments(String) void
  + setDate(LocalDate) void
  + setDurationMinutes(Integer) void
  + setFolio(String) void
  + setPatientApellido(String) void
  + setPatientEmail(String) void
  + setPatientId(Long) void
  + setPatientNombre(String) void
  + setPatientTelefono(String) void
  + setPaymentProof(String) void
  + setRoomId(Long) void
  + setSessionType(SessionType) void
  + setStartTime(LocalTime) void
  + setTherapistId(Long) void
}
class AppointmentResponse {
  - Long appointmentId
  - String appointmentStatus
  - String message
  - String patientFolio
  + getAppointmentId() Long
  + getAppointmentStatus() String
  + getMessage() String
  + getPatientFolio() String
}
class AppointmentService {
  - AppointmentRepository appointmentRepo
  - PatientRepository patientRepo
  - RoomRepository roomRepo
  - TherapistRepository therapistRepo
  + create(AppointmentRequest) AppointmentResponse
  + findAll() List~AppointmentResponse~
  - generarFolioPaciente(Long) String
  + getDay(LocalDate) List~AppointmentCalendarItem~
  + getWeek(LocalDate) List~AppointmentCalendarItem~
  - toCalendarItem(Appointment) AppointmentCalendarItem
  - toResponse(Appointment) AppointmentResponse
  + updateStatus(Long, String) AppointmentResponse
}
class AppointmentStatus {
<<enumeration>>
  +  CANCELADO
  +  COMPLETADO
  +  PENDIENTE
  - String dbValue
  + fromDbValue(String) AppointmentStatus
  + getDbValue() String
  + toString() String
  + valueOf(String) AppointmentStatus
  + values() AppointmentStatus[]
}
class AppointmentStatusConverter {
  + convertToDatabaseColumn(AppointmentStatus) String
  + convertToEntityAttribute(String) AppointmentStatus
}
class CatalogController {
  - PatientRepository patientRepo
  - RoomRepository roomRepo
  - TherapistRepository therapistRepo
  + getPatients() List~Patient~
  + getRooms() List~Room~
  + getSessionTypes() List~String~
  + getTherapists() List~Therapist~
}
class DataLoader {
  - AppointmentRepository appointmentRepo
  - PatientRepository patientRepo
  - RoomRepository roomRepo
  - TherapistRepository therapistRepo
  + run(String[]) void
}
class OpenApiConfig {
  + customOpenAPI() OpenAPI
}
class Patient {
  - LocalDate birthDate
  - String email
  - String firstName
  - String folio
  - Long id
  - String lastName
  - String phone
  + getBirthDate() LocalDate
  + getEmail() String
  + getFirstName() String
  + getFolio() String
  + getId() Long
  + getLastName() String
  + getPhone() String
  + setBirthDate(LocalDate) void
  + setEmail(String) void
  + setFirstName(String) void
  + setFolio(String) void
  + setId(Long) void
  + setLastName(String) void
  + setPhone(String) void
}
class PatientRepository {
<<Interface>>
  + existsByFolio(String) boolean
}
class Room {
  - Long id
  - String nombre
  + getId() Long
  + getNombre() String
  + setNombre(String) void
}
class RoomRepository {
<<Interface>>
  + findByNombre(String) Optional~Room~
}
class SessionType {
<<enumeration>>
  +  CITA_DE_TERAPIA
  +  EVALUACION_INICIAL
  +  EVALUACION_INICIAL_INTEGRAL
  + valueOf(String) SessionType
  + values() SessionType[]
}
class SpringbootApplication {
  + main(String[]) void
}
class Therapist {
  - Long id
  - String name
  + getId() Long
  + getName() String
}
class TherapistRepository {
<<Interface>>

}

Appointment "1" *--> "appointmentStatus 1" AppointmentStatus 
Appointment "1" *--> "patient 1" Patient 
Appointment "1" *--> "room 1" Room 
Appointment "1" *--> "sessionType 1" SessionType 
Appointment "1" *--> "therapist 1" Therapist 
AppointmentCalendarItem "1" *--> "sessionType 1" SessionType 
AppointmentController "1" *--> "appointmentService 1" AppointmentService 
AppointmentRequest "1" *--> "sessionType 1" SessionType 
AppointmentService  ..>  Appointment : «create»
AppointmentService  ..>  AppointmentCalendarItem : «create»
AppointmentService "1" *--> "appointmentRepo 1" AppointmentRepository 
AppointmentService  ..>  AppointmentResponse : «create»
AppointmentService  ..>  Patient : «create»
AppointmentService "1" *--> "patientRepo 1" PatientRepository 
AppointmentService "1" *--> "roomRepo 1" RoomRepository 
AppointmentService "1" *--> "therapistRepo 1" TherapistRepository 
CatalogController "1" *--> "patientRepo 1" PatientRepository 
CatalogController "1" *--> "roomRepo 1" RoomRepository 
CatalogController "1" *--> "therapistRepo 1" TherapistRepository 
DataLoader "1" *--> "appointmentRepo 1" AppointmentRepository 
DataLoader  ..>  Patient : «create»
DataLoader "1" *--> "patientRepo 1" PatientRepository 
DataLoader  ..>  Room : «create»
DataLoader "1" *--> "roomRepo 1" RoomRepository 
DataLoader  ..>  Therapist : «create»
DataLoader "1" *--> "therapistRepo 1" TherapistRepository 
