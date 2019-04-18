package ru.test.app;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.test.app.DTO.Developer;

import java.util.List;

public class DeveloperRunner {

    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        DeveloperRunner developerRunner = new DeveloperRunner();
        System.out.println("Adding developer's records to the DB");
        developerRunner.addDeveloper("Proselyte", "Developer", "Java Developer", 2);
        developerRunner.addDeveloper("Some", "Developer", "C++ Developer", 2);
        developerRunner.addDeveloper("Peter", "UI", "UI Developer", 4);

        System.out.println("List of developers");
        developerRunner.listDevelopers().stream().forEach(s-> System.out.println(s));

        System.out.println("===================================");
        System.out.println("Removing Some Developer and updating Proselyte");
        List<Developer> developers = developerRunner.listDevelopers();
        developerRunner.updateDeveloper(1, 3);
        developerRunner.removeDeveloper(2);

        System.out.println("Final list of developers");

        developers = developerRunner.listDevelopers();
        for (Developer developer : developers) {
            System.out.println(developer);
        }
        System.out.println("===================================");
        System.exit(0);
    }

    public void addDeveloper(String firstName, String lastName, String specialty, int experience) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Developer developer = new Developer(firstName, lastName, specialty, experience);
        session.save(developer);
        transaction.commit();
        session.close();
    }

    public List<Developer> listDevelopers() {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        List<Developer> developers = session.createQuery("FROM Developer").list();

        transaction.commit();
        session.close();
        return developers;
    }

    public void updateDeveloper(int developerId, int experience) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Developer developer = (Developer) session.get(Developer.class, developerId);
        try {
            developer.setExperience(experience);
            session.update(developer);
            transaction.commit();
        } catch (Exception e){
            transaction.rollback();
        }  finally {
            session.close();
        }
    }

    public void removeDeveloper(int developerId) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Developer developer = (Developer) session.get(Developer.class, developerId);
        try {
            session.delete(developer);
            transaction.commit();
        } catch (Exception e){
            transaction.rollback();
        }  finally {
            session.close();
        }
    }
}
