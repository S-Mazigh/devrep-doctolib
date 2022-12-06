package devrep.projet.devmed.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import devrep.projet.devmed.entities.Etat;
import devrep.projet.devmed.entities.Utilisateur;
import devrep.projet.devmed.repository.UtilisateurRepository;
import devrep.projet.devmed.security.ApplicationSecurityConfiguration;

/* utilise partout le transactional vu qu'il est desactivé dans DevmedApplication */
@Service
public class DataService {

    @Autowired
    private UtilisateurRepository UtilisateurBD;

    @Autowired
    private ApplicationSecurityConfiguration secuConfig;

    @Transactional
    public boolean addPatient(Map<String, String> allParams) {
        Utilisateur toAdd;
        // check if email is unique
        toAdd = UtilisateurBD.findByEmail(allParams.get("Email"));
        if (toAdd != null) {// already exists
            return false;
        }
        toAdd = new Utilisateur();
        toAdd.setEmail(allParams.get("Email"));
        toAdd.setMotDePasse(secuConfig.passwordEncoder().encode(allParams.get("Password")));
        toAdd.setNom(allParams.get("Nom"));
        toAdd.setPrenom(allParams.get("Prenom"));
        toAdd.setAuthority("PATIENT");
        // appel à la bd
        UtilisateurBD.save(toAdd);
        return true;
    }

    @Transactional
    public boolean addPro(Map<String, String> allParams) {
        Utilisateur toAdd;
        // check if email is unique
        toAdd = UtilisateurBD.findByEmail(allParams.get("Email"));
        if (toAdd != null) {// already exists
            return false;
        }
        toAdd = new Utilisateur();
        toAdd.setEmail(allParams.get("Email"));
        toAdd.setMotDePasse(secuConfig.passwordEncoder().encode(allParams.get("Password")));
        toAdd.setNom(allParams.get("Nom"));
        toAdd.setPrenom(allParams.get("Prenom"));
        toAdd.setDomaine(allParams.get("domaine"));//allParams.get("domain")));
        toAdd.setAuthority("PRO");
        // ajout des rendez vous ...
        // appel à la bd
        UtilisateurBD.save(toAdd);
        return true;
    }

    @Transactional
    public void modifyProfile(Etat state, Map<String, String> allParams) {
        Utilisateur user = state.getWho();
        user.setEmail(allParams.get("Email"));
        // user.setMotDePasse(secuConfig.passwordEncoder().encode(allParams.get("Password")));
        user.setNom(allParams.get("Nom"));
        user.setPrenom(allParams.get("Prenom"));
        if (state.isPro()) {
            // mettre à jour les truc exclusifs aux pros
            // tel, rdv ...
            // ajout de la nouvelle version
            user.setNumTelephone(allParams.get("Tel"));
            user.setAdresse(allParams.get("Rue"));
            user.setVille(allParams.get("Ville"));
            user.setPays(allParams.get("Pays"));
            UtilisateurBD.save(user);
            return;
        }
        // ajout de la nouvelle version
        /*
         * JPA et du coup spring utilise save pour insert et update. Quand on récupére
         * un objet avec un query qu'on le maj et on resave il remarquera que la
         * primary key n'a pas changé du coup il update
         */
        UtilisateurBD.save((Utilisateur) user);
    }
}