# Prioritization Document - Appointment Management System

This document presents the prioritization of the **functional requirements** for the appointment management system. Prioritization is performed using the **MoSCoW methodology**, classifying requirements into:  

- **Must:** Requirements essential for the basic operation of the system.  
- **Should:** Important requirements but not critical for the first version.  
- **Could:** Desirable requirements that can be implemented if time or resources allow.  
- **Won’t:** Requirements that will not be included in this version due to scope or priority.  

The prioritization ensures that deliverables meet the primary user needs and business objectives.

---

## 1. Must (Essential) - Refined Requirements

### 1.1 FR - Appointment Creation

**Description:** Allows authorized users to register new appointments in the system. **Justification:** Critical for the system to fulfill its main purpose of managing appointments. **Details (Refined):**

- Creation form with mandatory fields: Session type, **Patient (pre-searched/validated)**, Therapist, Room, Date and Time, and **Initial Appointment Status (e.g., "Scheduled")**.
    
- **Patient Existence Validation:** The system must search for the patient before creating a new record.
    
- **Automatic Folio Generation:** A unique folio is only generated if the patient **does not exist** previously in the system.
    
- **Instantaneous Business Rule Validation:**
    
    - No appointments on weekends.
        
    - Appointments within working hours (09:00 - 17:30).
        
    - No appointments beyond six months from the current date.
        
    - **Instantaneous conflict validation** (patient, therapist, room) with a **suggestion of an alternative time or room**.
        
- Visual confirmation of the appointment on the calendar upon successful creation.
    

### 1.2 FR - View Appointment

**Description:** Allows consulting scheduled appointments through an adaptable and intuitive interface. **Justification:** Essential for staff organization and efficient resource management. **Details (Refined):**

- Calendar view (agenda type).
    
- **Robust Filters:** By day, week, **Therapist**, and **Room**.
    
- Color-coded: By appointment type, daily occupancy level, and a **visual indicator of the Appointment Status** (e.g., "Confirmed", "Pending").
    
- Full detail view when clicking an appointment, including **direct access to the patient's record** and Update/Cancellation actions.
    

### 1.3 FR - Update Appointment (and Appointment Lifecycle)

**Description:** Allows authorized users to modify, cancel, and change the status of existing appointments. **Justification:** Critical to maintain accurate scheduling, manage operational flow, and avoid conflicts. **Details (Refined):**

- Access to edit from the appointment details view.
    
- Pre-populated edit form with current data.
    
- **Same business rule validations as in creation**, plus the addition of **time restrictions** (e.g., do not allow editing the "date/time" field if the appointment is less than 2 hours away).
    
- **Appointment Status Change Functionality** (to "Canceled", "Completed", "No-show", etc.).
    
- **Appointment Cancellation Functionality** with a mandatory field for the **Reason for Cancellation**.
    
- **Historical record** of rescheduled and canceled appointments.
    
- Visual confirmation of changes on the calendar.