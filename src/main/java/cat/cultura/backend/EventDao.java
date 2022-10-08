package cat.cultura.backend;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao {

    @Autowired
    private SessionFactory sessionFactory;
    public void createEvent(Event edv){
        Session session= null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Integer id =(Integer) session.save(edv);
            System.out.println("Event created with Id::" + id);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
