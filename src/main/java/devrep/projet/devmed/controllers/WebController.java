package devrep.projet.devmed.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
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

    @GetMapping(path = "/home")
    public String getHome(Model model) {
        System.out.println("Home: "+model.getAttribute("etat"));
        return "Home";
    }

    @GetMapping(path ="/categories")
    public String getCategories(Model model) {
        System.out.println("Categories: "+model.getAttribute("etat"));
        return "Categories";
    }

    @GetMapping(path = "/login")
    public String getLogin(Model model) {
        System.out.println("Connexion: "+model.getAttribute("etat"));
        return "Connexion";
    }

    @GetMapping(path="/profile")
    public String getProfile(Model model) {
        System.out.println("Profile: "+model.getAttribute("etat"));
        return "Profile";
    }

    @GetMapping(path = "/login-error")
    public String getError(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            // A améliorer pour differencier mauvais mdp ou mauvaise email
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "Connexion";
    }

    @GetMapping(path = "/signup/pro")
    public String getSignUpPro(Model model) {
        System.out.println("Inscription pro: "+model.getAttribute("etat"));
        return "InscriptionPro";
    }

    @GetMapping(path = "/signup/patient")
    public String getSignUpPatient(Model model) {
        System.out.println("Inscription patient: "+model.getAttribute("etat"));
        return "InscriptionUser";
    }

    // RequestMappings
    @GetMapping(path = "/home/search")
    public String getSearchPro(Model model, @RequestParam("searchField") String searchField,
            @RequestParam(name = "searchWhere", required = false) String searchWhere,
            @ModelAttribute("etat") Etat state) {
        model.addAttribute("listePro", searchService.getProByName(searchField, searchWhere));
        model.addAttribute("listeProDomaine", searchService.getProByDomaine(searchField, searchWhere));
        System.out.println("Search: "+model.getAttribute("etat"));
        return "Home";
    }

    @PostMapping(path = "/signup/patient")
    public String addPatient(Model model, @ModelAttribute("etat") Etat state,
            @RequestParam Map<String, String> allParams) {
        // System.out.println("ON PASSE");
        boolean isDone = dataService.addPatient(allParams);
        if (isDone)
            return "redirect:/login"; // sans redirect il continuera sur le meme path (i.e. /signup/login) ce qui veut rien dire.
        state.setBadEmail(true); // existe déja
        return "redirect:/signup/patient";
    }

    @PostMapping(path = "/signup/pro")
    public String addPro(Model model, @ModelAttribute("etat") Etat state, @RequestParam Map<String, String> allParams) {
        boolean isDone = dataService.addPro(allParams);
        if (isDone)
            return "redirect:/login";
        state.setBadEmail(true); // existe déja
        return "redirect:/signup/pro";
    }

    /*
     * @PostMapping(path = "/login")
     * public String loginPro(Model model, @ModelAttribute("etat") Etat
     * state, @RequestParam("Email") String email,
     * 
     * @RequestParam("Password") String mdp, @RequestParam("pro") String isPro) {
     * Etat newState = dataService.loginUser(email, mdp, isPro);
     * state = newState;
     * if(!state.isConnected() || state.isWrongPass() || state.isBadEmail())
     * return "Connexion";
     * else
     * return "Home";
     * }
     */

    @PostMapping(path = "/profile") // à voir si doit séparer patient et pro
    public String modifyProfile(Model model, @ModelAttribute("etat") Etat state,
            @RequestParam Map<String, String> allParams) {
        dataService.modifyProfile(state, allParams);
        return "Profile";
    }
}
