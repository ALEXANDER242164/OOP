# Clinic-System
## Second Review

### Introduction
For this second delivery, we continue working on the [Clinica Web repository](https://github.com/Proyectos-Vinculacion-FMAT/ClinicaWeb) by Carlos Roberto Ek Raigoza, a project designed for the Psychology Faculty.  
In the first review, we focused on defining the appointment scheduling module at a requirements and design level.

In this second review, our main goal is to **implement the backend API** for the appointment scheduling module so that the system can actually store and manage appointments instead of being just a prototype on paper.

We worked mainly on the `Second-review` and `Second2.0` branches of this repository, where the Spring Boot project and updated documentation live.

### Product Description
The product is a specific module of the Clinica Web project: **the appointment scheduling system**.

This module allows the faculty staff (especially the secretary/administrator) to:

- Register patients and their appointments.
- Manage the schedule of therapists and rooms.
- View, create, update, and cancel appointments in a more organized and efficient way.

The goal is to reduce manual work, avoid overlapping appointments, and give the staff a clearer view of the agenda.

### Objectives
For this second review, our objectives are:

- Implement a **REST API** for the appointment scheduling module.
- Provide endpoints to **create, read, update, and cancel (CRUD)** appointments.
- Support **different appointment types** (e.g., therapy session, initial integral evaluation).
- Prepare the backend so it can later be consumed by the frontend already started in the project.

---

## Relevance
- [Value Proposition (First Review)](https://github.com/ALEXANDER242164/OOP/blob/First-review/documentation/ValueProposition.md)
- Updated prioritization for this delivery:
    - [Feature Prioritization (Second Review)](https://github.com/ALEXANDER242164/OOP/blob/Second-review/PrioritizationF.md)

---

## Limitations
- The system still **does not handle payment processing or billing**.
- There is **no direct integration** yet with external services (e.g. Google Calendar, WhatsApp).
- The current version is focused on the **admin/staff side only**; there is no patient-facing portal.
- The backend is functional, but **deployment to a public server/hosting is not yet implemented**.
- Some parts are still in **pre-alpha** state and subject to change as we refine requirements.

---

## Requirements

> ⚠️ Note: The base requirements of the project were defined in the first review. For this second review, we extend and refine them mainly around the appointment module.

- [Functional Requirements (First Review)](https://github.com/ALEXANDER242164/OOP/blob/First-review/Requirements.md)
- [Non-functional Requirements (First Review)](https://github.com/ALEXANDER242164/OOP/blob/First-review/Requirements.md#2-non-functional-requirements-nfr)
- [Prioritization for Second Review](https://github.com/ALEXANDER242164/OOP/blob/Second-review/PrioritizationF.md)

---

## Artifacts

> Many artifacts were created or refined during the second review. They are stored mainly in the `Second-review` branch.

- **Use Case Diagram (Agenda de Citas)**
    - Stored in the `diagrama` / `assep` folders of the `Second-review` branch.
- **Use Cases**
    - Documentation for the main flows of the appointment scheduling module.
    - Located under the `documentacion` folder in `Second-review`.
- **User Stories**
    - Stories focused on the secretary/administrator managing appointments.
    - Also inside the `documentacion` folder.
- **Class Diagram / Domain Model**
    - Updated class diagram showing entities such as `Appointment`, `Patient`, `Therapist`, and `Room`.
    - Located in the `diagrama` folder of the `Second-review` branch.

> You can browse all of them directly from the `Second-review` branch:
> `https://github.com/ALEXANDER242164/OOP/tree/Second-review`

---

## Process

During this second review we focused on:

- Refining the **scope of the appointment module** based on feedback.
- Defining which endpoints were strictly necessary for this delivery.
- Designing the **data model** and relationships needed for appointments.
- Implementing the first functional version of the **Spring Boot API**.
- Reviewing the work distribution and contributions of each team member.

Documentation related to process and management is stored in the `documentacion` folder (branch `Second-review`).

---

## Backend Implementation

The core of the second review is the backend implementation of the agenda:

- Branch `Second2.0`:
    - Contains the **Spring Boot project** ready to build and run.
- Branch `Second-review`:
    - Contains code, documentation, diagrams and an initial frontend folder.

Key points of the implementation:

- REST API for managing appointments.
- Entities such as `Appointment`, `Patient`, `Therapist`, and `Room`.
- Separation between **domain model**, **controllers**, and **persistence layer**.
- Preparation for future integration with the existing frontend.

---

## Technologies

Technologies used in this delivery:

- **Java**
- **Spring Boot** (REST API)
- **Spring Data JPA**
- **H2** (in-memory database for development)
- **Swagger UI** (API documentation and testing)
- **Maven**

---

## Project Progress (History)

### Current Status

- Class structure for the appointment module is mostly defined.
- Main requirements for the appointment module have been discussed and approved.
- A **pre-alpha** version of the API is working:
    - Endpoints are defined and respond correctly.
    - Data is being stored in the database (H2 / SQL).

### Next Steps (Roadmap)

- Finish and polish the **database schema** for production.
- Complete integration with the **frontend** (not part of this branch, but needed for the final system).
- Deploy the API to a **web server / hosting** so it can be accessed externally.
- Add more tests and validation rules for better robustness.

