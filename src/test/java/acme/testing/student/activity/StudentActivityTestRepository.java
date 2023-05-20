package acme.testing.student.activity;

import acme.entities.activity.Activity;
import acme.entities.enrolment.Enrolment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StudentActivityTestRepository {

    @Query("select a from Activity a")
    Collection<Activity> findAll();

    @Query("select a from Activity a where a.enrolment.student.userAccount.username = :username")
    Collection<Activity> findByUser(String username);

    @Query("select e from Enrolment where e.isFinished = false and e.student.userAccount.username = :username")
    Collection<Enrolment> findUnfinishedEnrolmentsByStudent(String username);

    @Query("select e from Enrolment where e.isFinished = true and e.student.userAccount.username = :username")
    Collection<Enrolment> findFinishedEnrolmentsByStudent(String username);
}
