package acme.testing.student.activity;

import acme.entities.activity.Activity;
import acme.entities.enrolment.Enrolment;
import acme.framework.repositories.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StudentActivityTestRepository extends AbstractRepository {

    @Query("select a from Activity a where a.enrolment.student.userAccount.username = :username")
    Collection<Activity> findByUser(String username);

    @Query("select e from Enrolment e where e.isFinished = false and e.student.userAccount.username = :username")
    Collection<Enrolment> findUnfinishedEnrolmentsByStudent(String username);

    @Query("select e from Enrolment e where e.isFinished = true and e.student.userAccount.username = :username")
    Collection<Enrolment> findFinishedEnrolmentsByStudent(String username);
}
