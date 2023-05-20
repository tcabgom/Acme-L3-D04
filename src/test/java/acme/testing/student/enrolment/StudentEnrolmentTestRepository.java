package acme.testing.student.enrolment;

import acme.entities.enrolment.Enrolment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StudentEnrolmentTestRepository {

    Collection<Enrolment> findAll();

    @Query("select e from Enrolment e where e.student.userAccount.username = :username")
    Collection<Enrolment> findByStudentUsername(String student1);

    @Query("select e from Enrolment e where e.student.userAccount.username = :username and e.isFinished = true")
    Collection<Enrolment> findFinalisedByStudentUsername(String student1);
}
