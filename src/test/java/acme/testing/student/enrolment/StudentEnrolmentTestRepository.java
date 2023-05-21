package acme.testing.student.enrolment;

import acme.entities.enrolment.Enrolment;
import acme.framework.repositories.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StudentEnrolmentTestRepository extends AbstractRepository {


    @Query("select e from Enrolment e where e.student.userAccount.username = :username")
    Collection<Enrolment> findByStudentUsername(String username);

    @Query("select e from Enrolment e where e.student.userAccount.username = :username and e.isFinished = true")
    Collection<Enrolment> findFinalisedByStudentUsername(String username);
}
