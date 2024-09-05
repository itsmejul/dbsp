package com.dbsp;

import java.io.InputStream;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import java.util.Properties;

import com.dbsp.entity.Shops;

public class DBService implements AppInterface {

    private SessionFactory sessionFactory;

    @Override
    public void init() {
        Properties dbProperties = new Properties();
        try {

            /*
             * 
             * 
             * // Setze die Eigenschaften aus dem Properties-Objekt
             * configuration.setProperty("hibernate.connection.driver_class",
             * properties.getProperty("hibernate.connection.driver_class"));
             * configuration.setProperty("hibernate.connection.url",
             * properties.getProperty("hibernate.connection.url"));
             * configuration.setProperty("hibernate.connection.username",
             * properties.getProperty("hibernate.connection.username"));
             * configuration.setProperty("hibernate.connection.password",
             * properties.getProperty("hibernate.connection.password"));
             * configuration.setProperty("hibernate.dialect",
             * properties.getProperty("hibernate.dialect"));
             */

            // Load the properties from the file
            InputStream input = DBService.class.getClassLoader().getResourceAsStream("db.properties");
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find db.properties");
            }

            // Load properties
            dbProperties.load(input);
            // Erstelle eine Hibernate-Konfiguration und setze die Eigenschaften
            Configuration configuration = new Configuration();
            // Set the properties for Hibernate configuration
            configuration.setProperties(dbProperties);

            // Füge die Annotationen-Klasse hinzu
            configuration.addAnnotatedClass(Shops.class);

            // Erstelle die SessionFactory
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize the Database Connection", e);
        }
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
