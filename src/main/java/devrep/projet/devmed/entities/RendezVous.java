package devrep.projet.devmed.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="rdv")
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @OneToOne
    private Long patientId;

    @ManyToOne
    private Professionnel pro;

    @Temporal(TemporalType.TIMESTAMP)
    private Date daterdv;

    public RendezVous() {}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Professionnel getPro() {
        return pro;
    }

    public void setPro(Professionnel pro) {
        this.pro = pro;
    }

    public Date getDaterdv() {
        return daterdv;
    }

    public void setDaterdv(Date daterdv) {
        this.daterdv = daterdv;
    }
}
