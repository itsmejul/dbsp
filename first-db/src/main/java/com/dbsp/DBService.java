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

import jakarta.persistence.TypedQuery;

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
            configuration.addAnnotatedClass(Actor.class);
            configuration.addAnnotatedClass(Artist.class);
            configuration.addAnnotatedClass(Audiotext.class);
            configuration.addAnnotatedClass(Author.class);
            configuration.addAnnotatedClass(Bookspec.class);
            configuration.addAnnotatedClass(Categories.class);
            configuration.addAnnotatedClass(Creator.class);
            configuration.addAnnotatedClass(Customer.class);
            configuration.addAnnotatedClass(CustomerBuyItem.class);
            configuration.addAnnotatedClass(Director.class);
            configuration.addAnnotatedClass(Dvdspec.class);
            configuration.addAnnotatedClass(Item.class);
            configuration.addAnnotatedClass(ItemCategories.class);
            configuration.addAnnotatedClass(ItemCategoryId.class);
            configuration.addAnnotatedClass(Labels.class);
            configuration.addAnnotatedClass(Lists.class);
            configuration.addAnnotatedClass(Musicspec.class);
            configuration.addAnnotatedClass(Price.class);
            configuration.addAnnotatedClass(ProductReviews.class);
            configuration.addAnnotatedClass(Publishers.class);
            configuration.addAnnotatedClass(Shops.class);
            configuration.addAnnotatedClass(SimProducts.class);
            configuration.addAnnotatedClass(Studios.class);
            configuration.addAnnotatedClass(Tracks.class);
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
                            "FROM Categories WHERE trim(title) = :title AND parentId IS NULL AND c.parentId IS NULL",
                            Categories.class); // AND
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

    // ab hier noch nicht getestet, lg Simon
    public List<Item> getTopProducts(int k) {
        // Items unter den Top k Ratings
        Session session = null;
        Transaction transaction = null;
        List<Item> products = null;
        try {
            // Open a session and begin transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // HQL query to get the top k products ordered by avg_review_score in descending
            // order
            String hql = "FROM Item i ORDER BY i.avg_review_score DESC";
            // das ist Hibernate Query Language HQL
            TypedQuery<Item> query = session.createQuery(hql, Item.class);
            query.setMaxResults(k);

            // Execute the query and get the results
            products = query.getResultList();

            // Commit the transaction
            transaction.commit();
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

    public List<Item> getSimilarCheaperProduct(String asin) {
        Session session = null;
        Transaction transaction = null;
        List<Item> cheaperSimilarItems = null;

        try {
            // Open session and begin transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // HQL query to get the price of the original product from the Price table
            String originalProductPriceQuery = "SELECT p.price_value FROM Price p WHERE p.asin = :asin";
            TypedQuery<Integer> originalProductPrice = session.createQuery(originalProductPriceQuery, Integer.class);
            originalProductPrice.setParameter("asin", asin);
            int originalPrice = originalProductPrice.getSingleResult(); // Get the original product price

            // HQL query to get similar products that have a lower price than the original
            // product
            String hql = """
                        SELECT i FROM Item i
                        WHERE i.asin IN (
                            SELECT sp.asin_similar FROM SimProducts sp WHERE sp.asin_original = :asin
                        )
                        AND i.asin IN (
                            SELECT p.asin FROM Price p WHERE p.price_value < :originalPrice
                        )
                    """;
            TypedQuery<Item> query = session.createQuery(hql, Item.class);
            query.setParameter("asin", asin);
            query.setParameter("originalPrice", originalPrice);

            // Execute the query and get the result list
            cheaperSimilarItems = query.getResultList();

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return cheaperSimilarItems;
    }

    public void addNewReview(String asin, int rating, int helpful, String reviewDate, int customerId, String summary,
            String content) {
        Session session = null;
        Transaction transaction = null;

        try {
            // Open session and begin transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Create new ProductReviews object
            ProductReviews newReview = new ProductReviews(asin, rating, helpful, reviewDate, customerId, summary,
                    content);

            // Save the review in the database
            session.save(newReview);

            // Commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<ProductReviews> showReviews(String asin) {
        Session session = null;
        Transaction transaction = null;
        List<ProductReviews> reviews = null;

        try {
            // Open session and begin transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // HQL query to get all reviews for the given ASIN
            String hql = "FROM ProductReviews r WHERE r.asin = :asin";
            TypedQuery<ProductReviews> query = session.createQuery(hql, ProductReviews.class);
            query.setParameter("asin", asin);

            // Execute the query and get the result list
            reviews = query.getResultList();

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return reviews;
    }

    public List<Customer> getTrolls(double averageRating) {
        Session session = null;
        Transaction transaction = null;
        List<Customer> trolls = null;

        try {
            // Open session and begin transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // HQL query to find customers whose average rating is below the specified
            // averageRating
            String hql = "SELECT c FROM Customer c WHERE " +
                    "(SELECT AVG(r.rating) FROM ProductReviews r WHERE r.customerId = c.id) < :averageRating";
            TypedQuery<Customer> query = session.createQuery(hql, Customer.class);
            query.setParameter("averageRating", averageRating);

            // Execute the query and get the result list
            trolls = query.getResultList();

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return trolls;
    }

    public List<Price> getOffers(String asin) {
        Session session = null;
        Transaction transaction = null;
        List<Price> offers = null;

        try {
            // Open session and begin transaction
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // HQL query to get all price offers for a given ASIN where price_value is
            // greater than 0
            String hql = "FROM Price p WHERE p.asin = :asin AND p.price_value > 0";
            TypedQuery<Price> query = session.createQuery(hql, Price.class);
            query.setParameter("asin", asin);

            // Execute the query and get the result list
            offers = query.getResultList();

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return offers;
    }

    // Implementiere weitere Methoden hier, falls benötigt.
}
