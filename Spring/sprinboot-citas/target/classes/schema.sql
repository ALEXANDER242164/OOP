CREATE TABLE IF NOT EXISTS patients (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(150),
  last_name VARCHAR(150),
  birth_date DATE,
  email VARCHAR(255),
  phone VARCHAR(50),
  created_at DATETIME,
  updated_at DATETIME
);

CREATE TABLE IF NOT EXISTS appointments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  session_type VARCHAR(100),
  patient_id BIGINT,
  therapist_id BIGINT,
  room_id BIGINT,
  start_date_time DATETIME,
  end_date_time DATETIME,
  payment_proof_path VARCHAR(1024),
  created_at DATETIME,
  updated_at DATETIME,
  CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE SET NULL
);
