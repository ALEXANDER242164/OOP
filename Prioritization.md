
# Prioritization Document - Appointment Management System

This document presents the prioritization of the **functional requirements** for the appointment management system. Prioritization is performed using the **MoSCoW methodology**, classifying requirements into:  

- **Must:** Requirements essential for the basic operation of the system.  
- **Should:** Important requirements but not critical for the first version.  
- **Could:** Desirable requirements that can be implemented if time or resources allow.  
- **Won’t:** Requirements that will not be included in this version due to scope or priority.  

The prioritization ensures that deliverables meet the primary user needs and business objectives.

---

## 1. Must (Essential)

### 1.1 FR - Appointment Creation
**Description:** Allows authorized users (secretaries, coordinators) to register new appointments in the system.  
**Justification:** Critical for the system to fulfill its main purpose of managing appointments.  
**Details:**  
- Creation form with mandatory fields: session type, patient, therapist, room, date, and time.  
- Automatic generation of a unique folio for each patient on their first appointment.  
- Business rule validation:  
  - No appointments on weekends.  
  - Appointments within working hours (09:00 - 17:30).  
  - No appointments beyond six months from the current date.  
  - No schedule conflicts (patient, therapist, room).  
- Visual confirmation of the appointment on the calendar upon successful creation.  

### 1.2 FR - View Appointment
**Description:** Allows consulting scheduled appointments through an adaptable and intuitive interface.  
**Justification:** Essential for staff organization and efficient resource management.  
**Details:**  
- Calendar view (agenda type).  
- Filters by day or week.  
- Color-coded by appointment type and daily occupancy level.  
- Full detail view when clicking an appointment.  

### 1.3 FR - Update Appointment
**Description:** Allows authorized users to modify existing appointments.  
**Justification:** Critical to maintain accurate scheduling and avoid conflicts.  
**Details:**  
- Edit access from appointment details view.  
- Pre-populated edit form with current data.  
- Same business rule validations as in creation.  
- Historical record of rescheduled appointments.  
- Visual confirmation of changes on the calendar.  

---

## 2. Should (Important but Not Critical)
**Description:** No requirements have been identified for this category in the initial version.  
**Justification:** All current requirements are essential for the system’s core operation; there are no secondary features that add immediate value without affecting functionality.  

---

## 3. Could (Desirable if Time Allows)

### 3.1 FR - Automatic Reminders
**Description:** Sends notifications to patients and therapists before appointments (email or SMS).  
**Justification:** Enhances user experience and reduces no-shows but is not critical for initial system operation.  
**Details:**  
- Configurable reminders (time, notification type).  
- Optional integration with email and messaging services.  

---

## 4. Won’t (Out of Scope for This Version)

### 4.1 FR - Recurring Appointments
**Description:** The system could provide a feature to create recurring or repeating appointments automatically (e.g., weekly therapy sessions for the same patient and therapist), including automatic generation of folios for each new occurrence.  
**Justification:** While this functionality would allow users to schedule long-term sessions efficiently and reduce repetitive data entry, it is not essential for the initial release focused on basic appointment creation, viewing, and updating. Therefore, it is out of scope for the current version.

