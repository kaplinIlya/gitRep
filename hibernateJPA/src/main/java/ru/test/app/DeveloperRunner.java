package ru.test.app;

import ru.test.app.entity.Developer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Comparator;
import java.util.List;

public class DeveloperRunner {

    private static EntityManager entityManager;

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("persistenceUnit");
        entityManager = entityManagerFactory.createEntityManager();
        DeveloperRunner developerRunner = new DeveloperRunner();

        System.out.println("Adding developer's records to the DB");
        developerRunner.addDeveloper("Proselyte", "Developer", "Java Developer", 2);
        developerRunner.addDeveloper("Some", "Developer", "C++ Developer", 2);
        developerRunner.addDeveloper("Peter", "UI", "UI Developer", 4);

        System.out.println("List of developers");
        developerRunner.listDevelopers().stream()
                .sorted(Comparator.comparing(Developer::getId))
                .forEach(s-> System.out.println(s));

        System.out.println("===================================");
        System.out.println("Removing Some Developer and updating Proselyte");
        List<Developer> developers = developerRunner.listDevelopers();
        developerRunner.updateDeveloper(1, 3);
        developerRunner.removeDeveloper(3);

        System.out.println("Final list of developers");
        developerRunner.listDevelopers().stream().forEach(s-> System.out.println(s));
        System.out.println("===================================");
        System.exit(0);
    }

    public void addDeveloper(String firstName, String lastName, String specialty, int experience) {
        entityManager.getTransaction().begin();
        Developer developer = new Developer(firstName, lastName, specialty, experience);
        entityManager.persist(developer);
        entityManager.getTransaction().commit();
    }

    public List<Developer> listDevelopers() {
        return entityManager.createQuery("from Developer", Developer.class).getResultList();
    }

    public void updateDeveloper(int developerId, int experience) {
        entityManager.getTransaction().begin();
        Developer developer = entityManager.find(Developer.class, developerId);
        developer.setExperience(experience);
        entityManager.persist(developer);
        entityManager.getTransaction().commit();
    }

    public void removeDeveloper(int developerId) {
        entityManager.getTransaction().begin();
        Developer developer = entityManager.find(Developer.class, developerId);
        entityManager.remove(developer);
        entityManager.getTransaction().commit();
    }
}
