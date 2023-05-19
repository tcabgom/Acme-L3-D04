
package acme.features.student.studentDashboard;

import acme.entities.activity.Activity;
import acme.entities.enumerates.ActivityType;
import acme.entities.lecture.Course;
import acme.entities.lecture.Lecture;
import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.forms.AssistantDashboard;
import acme.forms.Statistics;
import acme.forms.StudentDashboard;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;
import acme.roles.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentDashboardShowService extends AbstractService<Student, StudentDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected StudentDashboardRepository repository;

	// Abstract Service interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		final StudentDashboard object = new StudentDashboard();

		Principal principal = super.getRequest().getPrincipal();
		int studentId = principal.getActiveRoleId();

		//////// Total Number Of Activities per type

		final Collection<Activity> studentActivities = this.repository.findActivitiesByStudentId(studentId);
		final Map<ActivityType, Integer> totalActivitiesByType = studentActivities.stream()
				.collect(Collectors.groupingBy(Activity::getType, Collectors.summingInt(x->1)));


		object.setTotalActivitiesByType(totalActivitiesByType);

		//////// Activity Statistics
		Statistics activityPeriodStatistics = new Statistics();
		Collection<Double> activityPeriodDuration = studentActivities.stream()
				.map(activity -> (double) MomentHelper.computeDuration(activity.getPeriodStart(), activity.getPeriodEnd())
						.toDays())
				.collect(Collectors.toList());

		activityPeriodStatistics.obtainValues(activityPeriodDuration);

		object.setActivityPeriodStatistics(activityPeriodStatistics);

		//////// Lecture Statistics

		Statistics learningTimeStatistics = new Statistics();
		final Collection<Lecture> studentLectures = this.repository.findLecturesByStudentId(studentId);
		System.out.println(studentLectures);
		final Collection<Double> sessionsDuration = studentLectures.stream()
				.map(Lecture::getLearningTime)
				.collect(Collectors.toCollection(ArrayList::new));

		learningTimeStatistics.obtainValues(sessionsDuration);

		object.setLearningTimeStatistics(learningTimeStatistics);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final StudentDashboard object) {

		Tuple tuple;
		tuple = super.unbind(object, "activityPeriodStatistics", "learningTimeStatistics");
		tuple.put("totalNumberOfHandsOnActivities", object.getTotalActivitiesByType().get(ActivityType.HANDS_ON));
		tuple.put("totalNumberOfTheoryActivities", object.getTotalActivitiesByType().get(ActivityType.THEORY));
		tuple.put("totalNumberOfBalancedActivities", object.getTotalActivitiesByType().get(ActivityType.BALANCED));
		super.getResponse().setData(tuple);

	}

}
