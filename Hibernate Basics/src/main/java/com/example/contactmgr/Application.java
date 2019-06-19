package com.example.contactmgr;

import com.example.contactmgr.model.Contact;
import com.example.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;


public class Application {
    // Hold a reusable reference to a SessionFactory (Since we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();  // loads hibernate config file from default location in resources folder (hibernate.cfg.xml)
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();     // builds the session factory. Includes the configuration of all our mappings and includes the configuration for how to connect to the database
    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Adrian", "Jablonski")
                .withEmail("adrian@jab.com")
                .withPhone(4445558899L)
                .build();

        System.out.println(contact);

        int id = save(contact);

        //Display list of contacts
//        for (Contact c : fetchAllContacts()) {
//            System.out.println(c);
//        }
        System.out.printf("%n%n Before Update %n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        // Get the persisted contact
        Contact c = findContactById(id);

        // Update the contact
        c.setFirstName("A");

        // Persist the changes
        System.out.printf("%n%n Updating %n%n");
        update(c);
        System.out.printf("%n%n Update Complete %n%n");

        // Display a list of contacts after the update
        System.out.printf("%n%n After Update %n%n");
        fetchAllContacts().stream().forEach(System.out::println);
    }

    private static Contact findContactById(int id) {
        //Open a session
        Session session = sessionFactory.openSession();
        //Retrieve the persistent object (or null if not found)
        Contact contact = session.get(Contact.class, id);

        // Close the session
        session.close();

        // Return the object
        return contact;
    }

    private static void update(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to update the contact
        session.update(contact);

        // Commit the changes
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    private static List<Contact> fetchAllContacts() {
        // Open a session
        Session session = sessionFactory.openSession();

        //Create Criteria
        CriteriaQuery<Contact> criteriaQuery = session.getCriteriaBuilder().createQuery(Contact.class);
        criteriaQuery.from(Contact.class);

        // Get a list of Contact objects according to the Criteria object
        List<Contact> contacts = session.createQuery(criteriaQuery).getResultList();

        // Close session
        session.close();

        return contacts;
    }

    private static int save(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        int id = (int)session.save(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close session
        session.close();

        return id;
    }
}