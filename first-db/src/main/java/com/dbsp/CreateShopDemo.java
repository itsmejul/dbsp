package com.dbsp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.dbsp.entity.Shops;

public class CreateShopDemo {

	public static void main(String[] args) {
		
		//create session factory
		SessionFactory factory = new Configuration()
								.configure("hibernate.cfg.xml")
								.addAnnotatedClass(Shops.class)
								.buildSessionFactory();
		//create session
		Session session = factory.getCurrentSession();
		System.out.println("test!");
		try {
			//use session object to save Java object
			
			//create shops object
			Shops tempShops = new Shops("testShop", "testStreet", 4279);
			//start a transaction
			session.beginTransaction();
			//save shops object
			session.save(tempShops);
			System.out.println("Saving ...");
			//commit transaction
			session.getTransaction().commit();
			System.out.println("Commited!");
		} finally {
			factory.close();
		}
	}
	
}
