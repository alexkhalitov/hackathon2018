package ru.sbrf.hackaton.telegram.bot;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {

    public static void main(String[] args) {
            try (StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build()) {
                Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
                try (SessionFactory sessionFactory = metaData.getSessionFactoryBuilder().build()) {
                    try (Session session = sessionFactory.openSession()) {



                    }
                }
            }
    }
}
