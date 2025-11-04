# Main Use Cases

- The **main actors** are the **secretaries or coordinators**, since they are the ones who **interact directly** with the system to create, view, or modify appointments.
- The **patient** and the **therapist** are **secondary actors** or **involved entities**, since their data is used for scheduling, but they do not operate the application.

---

## Use Case 1 — Schedule Appointment

**Main Actor:** Secretary / Coordinator (authorized user)

**Goal:** Register a new appointment in the system and, if it is the patient’s first appointment, generate their folio.

**Preconditions:** The user is authorized and has opened the appointment creation screen.

**Main Flow (steps):**

1. The user opens the “Schedule Appointment” form.
2. Completes: session type, patient, therapist, room, date, start and end time.
3. If everything is valid, the system saves the appointment, generates the folio if it is the patient’s first appointment, shows a confirmation message, and displays the appointment in the calendar.

**Alternative Flows / Exceptions:**

- If the date falls on a weekend → the system shows an error and does not save.
- If the time is outside 09:00–17:30 → error and not saved.
- If the date is more than 6 months ahead → error and not saved.
- If there is a scheduling conflict (patient/therapist/room) → error and not saved, showing the cause of the conflict.

**Postcondition:** The appointment appears in the calendar (or an error is reported).

---

## Use Case 2 — View/Consult Appointments (Agenda)

**Main Actor:** Secretary / Coordinator / any authorized user to view agenda

**Goal:** Consult appointments in an agenda-type view and filter by day or by week.

**Preconditions:** The user is in the calendar view.

**Main Flow:**

1. The user selects “Day” or “Week” view.
2. The system displays the corresponding appointments in agenda format.
3. The user can visually identify session type and daily workload by colors.
4. The user clicks on an appointment to see full details.

**Alternative Flows:**

- If there are no appointments on the selected date, the system shows the message “No appointments.”

**Postcondition:** The user sees the appointments and can open the details of any of them.

---

## Use Case 3 — Edit / Reschedule Appointment

**Main Actor:** Secretary / Coordinator (authorized user)

**Goal:** Change the date, time, room, therapist, or type of an existing appointment.

**Preconditions:** The appointment exists and the user has opened its details view.

**Main Flow:**

1. In the details view, the user clicks “Edit / Reschedule.”
2. A form appears with the current appointment data (pre-filled).
3. The user modifies the necessary fields and saves.
4. The system reapplies the same business rule validations.
5. If everything is valid, the system saves the new date, records that it was rescheduled (history), and confirms with a message; the calendar updates.

**Alternative Flows:**

- If the new selection violates a rule (weekend, schedule, conflict, >6 months) → error is shown and it is not saved.

**Postcondition:** Appointment is updated and a record exists indicating that it was rescheduled (original date referenced).

---

## Use Case 4 — Generate and Associate Patient Folio

**Main Actor:** System (automatic) / Secretary (indirect)

**Goal:** Create a unique folio the first time an appointment is registered for a patient and keep it associated with that patient.

**Preconditions:** The patient’s first appointment is being created in the system.

**Main Flow:**

1. When saving the patient’s first appointment, the system creates a unique folio and associates it with the patient.
2. The folio remains visible in the patient’s profile and in future appointments.

**Postcondition:** The patient has a permanent folio.

---

## Use Case 5 — User Feedback and Messages

**Main Actor:** System / Authorized User

**Goal:** Inform the user of success or error in operations (create, view, save, reschedule).

**Preconditions:** Any relevant CRUD operation is performed.

**Main Flow:**

1. Upon completing an operation, the system shows a clear message with the result (success or error).
2. The messages must be concise and specific about the cause if there was an error.

**Timing Requirement:** Messages must appear immediately (visually in less than 1 second).