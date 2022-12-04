package devrep.projet.devmed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import devrep.projet.devmed.entities.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>{
    // Unique Utilisateur
    Utilisateur findByEmail(String email);

    // Lists
    List<Utilisateur> findByNomAndPrenomAndAuthority(String nom, String prenom, String authority);
    List<Utilisateur> findByNomAndAuthority(String nom, String authority);
    List<Utilisateur> findByPrenomAndAuthority(String prenom, String authority);

    // with address
    List<Utilisateur> findByNomAndPrenomAndAuthorityAndAdresseContaining(String nom, String prenom, String authority, String adresse);
    List<Utilisateur> findByPrenomAndAuthorityAndAdresseContaining(String prenom, String authority, String adresse);
    List<Utilisateur> findByNomAndAuthorityAndAdresseContaining(String nom, String authority, String adresse);
    List<Utilisateur> findByAuthorityAndAdresseContaining(String authority, String adresse);

    // with domaine
    List<Utilisateur> findByDomaine(String domaine);
    List<Utilisateur> findByDomaineAndAdresseContaining(String domaine, String adresse);
}
