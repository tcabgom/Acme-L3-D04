
package acme.features.student.studentDashboard;

import acme.entities.activity.Activity;
import acme.entities.lecture.Course;
import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface StudentDashboardRepository extends AbstractRepository {

	@Query("select s from Student s where s.userAccount.id = :id")
	Student findStudentByUserAccountId(int id);


	@Query("select a from Activity a join a.enrolment e where e.student.id = :studentId")
	Collection<Activity> findActivitiesByStudentId(int studentId);

	@Query("select l from LecturesInCourse lc join lc.lecture l join lc.course c join Enrolment e on e.course = c where e.student.id = :studentId")
	Collection<Lecture> findLecturesByStudentId(int studentId);


}
