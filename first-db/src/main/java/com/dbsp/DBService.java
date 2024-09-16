package com.dbsp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
            configuration.addAnnotatedClass(ItemCategories.class);
            configuration.addAnnotatedClass(Categories.class);
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

    // Fetches a list of products matching the given pattern
    public List<Item> getProducts(String pattern) {
        Session session = null;
        List<Item> products = null;
        try {
            // Open a session
            session = sessionFactory.openSession();

            // Begin a transaction
            session.beginTransaction();

            String hql;
            Query<Item> query;

            // Check if the pattern is null or not
            if (pattern == null) {
                // Fetch all products if pattern is null
                hql = "FROM Item";
                query = session.createQuery(hql, Item.class);
            } else {
                // Fetch products with titles matching the pattern
                hql = "FROM Item WHERE title LIKE :pattern";
                query = session.createQuery(hql, Item.class);
                query.setParameter("pattern", pattern);
            }

            // Execute the query and get the result list
            products = query.getResultList();

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
        return products;
    }

    // Fetches products by category path
    public List<Item> getProductsByCategoryPath(String categoryPath) {
        Session session = null;
        List<Item> products = null;
        try {
            // Open a session
            session = sessionFactory.openSession();
            session.beginTransaction();

            // Split the category path into parts
            String[] categoryNames = categoryPath.split(">");
            Integer parentId = null; // Starting with root category parent ID, assumed to be 0 or NULL

            // Iterate over the category path parts to find the category ID
            Integer finalCategoryId = null;
            for (String categoryName : categoryNames) {
                categoryName = categoryName.trim(); // Remove any leading/trailing whitespace

                // Use HQL to find the category with the given name and parent ID
                Query<Categories> query;
                if (parentId == null) {
                    // Handle root categories with parentId null
                    System.out.print(categoryName);
                    query = session.createQuery(
                            "FROM Categories WHERE trim(title) = :title AND parentId IS NULL", Categories.class); // AND
                                                                                                                  // c.parentId
                                                                                                                  // IS
                    // NULL
                } else {
                    // Handle child categories with a specified parentId
                    query = session.createQuery(
                            "FROM Categories c WHERE c.title = :title AND c.parentId = :parentId", Categories.class);
                    query.setParameter("parentId", parentId);
                }
                query.setParameter("title", categoryName);

                List<Categories> categories = query.getResultList();

                if (categories.isEmpty()) {
                    System.out.println("Category not found: " + categoryName);
                    return null; // Return null if any category in the path is not found
                } else {
                    System.out.println("found");
                }

                Categories category = categories.get(0);
                finalCategoryId = category.getId();
                parentId = finalCategoryId; // Set parentId to the current category's ID for the next iteration
            }

            // Now, finalCategoryId should contain the ID of the last category in the path
            if (finalCategoryId != null) {
                // Query to find all items linked to this category
                Query<Item> itemQuery = session.createQuery(
                        "SELECT i FROM Item i JOIN ItemCategories ic ON i.asin = ic.asin WHERE ic.categoryId = :categoryId",
                        Item.class);
                itemQuery.setParameter("categoryId", finalCategoryId);

                products = itemQuery.getResultList();
            }

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
        return products;
    }

    public void printAllCategoryTitles() {
        Session session = null;
        try {
            // Open a session
            session = sessionFactory.openSession();
            session.beginTransaction();
            String title = "Features";

            // Use HQL to retrieve all categories
            Query<Categories> query = session.createQuery("FROM Categories WHERE trim(title) = :title",
                    Categories.class);
            query.setParameter("title", title.trim());
            List<Categories> categories = query.getResultList();
            int ee = 0;
            // Print out each category title
            System.out.println("All categories in the database:");
            for (Categories category : categories) {
                if (ee < 20) {

                    System.out.println("ID: " + category.getId() + " - Title: " + category.getTitle() + " - Parent ID: "
                            + category.getParentId());

                }
                ee++;

            }

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
