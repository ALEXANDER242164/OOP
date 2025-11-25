INSERT INTO patients (first_name, last_name, birth_date, email, phone, created_at, updated_at)
VALUES ('Juan', 'Perez', '1990-05-01', 'juan.perez@example.com', '5551234', NOW(), NOW());

INSERT INTO appointments (session_type, patient_id, start_date_time, end_date_time, payment_proof_path, created_at, updated_at)
VALUES ('PSYCHOTHERAPY', 1, '2025-12-01 10:00:00', '2025-12-01 11:00:00', NULL, NOW(), NOW());
