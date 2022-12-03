package devrep.projet.devmed.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import devrep.projet.devmed.entities.Domaine;
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

    /*
    private String encrypt(String toEncrypt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(toEncrypt.getBytes());
            byte[] encrypted = md.digest();
            generatedPassword = new String(encrypted, "UTF-8");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
    */

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
        toAdd.setAuthority("patient");
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
        toAdd.setDomaine(Domaine.valueOf(allParams.get("domain")));
        toAdd.setAuthority("pro");
        // ajout des rendez vous ...
        // appel à la bd
        UtilisateurBD.save(toAdd);
        return true;
    }

    /*
    @Transactional
    public Etat loginUser(String email, String password, String isPro) {
        Etat newState = new Etat(); // init to all false and Guest.
        Utilisateur user;
        if (isPro.equals("pro")) {
            user = UtilisateurBD.findByEmail(email);
            if (user == null) {
                newState.setBadEmail(true);
                return newState;
            }
            // user exists, check if password is correct
            if (!user.getMotDePasse().equals(encrypt(password))) {
                newState.setWrongPass(true);
                return newState;
            }
            // all is good
            newState.setConnected(true);
            newState.setPro(true);
            newState.setWho(user);
            return newState;
        } else { // patient
            user = UtilisateurBD.findByEmail(email);
            if (user == null) {
                newState.setBadEmail(true);
                return newState;
            }
            // user exists, check if password is correct
            if (!user.getMotDePasse().equals(encrypt(password))) {
                newState.setWrongPass(true);
                return newState;
            }
            // all is good
            newState.setConnected(true);
            newState.setWho(user);
            return newState;
        }
    }
    */

    @Transactional
    public void modifyProfile(Etat state, Map<String, String> allParams) {
        Utilisateur user = state.getWho();
        user.setEmail(allParams.get("Email"));
        user.setMotDePasse(secuConfig.passwordEncoder().encode(allParams.get("Password")));
        user.setNom(allParams.get("Nom"));
        user.setPrenom(allParams.get("Prenom"));
        if (state.isPro()) {
            // mettre à jour les truc exclusifs aux pros
            // tel, rdv ...
            // ajout de la nouvelle version
            UtilisateurBD.save((Utilisateur) user);
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