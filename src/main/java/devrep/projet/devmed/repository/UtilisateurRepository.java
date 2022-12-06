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
    List<Utilisateur> findByNomLikeAndPrenomLikeAndAuthority(String nom, String prenom, String authority);
    List<Utilisateur> findByNomLikeAndAuthority(String nom, String authority);
    List<Utilisateur> findByPrenomLikeAndAuthority(String prenom, String authority);

    // with address
    List<Utilisateur> findByNomLikeAndPrenomLikeAndAuthorityAndVilleContaining(String nom, String prenom, String authority, String Ville);
    List<Utilisateur> findByPrenomLikeAndAuthorityAndVilleContaining(String prenom, String authority, String Ville);
    List<Utilisateur> findByNomLikeAndAuthorityAndVilleContaining(String nom, String authority, String Ville);
    List<Utilisateur> findByAuthorityAndVilleContaining(String authority, String Ville);

    // with domaine
    List<Utilisateur> findByDomaineLike(String domaine);
    List<Utilisateur> findByDomaineLikeAndVilleContaining(String domaine, String Ville);
}
