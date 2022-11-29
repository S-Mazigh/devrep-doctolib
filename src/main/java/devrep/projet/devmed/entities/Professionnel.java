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
    @Column(nullable = false)
    private int duréeRdv;
    // private String mesCrenauxLibres; // pattern: JJ-MM-AAAA(HH:MM-HH:MM)

    @Basic
    @Column(name="tel", nullable=false, length=16)
    private String numTelephone;

    @Basic
    @Column(name="adresse", nullable=false)
    private String adress;

    public Professionnel() {}
}
