package test.hibernate.test;

import org.hibernate.Session;  
import org.hibernate.SessionFactory;  
import org.hibernate.cfg.Configuration;  
import org.hibernate.service.ServiceRegistry;  
import org.hibernate.service.ServiceRegistryBuilder;  
  
public class HibernateMain {  
  
 public static void main(String[] args) {  
      
  Configuration configuration=new Configuration();  
  configuration.configure();  
  ServiceRegistry sr= new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();  
  SessionFactory sf=configuration.buildSessionFactory(sr);  
    
  User2 user1=new User2();  
  user1.setUserName("Arpit");  
  user1.setUserMessage("Hello world from arpit");  
    
  User2 user2=new User2();  
  user2.setUserName("Ankita");  
  user2.setUserMessage("Hello world from ankita");  
  Session ss=sf.openSession();  
  ss.beginTransaction();  
 //saving objects to session  
  ss.save(user1);  
  //ss.save(user2);  
  ss.getTransaction().commit();  
  ss.close();  
    
 }  
  
}  

