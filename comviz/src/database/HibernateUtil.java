package database;


import org.hibernate.*;
import org.hibernate.cfg.*;

public class HibernateUtil {

    public static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @SuppressWarnings("rawtypes")
	public static final ThreadLocal threadLocal = new ThreadLocal();

    public static Session currentSession() throws HibernateException {
        Session session = (Session) threadLocal.get();
        // Open a new Session, if this thread has none yet
        if (session == null) {
            session = sessionFactory.openSession();
            // Store it in the ThreadLocal variable
            threadLocal.set(session);
        }
        return session;
    }

    public static void closeSession() throws HibernateException {
        Session session = (Session) threadLocal.get();
        if (session != null)
            session.close();
        threadLocal.set(null);
    }
}

