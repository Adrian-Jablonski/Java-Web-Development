package com.example.contactmgr;

import com.example.contactmgr.model.Contact;
import com.example.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


public class Application {
    // Hold a reusable reference to a SessionFactory (Since we need only one)
    //private static final SessionFactory sessionFactory = buildSessionFactory();

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
    }
}