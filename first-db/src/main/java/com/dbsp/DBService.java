package com.dbsp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.dbsp.entity.Actor;
import com.dbsp.entity.Artist;
import com.dbsp.entity.Audiotext;
import com.dbsp.entity.Author;
import com.dbsp.entity.Bookspec;
import com.dbsp.entity.Categories;
import com.dbsp.entity.Creator;
import com.dbsp.entity.Customer;
import com.dbsp.entity.CustomerBuyItem;
import com.dbsp.entity.Director;
import com.dbsp.entity.Dvdspec;
import com.dbsp.entity.Item;
import com.dbsp.entity.ItemCategories;
import com.dbsp.entity.ItemCategoryId;
import com.dbsp.entity.Labels;
import com.dbsp.entity.Lists;
import com.dbsp.entity.Musicspec;
import com.dbsp.entity.Price;
import com.dbsp.entity.ProductReviews;
import com.dbsp.entity.Publishers;
import com.dbsp.entity.Shops;
import com.dbsp.entity.SimProducts;
import com.dbsp.entity.Studios;
import com.dbsp.entity.Tracks;
import com.dbsp.extra.Category;

import jakarta.persistence.TypedQuery;

public class DBService implements AppInterface {

    private SessionFactory sessionFactory;

    @Override
    public void init() {
        Properties dbProperties = new Properties();
        try {

            // Hier werden die Parameter für die Initialisierung aus der db.properties-Datei
            // geladen
            InputStream input = DBService.class.getClassLoader().getResourceAsStream("db.properties");
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find db.properties");
            }

            // Lade die gelesene Property-Datei in das Properties-Objekt
            dbProperties.load(input);

            // Prüfe, dass keine wichtigen properties fehlen
            if (dbProperties.getProperty("hibernate.dialect") == null) {
                throw new RuntimeException("Missing required property: hibernate.dialect");
            }
            if (dbProperties.getProperty("hibernate.connection.url") == null) {
                throw new RuntimeException("Missing required property: hibernate.connection.url");
            }

            // Erstelle eine Hibernate-Konfiguration
            Configuration configuration = new Configuration();
            // Setze die geladenen Properties für die Hibernate-Konfiguration
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

            // Erstelle die SessionFactory und speichere sie in der Klasse, damit die
            // anderen Methoden sie benutzen können
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

    // Finish-Methode, die alle Datenbankobjekte kontrolliert freigibt
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

    public Category getCategoryTree() {
        // Wir haben eine neue Klasse "Category" erstellt, die für jede Category den
        // Namen und eine Liste an Subcategories speichert
        // Erst werden alle "Categories"-Instanzen aus der DB in eine Liste gelesen (die
        // nur die parentID speichern), und dann mit einer
        // Hilfsmethode in die Baumstruktur aus "Category"-Instanzen konvertiert
        Session session = null;
        List<Categories> categoriesList = null;

        try {
            // Open a session and begin a transaction
            session = sessionFactory.openSession();
            session.beginTransaction();

            // Zuerst werden alle Categories in eine Liste geladen, damit man einfacher den
            // Baum generieren kann
            Query<Categories> query = session.createQuery("FROM Categories", Categories.class);
            categoriesList = query.list();

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

        // Sollte es keine Categorien geben, returne eine Leere Category
        if (categoriesList == null || categoriesList.isEmpty()) {
            return new Category("TopCategory");
        }

        // Mittels Hilfsmethode aus der Liste den Baum generieren
        return buildTreeFromCategories(categoriesList);
    }

    private Category buildTreeFromCategories(List<Categories> categoriesList) {

        // Mappe jede CategoriesID auf eine Category-Instanz (für jede ID wird eine neue
        // Category-Instanz erstellt)
        // Die Instanzen müssen hier schon alle erstellt werden, da in der DB die
        // Kategorien vielleicht nicht in der richtigen Reihenfolge sind (Kindern
        // könnten vor ihren Eltern in der Liste stehen)
        Map<Integer, Category> categoryMap = new HashMap<>();

        for (Categories dbCategory : categoriesList) {
            categoryMap.put(dbCategory.getId(), new Category(dbCategory.getTitle()));
        }

        // Da gefordert war, die Wurzelkategorie auszugeben, aber es mehrere Top-Level
        // Kategorien gibt, haben wir einen künstlichen Wurzelknoten "TopCategory"
        // erstellt. Alle echten Top-Level Kategorien sind dann die Kinder dieses
        // Wurzelknotens
        Category topCategory = new Category("TopCategory");

        // Jetzt iterieren wir durch alle Kategorien und überprüfen, ob ihre parentID
        // null ist
        for (Categories dbCategory : categoriesList) {
            Integer parentId = dbCategory.getParentId();

            // Wenn parentID null ist, ist es eine Top-Level Kategorie und wird ein Kind der
            // TopCategory
            if (parentId == null) {
                topCategory.addSubCategory(categoryMap.get(dbCategory.getId()));
            } else {
                // Ansonsten, suche aus der Map anhand der parentID die passende
                // Parent-Kategorie und füge die aktuelle Kategorie als deren SubCategory hinzu
                Category parentCategory = categoryMap.get(parentId);
                if (parentCategory != null) {
                    parentCategory.addSubCategory(categoryMap.get(dbCategory.getId()));
                }
            }
        }

        return topCategory;
    }

    public List<Item> getProductsByCategoryPath(String categoryPath) {

        Session session = null;
        List<Item> items = new ArrayList<>();

        try {
            // Open a session and begin a transaction
            session = sessionFactory.openSession();
            session.beginTransaction();

            // Die übergebene User-Eingabe in die einzelnen Kategoriennamen splitten
            String[] categoryNames = categoryPath.split(">");

            // Rufe die Hilfsmethode auf und finde so die passende Categories-Instanz
            Categories targetCategory = findCategoryByPath(session, categoryNames);

            if (targetCategory == null) {
                System.out.println("Category not found for the given path: " + categoryPath);
                return items;
                // Wenn die gesuchte Kategorie nicht existiert, returne eine leere Liste
            }

            // Suche aus ItemCategories eine Liste aller ASINs in der gesuchten Kategorie
            Query<String> itemAsinQuery = session.createQuery(
                    "SELECT ic.asin FROM ItemCategories ic WHERE ic.categoryId = :categoryId", String.class);
            itemAsinQuery.setParameter("categoryId", targetCategory.getId());
            // Führe die Query aus
            List<String> asinList = itemAsinQuery.list();

            // Suche zusätzlich den Item-Instanzen anhand ihrer asins (damit Name etc
            // ausgegeben werden können)
            if (!asinList.isEmpty()) {
                Query<Item> itemQuery = session.createQuery(
                        "FROM Item i WHERE i.asin IN (:asinList)", Item.class);
                itemQuery.setParameter("asinList", asinList);
                items = itemQuery.list();
            }

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

        return items;
    }

    private Categories findCategoryByPath(Session session, String[] categoryNames) {
        // Hilfsmethode, die anhand eines Pfades (String[]) die richtige
        // Categories-Instanz auswählt

        // Hilfsvariable, die immer anzeigt, an welcher Stelle des Pfades wir uns gerade
        // befinden (also was gerade die parentCategory ist)
        Categories parentCategory = null;

        for (String categoryName : categoryNames) {
            Query<Categories> categoryQuery;

            if (parentCategory == null) {
                // Wenn parentCategory null ist, ist der aktuelle Name der Name eine Top-Level
                // Kategorie
                // Suche also nach allen Top-Kategorien mit dem passenden Namen
                categoryQuery = session.createQuery(
                        "FROM Categories c WHERE c.title = :categoryName AND c.parentId IS NULL", Categories.class);
            } else {
                // Wenn die aktuelle Kategorie eine Subkategorie ist, steht in parentCategory
                // der Name der Parent Kategorie
                // Suche also nach allen Kinder der parentCategory mit dem passenden Namen
                categoryQuery = session.createQuery(
                        "FROM Categories c WHERE c.title = :categoryName AND c.parentId = :parentId", Categories.class);
                categoryQuery.setParameter("parentId", parentCategory.getId());
            }
            // Trimme Category Name, da evtl Leerzeichen danach angehängt sein könnten
            categoryQuery.setParameter("categoryName", categoryName.trim());

            // Führe die Query aus und speichere die Ergebnisse in einer Liste
            List<Categories> categoryResult = categoryQuery.list();

            if (categoryResult.isEmpty()) {
                // Wenn es keine passende Kategorie ist, returne null
                return null;
            }

            // Sollte es mehrere Kategorien mit gleichem Namen an gleicher Stelle geben,
            // können wir sie nicht unterscheiden und nehmen einfach die erste
            parentCategory = categoryResult.get(0);
        }

        return parentCategory;
    }

    /*
     * // Fetches products by category path
     * public List<Item> getProductsByCategoryPath(String categoryPath) {
     * Session session = null;
     * List<Item> products = null;
     * try {
     * // Open a session
     * session = sessionFactory.openSession();
     * session.beginTransaction();
     * 
     * // Split the category path into parts
     * String[] categoryNames = categoryPath.split(">");
     * Integer parentId = null; // Starting with root category parent ID, assumed to
     * be 0 or NULL
     * 
     * // Iterate over the category path parts to find the category ID
     * Integer finalCategoryId = null;
     * for (String categoryName : categoryNames) {
     * categoryName = categoryName.trim(); // Remove any leading/trailing whitespace
     * 
     * // Use HQL to find the category with the given name and parent ID
     * Query<Categories> query;
     * if (parentId == null) {
     * // Handle root categories with parentId null
     * System.out.print(categoryName);
     * query = session.createQuery(
     * "FROM Categories WHERE trim(title) = :title AND parentId IS NULL",
     * Categories.class); // AND
     * // c.parentId
     * // IS
     * // NULL
     * } else {
     * // Handle child categories with a specified parentId
     * query = session.createQuery(
     * "FROM Categories c WHERE c.title = :title AND c.parentId = :parentId",
     * Categories.class);
     * query.setParameter("parentId", parentId);
     * }
     * query.setParameter("title", categoryName);
     * 
     * List<Categories> categories = query.getResultList();
     * 
     * if (categories.isEmpty()) {
     * System.out.println("Category not found: " + categoryName);
     * return null; // Return null if any category in the path is not found
     * } else {
     * System.out.println("found");
     * }
     * 
     * Categories category = categories.get(0);
     * finalCategoryId = category.getId();
     * parentId = finalCategoryId; // Set parentId to the current category's ID for
     * the next iteration
     * }
     * 
     * // Now, finalCategoryId should contain the ID of the last category in the
     * path
     * if (finalCategoryId != null) {
     * // Query to find all items linked to this category
     * Query<Item> itemQuery = session.createQuery(
     * "SELECT i FROM Item i JOIN ItemCategories ic ON i.asin = ic.asin WHERE ic.categoryId = :categoryId"
     * ,
     * Item.class);
     * itemQuery.setParameter("categoryId", finalCategoryId);
     * 
     * products = itemQuery.getResultList();
     * }
     * 
     * session.getTransaction().commit();
     * 
     * } catch (Exception e) {
     * if (session != null && session.getTransaction().isActive()) {
     * session.getTransaction().rollback();
     * }
     * e.printStackTrace();
     * } finally {
     * if (session != null) {
     * session.close();
     * }
     * }
     * return products;
     * }
     */
    public void printAllCategoryTitles() {
        Session session = null;
        try {
            // Open a session
            session = sessionFactory.openSession();
            session.beginTransaction();
            String title = "Alle SACDs";

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

    public void addNewReview(String asin, Integer rating, Integer helpful, String reviewDate, Integer customerId,
            String summary, String content) {
        if (sessionFactory == null) {
            throw new IllegalStateException("Service not initialized. Call init() before using this method.");
        }
        
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            ProductReviews newReview = new ProductReviews(asin, rating, helpful, reviewDate, customerId, summary, content);
            session.persist(newReview);// Speichert die Review in der Datenbank
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
