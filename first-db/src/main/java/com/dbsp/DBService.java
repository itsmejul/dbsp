package com.dbsp;

import java.io.InputStream;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import java.util.Properties;

//import com.dbsp.entity.Shops;
import com.dbsp.entity.*;

public class DBService implements AppInterface {

    private SessionFactory sessionFactory;

    @Override
    public void init() {
        Properties dbProperties = new Properties();
        try {

            // Load the properties from the file
            InputStream input = DBService.class.getClassLoader().getResourceAsStream("db.properties");
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find db.properties");
            }

            // Load properties
            dbProperties.load(input);
            // Validate critical properties
            if (dbProperties.getProperty("hibernate.dialect") == null) {
                throw new RuntimeException("Missing required property: hibernate.dialect");
            }
            if (dbProperties.getProperty("hibernate.connection.url") == null) {
                throw new RuntimeException("Missing required property: hibernate.connection.url");
            }
            // Erstelle eine Hibernate-Konfiguration und setze die Eigenschaften
            Configuration configuration = new Configuration();
            // Set the properties for Hibernate configuration
            configuration.setProperties(dbProperties);

            // Füge die Annotationen-Klasse hinzu
            // TODO alle Klassen hier die notwendig sind
            configuration.addAnnotatedClass(Shops.class);
            configuration.addAnnotatedClass(Item.class);
            // Erstelle die SessionFactory
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize the Database Connection", e);
        }
    }

    // Returns the Hibernate SessionFactory
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Closes all resources properly
    public void finish() {
        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("SessionFactory closed successfully.");
        } else {
            System.out.println("SessionFactory was not initialized or already closed.");
        }
    }

    @Override
    public Item getProduct(String asin) {
        Session session = null;
        Item product = null;
        try {
            // Open a session
            session = sessionFactory.openSession();

            // Begin a transaction
            session.beginTransaction();

            // Fetch the item using the ASIN as the identifier
            product = session.get(Item.class, asin);

            // Commit the transaction
            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return product;
    }

    @Override
    public void addShop(String name, String street, int zip) {
        if (sessionFactory == null) {
            throw new IllegalStateException("Service not initialized. Call init() before using this method.");
        }

        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Shops shop = new Shops(name, street, zip);
            session.persist(shop); // Speichert den Shop in der Datenbank
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    // Implementiere weitere Methoden hier, falls benötigt.
}
