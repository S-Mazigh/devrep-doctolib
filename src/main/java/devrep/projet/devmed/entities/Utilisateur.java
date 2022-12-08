package devrep.projet.devmed.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
// @Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class Utilisateur implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    @Basic
    @Column(name = "prenom", length = 50, nullable = false)
    private String prenom;

    @Basic
    @Column(name = "email", length = 70, nullable = false)
    private String email; // a tester avec un regex ?

    @Basic
    @Column(name = "authority", length = 10, nullable = false)
    private String authority; // GUEST , PATIENT, PRO

    @Basic
    @Column(name = "motdepasse", length = 64, nullable = false)
    private String motDePasse; // encodé et taille mini ?

    // Pour Pro
    // @Enumerated(EnumType.STRING) // pour garder le nom et non la valeur dans la
    // base de données
    // @Enumerated(EnumType.ORDINAL) // pour garder la valeur et non le nom dans la
    // base de données
    @Basic
    @Column(name = "domaine", length = 25, nullable = true)
    private String domaine;

    @Basic
    @Column(name = "horaires", nullable = true, length = 500)
    private String mesHoraires; // pattern: JOUR(HH:MM-HH:MM);...

    @Basic
    @Column(name = "dureeRdv", nullable = true)
    private int duréeRdv;
    // private String mesCrenauxLibres; // pattern: JJ-MM-AAAA(HH:MM-HH:MM)

    @Basic
    @Column(name = "tel", nullable = true, length = 16)
    private String numTelephone;

    @Basic
    @Column(name = "adresse", nullable = true) // Rue, Ville, Pays
    private String adresse;

    @Basic
    @Column(name = "ville", nullable = true) // Rue, Ville, Pays
    private String ville;

    @Basic
    @Column(name = "pays", nullable = true) // Rue, Ville, Pays
    private String pays;

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    // Pour les pro
    public String getMesHoraires() {
        return mesHoraires;
    }

    public void setMesHoraires(String mesHoraires) {
        this.mesHoraires = mesHoraires;
    }

    public int getDuréeRdv() {
        return duréeRdv;
    }

    public void setDuréeRdv(int duréeRdv) {
        this.duréeRdv = duréeRdv;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "Utilisateur [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", authority="
                + authority + ", motDePasse=" + motDePasse + ", domaine=" + domaine + ", mesHoraires=" + mesHoraires
                + ", duréeRdv=" + duréeRdv + ", numTelephone=" + numTelephone + ", adresse=" + adresse + ", ville="
                + ville + ", pays=" + pays + "]";
    }
}
