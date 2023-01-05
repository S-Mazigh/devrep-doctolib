package devrep.projet.devmed.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import devrep.projet.devmed.entities.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>{
    // Unique Utilisateur
    Optional<Utilisateur> findById(Long id);
    Utilisateur findByEmail(String email);

    // Lists
    List<Utilisateur> findByAuthorityOrderByNomAsc(String authority);
    List<Utilisateur> findByNomLikeAndPrenomLikeAndAuthorityOrderByNomAsc(String nom, String prenom, String authority);
    List<Utilisateur> findByNomLikeAndAuthorityOrderByNomAsc(String nom, String authority);
    List<Utilisateur> findByPrenomLikeAndAuthorityOrderByNomAsc(String prenom, String authority);

    // with address
    List<Utilisateur> findByNomLikeAndPrenomLikeAndAuthorityAndVilleContainingOrderByNomAsc(String nom, String prenom, String authority, String Ville);
    List<Utilisateur> findByPrenomLikeAndAuthorityAndVilleContainingOrderByNomAsc(String prenom, String authority, String Ville);
    List<Utilisateur> findByNomLikeAndAuthorityAndVilleContainingOrderByNomAsc(String nom, String authority, String Ville);
    List<Utilisateur> findByAuthorityAndVilleContainingOrderByNomAsc(String authority, String Ville);

    // with domaine
    List<Utilisateur> findByDomaineLikeAndAuthorityOrderByNomAsc(String domaine, String authority);
    List<Utilisateur> findByDomaineLikeAndVilleContainingAndAuthorityOrderByNomAsc(String domaine, String Ville, String authority);
}
