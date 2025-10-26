package ma.projet.beans;

import javax.persistence.*;
import java.util.List;

@NamedNativeQuery(
        name = "Femme.nombreEnfantsEntreDates",
        query = "SELECT SUM(m.nbrEnfant) AS total " +
                "FROM mariage m " +
                "WHERE m.femme_id = ?1 " +
                "AND m.dateDebut <= ?3 " +                  // commence avant la fin
                "AND (m.dateFin IS NULL OR m.dateFin >= ?2)" // finit après le début ou toujours en cours
)
@NamedQuery(
        name = "Femme.marieesDeuxFoisOuPlus",
        query = "SELECT f FROM Femme f WHERE SIZE(f.mariages) >= 2"
)
@Entity
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme", cascade = CascadeType.ALL)
    private List<Mariage> mariages;

    public Femme() {}


    public List<Mariage> getMariages() { return mariages; }
    public void setMariages(List<Mariage> mariages) { this.mariages = mariages; }
}
