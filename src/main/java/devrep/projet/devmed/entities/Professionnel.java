package devrep.projet.devmed.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Professionnels")
public class Professionnel extends Utilisateur{
    // @Enumerated(EnumType.STRING) // pour garder le nom et non la valeur dans la base de données
    // @Enumerated(EnumType.ORDINAL) // pour garder la valeur et non le nom dans la base de données
    private Domaine monDomaine;

    @Basic
    @Column(name="horaires", nullable = false, length = 500)
    private String mesHoraires; // pattern: JOUR(HH:MM-HH:MM);...

    @Basic
    @Column(name="dureeRdv", nullable = false)
    private int duréeRdv;
    // private String mesCrenauxLibres; // pattern: JJ-MM-AAAA(HH:MM-HH:MM)

    @Basic
    @Column(name="tel", nullable=false, length=16)
    private String numTelephone;

    @Basic
    @Column(name="adresse", nullable=false)
    private String adress;

    public Professionnel() {}

    public Domaine getMonDomaine() {
        return monDomaine;
    }

    public void setMonDomaine(Domaine monDomaine) {
        this.monDomaine = monDomaine;
    }

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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
