package devrep.projet.devmed.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import devrep.projet.devmed.entities.Utilisateur;
import devrep.projet.devmed.repository.UtilisateurRepository;

/* utilise partout le transactional vu qu'il est desactivé dans DevmedApplication */
@Service
public class SearchService {

    @Autowired
    UtilisateurRepository proRepo;

    @Transactional(readOnly = true)
    public List<Utilisateur> getProByName(String fullname, String where) {
        // string split needed
        String[] fullName = fullname.split(" ");
        String nom = fullName[1];
        String prenom = fullName[0];
        List<Utilisateur> toReturn = null;
        // Faire attention si l'utilisateur a inverser le nom et prénom ou s'il a donné
        // qu'un nom ou prénom
        if (where.isBlank()) {
            if (!nom.isBlank() && !prenom.isBlank()){
                toReturn = proRepo.findByNomAndPrenom(prenom, nom);
                toReturn.addAll(proRepo.findByNomAndPrenom(nom, prenom));
                return toReturn;
            }
            if (!nom.isBlank()) {
                toReturn = proRepo.findByNom(prenom);
                toReturn.addAll(proRepo.findByPrenom(prenom));
                return toReturn;
            }
            else // if all blank
                return proRepo.findAll();
        } else {
            if (!nom.isBlank() && !prenom.isBlank()) {
                toReturn = proRepo.findByNomAndPrenomAndAdresseContaining(prenom, nom, where);
                toReturn.addAll(proRepo.findByNomAndPrenomAndAdresseContaining(nom, prenom, where));
                return toReturn;
            }
                
            if (!nom.isBlank()) {
                toReturn = proRepo.findByNomAndAdresseContaining(prenom, where);
                toReturn.addAll(proRepo.findByPrenomAndAdresseContaining(prenom, where));
                return toReturn;
            }
                
            else // if nom et prenom are blank
                return proRepo.findAdresseContaining(where);
        }
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> getProByDomaine(String domaine, String where) {
        if (where.isBlank())
            return proRepo.findByDomaine(domaine);
        else {
            return proRepo.findByDomaineAndAdresseContaining(domaine, where);
        }
    }
}
