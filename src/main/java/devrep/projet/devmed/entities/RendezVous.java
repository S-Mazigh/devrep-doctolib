package devrep.projet.devmed.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="rdv")
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @ManyToOne
    private Utilisateur patient;

    @ManyToOne
    private Utilisateur pro;

    @Temporal(TemporalType.TIMESTAMP)
    private Date daterdv;

    public RendezVous() {}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Utilisateur getPatient() {
        return patient;
    }

    public void setPatientId(Utilisateur patient) {
        this.patient = patient;
    }

    public Utilisateur getPro() {
        return pro;
    }

    public void setPro(Utilisateur pro) {
        this.pro = pro;
    }

    public Date getDaterdv() {
        return daterdv;
    }

    public void setDaterdv(Date daterdv) {
        this.daterdv = daterdv;
    }
}
