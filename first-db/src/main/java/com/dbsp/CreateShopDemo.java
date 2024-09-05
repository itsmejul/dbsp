package com.dbsp;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.dbsp.entity.Shops;

public class CreateShopDemo {

	public static void main(String[] args) {

		// create session factory
		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(Shops.class)
				.buildSessionFactory();
		// create session
		// Session session = factory.getCurrentSession();
		Session session = factory.getCurrentSession();
		System.out.println("test!");
		Transaction tx = null;
		try {
			// use session object to save Java object

			// create shops object
			Shops tempShops = new Shops("testShop", "teststraasse", 4279);
			System.out.println(tempShops.getId());

			// start a transaction
			tx = session.beginTransaction();
			// save shops object
			session.save(tempShops);
			// save ist veraltet, man soll lieber persist() nehmen
			System.out.println("Saving ...");

			// commit transaction
			// session.flush();
			// chatgpt hat gesagt man soll flush vor commit schreiben
			// session.getTransaction().commit();
			tx.commit();
			System.out.println("Commited!");

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
			factory.close();
		}
	}

}
