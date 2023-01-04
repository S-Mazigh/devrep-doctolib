package devrep.projet.devmed.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import devrep.projet.devmed.entities.RendezVous;
import devrep.projet.devmed.entities.Utilisateur;

public interface AppointmentRepository extends JpaRepository<RendezVous, Long> {
    
    public List<RendezVous> findByPatient(Utilisateur patient);
    public List<RendezVous> findByPro(Utilisateur pro);
    public List<RendezVous> findByDaterdv(Date d);
    public List<RendezVous> findByDaterdvAndPro(Date d, Utilisateur pro);

    // suppression
    public void deleteById(Long Id);
}
