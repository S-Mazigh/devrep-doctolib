package devrep.projet.devmed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import devrep.projet.devmed.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Unique patient
    Patient findByEmail(String Email);
}
