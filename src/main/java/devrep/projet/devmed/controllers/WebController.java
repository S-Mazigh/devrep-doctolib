package devrep.projet.devmed.controllers;

import java.util.ArrayList;
import java.util.List;
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
import devrep.projet.devmed.entities.RendezVous;
import devrep.projet.devmed.entities.Utilisateur;
import devrep.projet.devmed.service.AppointmentService;
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

    @Autowired
    private AppointmentService rdvService;

    /*
     * va toujours être appelé avant les requestmapping (get, post...)
     * et rajoute l'objet de retour au modèle
     */
    @ModelAttribute(name = "etat")
    public Etat getEtat() {
        Etat state = new Etat();
        return state;
    }

    @GetMapping(path = { "/home" })
    public String getHomeAllPro(Model model) {
        // System.err.println("Connexion: "+model.getAttribute("etat"));
        model.addAttribute("listePro", searchService.getProByDomaineOrName("", ""));
        return "Home";
    }

    @GetMapping(path = "home/search")
    public String getHomeSearch(Model model) {
        return "Home";
    }

    @PostMapping(path = "/home/search")
    public String getSearchPro(Model model, RedirectAttributes redirect,
            @RequestParam("searchWords") String searchWords,
            @RequestParam(name = "searchWhere", required = false) String searchWhere,
            @ModelAttribute("etat") Etat state) {
        // il faut les ajouté au redirect vu qu'on les affiche dans le get
        // on utilise le addFlash au lieu du addAttribute pour eviter d'envoyer
        // l'attribut en le serialisant, il vaut mieux le garder dans la flash map
        redirect.addFlashAttribute("listePro", searchService.getProByDomaineOrName(searchWords, searchWhere));
        // System.err.println("Results: "+redirect.getAttribute("listePro"));
        // System.err.println("Search: "+model.getAttribute("etat"));

        return "redirect:/home/search";
    }

    @GetMapping(path = "/home/search/{domaine}")
    public String getSearchDomaine(Model model, RedirectAttributes redirect,
            @PathVariable("domaine") String searchWords,
            @ModelAttribute("etat") Etat state) {
        // il faut les ajouté au redirect vu qu'on les affiche dans home et non dans
        // home/search
        redirect.addFlashAttribute("listePro", searchService.getProByDomaine(searchWords, ""));
        // System.err.println("Results: "+redirect.getAttribute("listePro"));
        // System.err.println("Search: "+model.getAttribute("etat"));

        return "redirect:/home/search";
    }

    /*
     * @GetMapping(path ="/categories")
     * public String getCategories(Model model) {
     * //System.err.println("Categories: "+model.getAttribute("etat"));
     * return "Categories";
     * }
     */

    @GetMapping(path = "/login")
    public String getLogin(Model model, @ModelAttribute("etat") Etat state) {
        if (state.isConnected())
            return "redirect:/home";
        // System.err.println("Connexion: "+model.getAttribute("etat"));
        return "Connexion";
    }

    @GetMapping(path = "/login-error")
    public String getError(HttpServletRequest request, Model model, @ModelAttribute("etat") Etat state) {
        if (state.isConnected())
            return "redirect:/home";
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
    public String getSignUpPro(Model model, @ModelAttribute("etat") Etat state) {
        if (state.isConnected())
            return "redirect:/home";
        // System.err.println("Inscription pro: "+model.getAttribute("etat"));
        return "InscriptionPro";
    }

    @GetMapping(path = "/signup/patient")
    public String getSignUpPatient(Model model, @ModelAttribute("etat") Etat state) {
        if (state.isConnected())
            return "redirect:/home";
        System.err.println("Inscription patient: " + model.getAttribute("etat"));
        return "InscriptionUser";
    }

    @PostMapping(path = "/signup/patient")
    public String addPatient(Model model, @ModelAttribute("etat") Etat state,
            @RequestParam Map<String, String> allParams) {
        boolean isDone = dataService.addPatient(allParams);
        if (isDone)
            return "redirect:/login"; // sans redirect il continuera sur le meme path (i.e. /signup/login) ce qui veut
                                      // rien dire.
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

    @GetMapping(path = "/profile/public/{id}")
    public String getAProfile(Model model, @PathVariable(name = "id") Long id) {
        // Le Not found est gére par thymeleaf avec un if.
        Utilisateur theOther = searchService.getById(id);
        if (!theOther.equals(null) && theOther.getAuthority().equals("PRO")) {
            model.addAttribute("theOther", theOther);
            model.addAttribute("disponibilities", rdvService.getDisponibilities(theOther.getMesHoraires(), theOther));
            // to call group we must first call .matches
            // model.addAttribute("dateTimeRegex", Pattern.compile("(?<date>\\w+
            // \\d{2}/\\d{2}/\\d{4}) (?<hour>\\d{2}:\\d{2})")); // group 1 Jour dd/MM/yyyy,
            // group 2 HH:mm
        } else {
            return "redirect:/home";
        }
        // System.err.println("MyProfile: "+model.getAttribute("etat"));
        return "PubProfile";
    }

    @GetMapping(path = "/profile/infPerso")
    public String getMyProfile(Model model) {
        Etat current = (Etat) model.getAttribute("etat");
        if (current != null) {
            if (!current.isConnected())
                return "redirect:/login";
            model.addAttribute("meshoraires", AppointmentService.formatHoraires(current.getWho().getMesHoraires()));
        }
        return "ProfileInfPerso";
    }

    @GetMapping(path = "/profile/infPerso/delete")
    public String delMyProfile(Model model, @ModelAttribute("etat") Etat state) {
        if (!state.isConnected())
            return "redirect:/login";
        // del tous les rdv pris par cette utilisateur ou prévu pour ce pro
        // System.out.println("Suppression Initiée!!!");
        List<RendezVous> toDelete = new ArrayList<>();
        toDelete = rdvService.getRdvByUser(state.getWho());
        rdvService.delAllRdv(toDelete);
        dataService.deleteProfile(state.getWho());
        return "redirect:/logout-all";
    }

    @GetMapping(path = "/profile/rdv")
    public String getMyRdv(Model model, @ModelAttribute("etat") Etat state) {
        if (!state.isConnected())
            return "redirect:/login";
        model.addAttribute("rdv", rdvService.getRdv(state.isPro(), state.getWho()));

        // System.err.println("MyProfile: "+model.getAttribute("etat"));
        return "ProfileRdv";
    }

    @PostMapping(path = "/profile/rdv/delete/{id}")
    public String delAnRdv(Model model, @PathVariable(name = "id") long rdvId) {
        rdvService.delRdv(rdvId);
        return "redirect:/profile/rdv";
    }

    @PostMapping(path = "/profile/modify") // à voir si doit séparer patient et pro
    public String modifyProfile(Model model, @ModelAttribute("etat") Etat state,
            @RequestParam Map<String, String> allParams, RedirectAttributes redirect) {
        if (state.isConnected()) {
            return "redirect:/login";
        }
        if (allParams.get("delete") != null && allParams.get("delete").equals("yes")) {
            redirect.addFlashAttribute(state);
            return "redirect:/profile/infPerso/delete";
        }
        dataService.modifyProfile(state, allParams);
        return "redirect:/profile/infPerso";
    }

    @PostMapping(path = "/appointment/take")
    public String takeRdv(Model model, @RequestParam("pro") Long pro, @RequestParam("dateRdv") String date,
            @ModelAttribute("etat") Etat state) {
        if (!state.isConnected())
            return "redirect:/login";
        rdvService.addRdv(state.getWho().getId(), pro, date);
        return "redirect:/profile/public/" + pro;
    }
}
