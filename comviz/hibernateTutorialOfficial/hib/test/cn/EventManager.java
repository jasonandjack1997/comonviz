package hib.test.cn;

import org.hibernate.Transaction;
import org.hibernate.Session;

import java.util.List;

import java.util.Date;

public class EventManager {

	public static void main(String[] args) {
		EventManager mgr = new EventManager();

		if (args[0].equals("store")) {
			mgr.createAndStoreEvent("My Event", new Date());
		} else if (args[0].equals("list")) {
			List events = mgr.listEvents();
			for (int i = 0; i < events.size(); i++) {
				Event theEvent = (Event) events.get(i);
				System.out.println("Event: " + theEvent.getTitle() + " Time: "
						+ theEvent.getDate());
			}
		}

		HibernateUtil.sessionFactory.close();
	}

	private void createAndStoreEvent(String title, Date theDate) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();

		Event theEvent = new Event();
		theEvent.setTitle(title);
		theEvent.setDate(theDate);

		session.save(theEvent);

		tx.commit();
		HibernateUtil.closeSession();
	}

	private List listEvents() {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();

		List result = session.createQuery("from Event").list();

		tx.commit();
		session.close();

		return result;
	}

	private void addPersonToEvent(Long personId, Long eventId) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();

		Person aPerson = (Person) session.load(Person.class, personId);
		Event anEvent = (Event) session.load(Event.class, eventId);

		aPerson.getEvents().add(anEvent);

		tx.commit();
		HibernateUtil.closeSession();
	}

}
