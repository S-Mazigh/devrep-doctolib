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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import devrep.projet.devmed.entities.Etat;
import devrep.projet.devmed.entities.Utilisateur;
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
        System.err.println("Home: "+model.getAttribute("etat"));
        System.err.println("Home: "+model.getAttribute("listePro"));
        return "Home";
    }

    @GetMapping(path ="/categories")
    public String getCategories(Model model) {
        System.err.println("Categories: "+model.getAttribute("etat"));
        return "Categories";
    }

    @GetMapping(path = "/login")
    public String getLogin(Model model) {
        System.err.println("Connexion: "+model.getAttribute("etat"));
        return "Connexion";
    }

    @GetMapping(path="/profile")
    public String getProfile(Model model) {
        System.err.println("Profile: "+model.getAttribute("etat"));
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
        System.err.println("Inscription pro: "+model.getAttribute("etat"));
        return "InscriptionPro";
    }

    @GetMapping(path = "/signup/patient")
    public String getSignUpPatient(Model model) {
        System.err.println("Inscription patient: "+model.getAttribute("etat"));
        return "InscriptionUser";
    }

    // RequestMappings
    @PostMapping(path = "/home/search")
    public String getSearchPro(Model model, RedirectAttributes redirect, @RequestParam("searchWords") String searchWords,
            @RequestParam(name = "searchWhere", required = false) String searchWhere,
            @ModelAttribute("etat") Etat state) {
        // il faut les ajouté au redirect vu qu'on les affiche dans home et non dans home/search
        redirect.addFlashAttribute("listePro", searchService.getProByName(searchWords, searchWhere));
        redirect.addFlashAttribute("listeProDomaine", searchService.getProByDomaine(searchWords, searchWhere));
        System.err.println("Results: "+redirect.getAttribute("listePro"));
        System.err.println("Search: "+model.getAttribute("etat"));
        
        return "redirect:/home";
    }

    @PostMapping(path = "/signup/patient")
    public String addPatient(Model model, @ModelAttribute("etat") Etat state,
            @RequestParam Map<String, String> allParams) {
        // System.err.println("ON PASSE");
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

    @GetMapping(path="/profile/public/{id}")
    public String getAProfile(Model model, @PathVariable(name="id") Long id) {
        // Le Not found est gére par thymeleaf avec un if.
        model.addAttribute("theOther", searchService.getById(id));
        System.err.println("MyProfile: "+model.getAttribute("etat"));
        return "PubProfile";
    }

    @GetMapping(path="/profile")
    public String getMyProfile(Model model) {
        System.err.println("MyProfile: "+model.getAttribute("etat"));
        return "Profile";
    }

    @PostMapping(path = "/profile/modify") // à voir si doit séparer patient et pro
    public String modifyProfile(Model model, @ModelAttribute("etat") Etat state,
            @RequestParam Map<String, String> allParams) {
        dataService.modifyProfile(state, allParams);
        return "redirect:/profile/private";
    }
}
