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
    UtilisateurRepository userRepo;

    @Transactional(readOnly = true)
    public List<Utilisateur> getProByName(String fullname, String where) {
        // string split needed
        String[] fullName = fullname.split(" ");
        String nom="", prenom="";
        if(fullName.length>1)
            nom = fullName[1];
        if(fullName.length!=0)
        prenom = fullName[0];
        List<Utilisateur> toReturn = null;
        // Faire attention si l'utilisateur a inverser le nom et prénom ou s'il a donné
        // qu'un nom ou prénom
        if (where.isBlank()) {
            if (!nom.isBlank() && !prenom.isBlank()){
                toReturn = userRepo.findByNomAndPrenomAndAuthority(prenom, nom, "PRO");
                toReturn.addAll(userRepo.findByNomAndPrenomAndAuthority(nom, prenom, "PRO"));
                return toReturn;
            }
            if (!nom.isBlank()) {
                toReturn = userRepo.findByNomAndAuthority(prenom, "PRO");
                toReturn.addAll(userRepo.findByPrenomAndAuthority(prenom, "PRO"));
                return toReturn;
            }
            else // if all blank
                return userRepo.findAll();
        } else {
            if (!nom.isBlank() && !prenom.isBlank()) {
                toReturn = userRepo.findByNomAndPrenomAndAuthorityAndAdresseContaining(prenom, nom, "PRO", where);
                toReturn.addAll(userRepo.findByNomAndPrenomAndAuthorityAndAdresseContaining(nom, prenom, "PRO", where));
                return toReturn;
            }
                
            if (!nom.isBlank()) {
                toReturn = userRepo.findByNomAndAuthorityAndAdresseContaining(prenom,"PRO", where);
                toReturn.addAll(userRepo.findByPrenomAndAuthorityAndAdresseContaining(prenom,"PRO", where));
                return toReturn;
            }
                
            else // if nom et prenom are blank
                return userRepo.findByAuthorityAndAdresseContaining("PRO", where);
        }
    }

    // On est sûr que c'est un pro vu qu'un patient a Domain = null
    @Transactional(readOnly = true)
    public List<Utilisateur> getProByDomaine(String domaine, String where) {
        if (where.isBlank())
            return userRepo.findByDomaine(domaine);
        else {
            return userRepo.findByDomaineAndAdresseContaining(domaine, where);
        }
    }
}
