package devrep.projet.devmed.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="Patients")
public class Patient extends Utilisateur{
    public Patient(){}
}
