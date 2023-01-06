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
        System.out.println(nom+", "+prenom);
        List<Utilisateur> toReturn = null;
        // Faire attention si l'utilisateur a inverser le nom et prénom ou s'il a donné
        // qu'un nom ou prénom
        if (where.isBlank()) {
            System.out.println("where blank");
            if (!nom.isBlank() && !prenom.isBlank()){
                toReturn = userRepo.findByNomLikeAndPrenomLikeAndAuthorityOrderByNomAsc(prenom, nom, "PRO");
                toReturn.addAll(userRepo.findByNomLikeAndPrenomLikeAndAuthorityOrderByNomAsc(nom, prenom, "PRO"));
                return toReturn;
            }
            if (!prenom.isBlank()) {
                toReturn = userRepo.findByNomLikeAndAuthorityOrderByNomAsc(prenom, "PRO");
                toReturn.addAll(userRepo.findByPrenomLikeAndAuthorityOrderByNomAsc(prenom, "PRO"));
                return toReturn;
            }
            else {// if all blank
                return userRepo.findByAuthorityOrderByNomAsc("PRO");
            }
        } else {
            if (!nom.isBlank() && !prenom.isBlank()) {
                toReturn = userRepo.findByNomLikeAndPrenomLikeAndAuthorityAndVilleContainingOrderByNomAsc(prenom, nom, "PRO", where);
                toReturn.addAll(userRepo.findByNomLikeAndPrenomLikeAndAuthorityAndVilleContainingOrderByNomAsc(nom, prenom, "PRO", where));
                return toReturn;
            }
                
            if (!nom.isBlank()) {
                toReturn = userRepo.findByNomLikeAndAuthorityAndVilleContainingOrderByNomAsc(prenom,"PRO", where);
                toReturn.addAll(userRepo.findByPrenomLikeAndAuthorityAndVilleContainingOrderByNomAsc(prenom,"PRO", where));
                return toReturn;
            }
                
            else // if nom et prenom are blank
                return userRepo.findByAuthorityAndVilleContainingOrderByNomAsc("PRO", where);
        }
    }

    // On est sûr que c'est un pro vu qu'un patient a Domain = null
    @Transactional(readOnly = true)
    public List<Utilisateur> getProByDomaine(String domaine, String where) {
        if (where.isBlank())
            return userRepo.findByDomaineLikeAndAuthorityOrderByNomAsc(domaine, "PRO");
        else {
            return userRepo.findByDomaineLikeAndVilleContainingAndAuthorityOrderByNomAsc(domaine, where, "PRO");
        }
    }

    @Transactional(readOnly = true)
    public List<Utilisateur> getProByDomaineOrName(String domaineOrName, String where) {
        Set<Utilisateur> set = new HashSet<Utilisateur>();
        set.addAll(getProByName(domaineOrName, where));
        set.addAll(getProByDomaine(domaineOrName, where));
        //System.out.println("Result SET : " + set);
        return new ArrayList<Utilisateur>(set);
    }
}