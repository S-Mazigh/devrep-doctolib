package devrep.projet.devmed.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import devrep.projet.devmed.entities.Etat;
import devrep.projet.devmed.entities.Patient;
import devrep.projet.devmed.entities.Professionnel;
import devrep.projet.devmed.entities.Utilisateur;
import devrep.projet.devmed.repository.PatientRepository;
import devrep.projet.devmed.repository.ProfessionnelRepository;

/* utilise partout le transactional vu qu'il est desactivé dans DevmedApplication */
@Service
public class DataService {

    @Autowired
    private ProfessionnelRepository proBD;

    @Autowired
    private PatientRepository patientBD;

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

    @Transactional
    public boolean addPatient(Map<String, String> allParams) {
        Patient toAdd;
        // check if email is unique
        toAdd = patientBD.findByEmail(allParams.get("Email"));
        if (toAdd != null) {// already exists
            return false;
        }
        toAdd = new Patient();
        toAdd.setEmail(allParams.get("Email"));
        toAdd.setMotDePasse(encrypt(allParams.get("Password")));
        toAdd.setNom(allParams.get("Nom"));
        toAdd.setPrenom(allParams.get("Prenom"));
        // appel à la bd
        patientBD.save(toAdd);
        return true;
    }

    @Transactional
    public boolean addPro(Map<String, String> allParams) {
        Professionnel toAdd;
        // check if email is unique
        toAdd = proBD.findByEmail(allParams.get("Email"));
        if (toAdd != null) {// already exists
            return false;
        }
        toAdd = new Professionnel();
        toAdd.setEmail(allParams.get("Email"));
        toAdd.setMotDePasse(encrypt(allParams.get("Password")));
        toAdd.setNom(allParams.get("Nom"));
        toAdd.setPrenom(allParams.get("Prenom"));
        // ajout des rendez vous ...
        // appel à la bd
        proBD.save(toAdd);
        return true;
    }

    @Transactional
    public Etat loginUser(String email, String password, String isPro) {
        Etat newState = new Etat(); // init to all false and Guest.
        Utilisateur user;
        if (isPro.equals("pro")) {
            user = proBD.findByEmail(email);
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
            user = patientBD.findByEmail(email);
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

    @Transactional
    public void modifyProfile(Etat state, Map<String, String> allParams) {
        Utilisateur user = state.getWho();
        user.setEmail(allParams.get("Email"));
        user.setMotDePasse(encrypt(allParams.get("Password")));
        user.setNom(allParams.get("Nom"));
        user.setPrenom(allParams.get("Prenom"));
        if (state.isPro()) {
            // mettre à jour les truc exclusifs aux pros
            // tel, rdv ...
            // ajout de la nouvelle version
            proBD.save((Professionnel) user);
            return;
        }
        // ajout de la nouvelle version
        /*
         * JPA et du coup spring utilise save pour insert et update. Quand on récupére
         * un objet avec un query qu'on le maj et on resave il remarquera que la
         * primary key n'a pas changé du coup il update
         */
        patientBD.save((Patient) user);
    }
}
