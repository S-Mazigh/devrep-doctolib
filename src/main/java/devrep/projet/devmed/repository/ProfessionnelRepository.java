package devrep.projet.devmed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import devrep.projet.devmed.entities.Professionnel;

public interface ProfessionnelRepository extends JpaRepository<Professionnel, Long>{
    // Unique professionnel
    Professionnel findByEmail(String email);

    // Lists
    List<Professionnel> findByNomAndPrenom(String nom, String prenom);
    List<Professionnel> findByNom(String nom);
    List<Professionnel> findByPrenom(String prenom);

    // with address
    List<Professionnel> findByNomAndPrenomAndAdresseContaining(String nom, String prenom, String adresse);
    List<Professionnel> findByPrenomAndAdresseContaining(String prenom, String adresse);
    List<Professionnel> findByNomAndAdresseContaining(String nom,String adresse);
    List<Professionnel> findAdresseContaining(String adresse);

    // with domaine
    List<Professionnel> findByDomaine(String domaine);
    List<Professionnel> findByDomaineAndAdresseContaining(String domaine, String adresse);
}
