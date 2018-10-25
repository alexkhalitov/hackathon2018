package ru.sbrf.hackaton.telegram.bot.dataprovider.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class HibernateSessionFactory {

  private SessionFactory sessionFactory;

  @PostConstruct
  public void init() {
    StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
    sessionFactory = metaData.getSessionFactoryBuilder().build();
  }

  @PreDestroy
  public void shutdown() {
    if(sessionFactory != null) {
      try{
        sessionFactory.close();
      }catch (Exception ignored){}
    }
  }

  public Session openSession() {
    return sessionFactory.openSession();
  }
}
