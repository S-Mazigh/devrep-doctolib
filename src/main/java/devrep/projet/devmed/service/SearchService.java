package devrep.projet.devmed.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public Utilisateur getById(Long id){
        Optional<Utilisateur> result = userRepo.findById(id);
        if(result.isPresent())
            return result.get();
        Utilisateur error = new Utilisateur();
        error.setNom("Unknown");
        return error;
    }

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
                toReturn = userRepo.findByNomLikeAndPrenomLikeAndAuthority(prenom, nom, "PRO");
                toReturn.addAll(userRepo.findByNomLikeAndPrenomLikeAndAuthority(nom, prenom, "PRO"));
                return toReturn;
            }
            if (!nom.isBlank()) {
                toReturn = userRepo.findByNomLikeAndAuthority(prenom, "PRO");
                toReturn.addAll(userRepo.findByPrenomLikeAndAuthority(prenom, "PRO"));
                return toReturn;
            }
            else // if all blank
                return userRepo.findByAuthority("PRO");
        } else {
            if (!nom.isBlank() && !prenom.isBlank()) {
                toReturn = userRepo.findByNomLikeAndPrenomLikeAndAuthorityAndVilleContaining(prenom, nom, "PRO", where);
                toReturn.addAll(userRepo.findByNomLikeAndPrenomLikeAndAuthorityAndVilleContaining(nom, prenom, "PRO", where));
                return toReturn;
            }
                
            if (!nom.isBlank()) {
                toReturn = userRepo.findByNomLikeAndAuthorityAndVilleContaining(prenom,"PRO", where);
                toReturn.addAll(userRepo.findByPrenomLikeAndAuthorityAndVilleContaining(prenom,"PRO", where));
                return toReturn;
            }
                
            else // if nom et prenom are blank
                return userRepo.findByAuthorityAndVilleContaining("PRO", where);
        }
    }

    // On est sûr que c'est un pro vu qu'un patient a Domain = null
    @Transactional(readOnly = true)
    public List<Utilisateur> getProByDomaine(String domaine, String where) {
        if (where.isBlank())
            return userRepo.findByDomaineLikeAndAuthority(domaine, "PRO");
        else {
            return userRepo.findByDomaineLikeAndVilleContainingAndAuthority(domaine, where, "PRO");
        }
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> getProByDomaineOrName(String domaineOrName, String where) {
        Set<Utilisateur> set = new HashSet<Utilisateur>(getProByDomaine(domaineOrName, where));
        set.addAll(getProByName(domaineOrName, where));
        // System.out.println("Result SET : " + set);
        return new ArrayList<Utilisateur>(set);
    }
}