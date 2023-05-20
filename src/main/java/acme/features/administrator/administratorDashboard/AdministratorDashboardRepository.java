
package acme.features.administrator.administratorDashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.entities.bulletin.Bulletin;
import acme.entities.configuration.Configuration;
import acme.entities.lecture.Course;
import acme.entities.lecture.Lecture;
import acme.entities.note.Note;
import acme.entities.offer.Offer;
import acme.entities.peep.Peep;
import acme.framework.components.accounts.Administrator;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

public interface AdministratorDashboardRepository extends AbstractRepository {

	@Query("select count(s) from Lecture s ")
	Integer findNumOfLecturers();

	@Query("select count(s) from Assistant s ")
	Integer findNumOfAssistants();

	@Query("select count(s) from Administrator s ")
	Integer findNumOfAdministrators();

	@Query("select count(s) from Auditor s ")
	Integer findNumOfAuditors();

	@Query("select count(s) from Student s ")
	Integer findNumOfStudents();

	@Query("select count(s) from Company s ")
	Integer findNumOfCompanys();

	@Query("select p from Peep p ")
	Collection<Peep> findAllPeeps();

	@Query("select b from Bulletin b ")
	Collection<Bulletin> findAllBulletins();

	@Query("select o from Offer o ")
	Collection<Offer> findAllOffers();

	@Query("select n from Note n ")
	Collection<Note> findAllNotes();

	@Query("select a from Administrator a where a.userAccount.id = :id")
	Administrator findOneAdministratorByUserAccountId(int id);

	@Query("select c from Configuration c ")
	List<Configuration> findConfiguration();

	@Query("select l from Lecture l where l.lecturer = :lecturer")
	Collection<Lecture> findLecturesByLecturer(Lecturer lecturer);

	@Query("select c from Course c where c.lecturer.id = :id")
	Collection<Course> findManyCoursesByLecturerId(int id);

	@Query("select l from Lecture l join LecturesInCourse lc on lc.course.id = :id and lc.lecture.id = l.id")
	Collection<Lecture> findManyLecturesByCourseId(int id);

}
