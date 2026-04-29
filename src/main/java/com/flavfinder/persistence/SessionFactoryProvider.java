package com.flavfinder.persistence;

import com.flavfinder.entity.Restaurant;
import com.flavfinder.entity.SavedLocation;
import com.flavfinder.entity.SavedRestaurant;
import com.flavfinder.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * This file provides a SessionFactory for use with DAOs using Hibernate
 *
 * @author paulawaite
 * @version 3.0
 */
public class SessionFactoryProvider {

    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    /**
     * Create session factory.
     */
    public static void createSessionFactory() {
        MetadataSources sources;

        if (System.getenv("MYSQL_URL") != null) {
            // Force the MySQL driver to register before C3P0 spawns its pool threads
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("MySQL JDBC driver not found", e);
            }

            // Railway sets MYSQL_URL as mysql://... — JDBC requires the jdbc: prefix
            String rawUrl = System.getenv("MYSQL_URL");
            String jdbcUrl = rawUrl.startsWith("jdbc:") ? rawUrl : "jdbc:" + rawUrl;

            registry = new StandardServiceRegistryBuilder()
                    .applySetting("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver")
                    .applySetting("hibernate.connection.url",      jdbcUrl)
                    .applySetting("hibernate.connection.username", System.getenv("MYSQLUSER"))
                    .applySetting("hibernate.connection.password", System.getenv("MYSQLPASSWORD"))
                    .applySetting("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
                    .applySetting("hibernate.hbm2ddl.auto", "update")
                    .applySetting("show_sql", "false")
                    .applySetting("hibernate.c3p0.min_size", "2")
                    .applySetting("hibernate.c3p0.max_size", "10")
                    .applySetting("hibernate.c3p0.timeout",  "300")
                    .applySetting("hibernate.c3p0.max_statements", "50")
                    .applySetting("hibernate.c3p0.idle_test_period","3000")
                    .build();
            sources = new MetadataSources(registry);
            sources.addAnnotatedClass(User.class);
            sources.addAnnotatedClass(SavedLocation.class);
            sources.addAnnotatedClass(SavedRestaurant.class);
            sources.addAnnotatedClass(Restaurant.class);
        } else {
            // Local dev: use hibernate.cfg.xml
            registry = new StandardServiceRegistryBuilder().configure().build();
            sources = new MetadataSources(registry);
        }

        Metadata metadata = sources.getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    /**
     * Gets session factory.
     *
     * @return the session factory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            createSessionFactory();
        }
        return sessionFactory;
    }
}
