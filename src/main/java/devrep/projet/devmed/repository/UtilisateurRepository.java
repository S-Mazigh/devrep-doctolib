package devrep.projet.devmed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import devrep.projet.devmed.entities.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>{
    // Unique Utilisateur
    Utilisateur findByEmail(String email);

    // Lists
    List<Utilisateur> findByNomAndPrenom(String nom, String prenom);
    List<Utilisateur> findByNom(String nom);
    List<Utilisateur> findByPrenom(String prenom);

    // with address
    List<Utilisateur> findByNomAndPrenomAndAdresseContaining(String nom, String prenom, String adresse);
    List<Utilisateur> findByPrenomAndAdresseContaining(String prenom, String adresse);
    List<Utilisateur> findByNomAndAdresseContaining(String nom,String adresse);
    List<Utilisateur> findByAdresseContaining(String adresse);

    // with domaine
    List<Utilisateur> findByDomaine(String domaine);
    List<Utilisateur> findByDomaineAndAdresseContaining(String domaine, String adresse);
}
