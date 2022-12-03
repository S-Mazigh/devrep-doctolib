package devrep.projet.devmed.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import devrep.projet.devmed.entities.Etat;
import devrep.projet.devmed.service.DataService;
import devrep.projet.devmed.service.SearchService;

@RequestMapping("/")
@SessionAttributes("etat")
@Controller
public class WebController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private DataService dataService;

    /*
     * va toujours être appelé avant les requestmapping (get, post...)
     * et rajoute l'objet de retour au modèle
     */
    @ModelAttribute(name = "etat")
    public Etat getEtat() {
        Etat state = new Etat();
        return state;
    }

    // RequestMappings
    @GetMapping(path = "/home/search")
    public String getSearchPro(Model model, @RequestParam("searchField") String searchField,
            @RequestParam(name = "searchWhere", required = false) String searchWhere,
            @ModelAttribute("etat") Etat state) {
        model.addAttribute("listePro", searchService.getProByName(searchField, searchWhere));
        model.addAttribute("listeProDomaine", searchService.getProByDomaine(searchField, searchWhere));
        return "Home";
    }

    @PostMapping(path = "/signup/patient")
    public String addPatient(Model model, @ModelAttribute("etat") Etat state,
            @RequestParam Map<String, String> allParams) {
        boolean isDone = dataService.addPatient(allParams);
        if(isDone)
            return "Connexion";
        state.setBadEmail(true); // existe déja
        return "InscriptionUser";
    }

    @PostMapping(path = "/signup/pro")
    public String addPro(Model model, @ModelAttribute("etat") Etat state, @RequestParam Map<String, String> allParams) {
        boolean isDone = dataService.addPro(allParams);
        if(isDone)
            return "Connexion";
        state.setBadEmail(true); // existe déja
        return "InscriptionPro";
    }

    @PostMapping(path = "/login")
    public String loginPro(Model model, @ModelAttribute("etat") Etat state, @RequestParam("Email") String email,
            @RequestParam("Password") String mdp, @RequestParam("pro") String isPro) {
        Etat newState = dataService.loginUser(email, mdp, isPro);
        state = newState;
        if(!state.isConnected() || state.isWrongPass() || state.isBadEmail())
            return "Connexion";
        else
            return "Home"; 
    }

    @PostMapping(path = "/profile") // à voir si doit séparer patient et pro
    public String modifyProfile(Model model, @ModelAttribute("etat") Etat state, @RequestParam Map<String, String> allParams) {
        dataService.modifyProfile(state, allParams);
        return "Profile";
    }
}
