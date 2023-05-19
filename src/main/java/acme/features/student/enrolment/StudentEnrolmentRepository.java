
package acme.features.student.enrolment;

import acme.entities.activity.Activity;
import acme.entities.banner.Banner;
import acme.entities.enrolment.Enrolment;
import acme.entities.lecture.Course;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentEnrolmentRepository extends AbstractRepository {

	@Query(value = "SELECT s FROM Student s WHERE s.id = :id")
	Student findStudentById(int id);

	@Query(value = "SELECT e FROM Enrolment e WHERE e.id = :id")
	Enrolment findById(int id);

	@Query(value = "SELECT e FROM Enrolment e WHERE e.code = :code")
    Enrolment findByCode(String code);

	@Query(value = "SELECT e FROM Enrolment e WHERE e.student.id = :studentId")
	Collection<Enrolment> findEnrolmentsByStudentId(int studentId);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findAllPublishedCourses();

	@Query("select a from Activity a where a.enrolment.id = :enrolmentId")
    List<Activity> findActivitiesByEnrolment(int enrolmentId);
}
