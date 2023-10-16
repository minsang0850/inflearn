package hellojpa.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager(); //데이터베이스 커넥션 받은것과 비슷

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Member member = em.find(Member.class, 150L);
            member.setUsername("ZZZZZ");

            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally {
            em.close();
        }


        em.close();
        emf.close();
    }
}
