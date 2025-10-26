package ma.projet.test;

import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.classes.EmployeTache;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(HibernateUtil.class);

        EmployeService employeService = context.getBean(EmployeService.class);
        EmployeTacheService employeTacheService = context.getBean(EmployeTacheService.class);
        ProjetService projetService = context.getBean(ProjetService.class);
        TacheService tacheService = context.getBean(TacheService.class);

        // ---------- Employés ----------
        Employe emp1 = new Employe();
        emp1.setNom("Koubichate");
        emp1.setPrenom("Jouhayna");
        employeService.create(emp1);

        Employe emp2 = new Employe();
        emp2.setNom("Allali");
        emp2.setPrenom("Rajae");
        employeService.create(emp2);

        // ---------- Projets ----------
        Projet proj1 = new Projet();
        proj1.setNom("Gestion EPI");
        proj1.setChef(emp1);
        proj1.setDateDebut(LocalDate.of(2025, 7, 1));
        proj1.setDateFin(LocalDate.of(2025, 12, 15));
        projetService.create(proj1);

        Projet proj2 = new Projet();
        proj2.setNom("Plateforme RH");
        proj2.setChef(emp2);
        proj2.setDateDebut(LocalDate.of(2025, 8, 10));
        proj2.setDateFin(LocalDate.of(2025, 11, 20));
        projetService.create(proj2);

        // ---------- Tâches ----------
        Tache t1 = new Tache();
        t1.setNom("Planification EPI");
        t1.setProjet(proj1);
        t1.setPrix(450f);
        t1.setDateDebut(LocalDate.of(2025, 7, 10));
        t1.setDateFin(null);
        tacheService.create(t1);

        Tache t2 = new Tache();
        t2.setNom("Interface RH");
        t2.setProjet(proj2);
        t2.setPrix(900f);
        t2.setDateDebut(LocalDate.of(2025, 8, 18));
        t2.setDateFin(null);
        tacheService.create(t2);

        Tache t3 = new Tache();
        t3.setNom("Audit EPI");
        t3.setProjet(proj1);
        t3.setPrix(1800f);
        t3.setDateDebut(LocalDate.of(2025, 9, 3));
        t3.setDateFin(LocalDate.of(2025, 9, 25));
        tacheService.create(t3);

        Tache t4 = new Tache();
        t4.setNom("Reporting EPI");
        t4.setProjet(proj1);
        t4.setPrix(2200f);
        t4.setDateDebut(LocalDate.of(2025, 10, 1));
        t4.setDateFin(LocalDate.of(2025, 10, 4));
        tacheService.create(t4);

        // ---------- Affectations ----------
        EmployeTache et1 = new EmployeTache();
        et1.setEmploye(emp1);
        et1.setTache(t1);
        employeTacheService.create(et1);

        EmployeTache et2 = new EmployeTache();
        et2.setEmploye(emp1);
        et2.setTache(t3);
        employeTacheService.create(et2);

        EmployeTache et3 = new EmployeTache();
        et3.setEmploye(emp2);
        et3.setTache(t2);
        employeTacheService.create(et3);

        EmployeTache et4 = new EmployeTache();
        et4.setEmploye(emp1);
        et4.setTache(t4);
        employeTacheService.create(et4);

        // ---------- Affichages ----------
        System.out.println("\n--- Tâches d'un employé ---");
        employeService.afficherTachesParEmploye(emp1.getId());

        System.out.println("\n--- Projets gérés par l'employé ---");
        employeService.afficherProjetsGeresParEmploye(emp1.getId());

        System.out.println("\n--- Tâches planifiées pour le projet ---");
        projetService.afficherTachesPlanifieesParProjet(proj1.getId());

        System.out.println("\n--- Tâches réalisées pour le projet ---");
        projetService.afficherTachesRealiseesParProjet(proj1.getId());

        System.out.println("\n--- Tâches prix > 1000 ---");
        tacheService.afficherTachesPrixSuperieurA1000();

        System.out.println("\n--- Tâches réalisées entre deux dates ---");
        LocalDate debut = LocalDate.of(2025, 9, 1);
        LocalDate fin = LocalDate.of(2025, 10, 10);
        tacheService.afficherTachesEntreDates(debut, fin);

        context.close();
    }
}
