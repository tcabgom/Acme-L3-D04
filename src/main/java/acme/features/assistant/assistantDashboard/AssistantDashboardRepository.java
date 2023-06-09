
package acme.features.assistant.assistantDashboard;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.enumerates.ActivityType;
import acme.entities.lecture.Lecture;
import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Assistant;

@Repository
public interface AssistantDashboardRepository extends AbstractRepository {

	@Query("select a from Assistant a where a.userAccount.id = :id")
	Assistant findOneAssistantByUserAccountId(int id);

	@Query("select count(s) from TutorialSession s where s.tutorial.assistant = :a and s.sessionType = :st")
	Integer findNumOfAssistantSessionsBySessionType(ActivityType st, Assistant a);

	@Query("select t from Tutorial t where t.assistant.id = :id")
	Collection<Tutorial> findManyTutorialsByAssistantId(int id);

	@Query("select ts from TutorialSession ts where ts.tutorial.id = :id")
	Collection<TutorialSession> findManySessionsByTutorialId(int id);

	@Query("select l from Lecture l inner join LecturesInCourse lc on l = lc.lecture inner join Course c on lc.course = c where c.id = :id")
	List<Lecture> findManyLecturesByCourseId(int id);

}
