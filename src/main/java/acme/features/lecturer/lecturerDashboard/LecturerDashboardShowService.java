
package acme.features.lecturer.lecturerDashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.enumerates.ActivityType;
import acme.entities.lecture.Course;
import acme.entities.lecture.Lecture;
import acme.forms.LecturerDashboard;
import acme.forms.Statistics;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerDashboardShowService extends AbstractService<Lecturer, LecturerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


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
		final LecturerDashboard object = new LecturerDashboard();

		Principal principal;
		int userAccountId;
		int lecturerId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		lecturerId = principal.getActiveRoleId();
		final Lecturer lecturer = this.repository.findOneLecturerByUserAccountId(userAccountId);

		final Map<ActivityType, Integer> lecturesPerType = new HashMap<>();
		for (final ActivityType at : ActivityType.values())
			lecturesPerType.put(at, this.repository.findNumOfLecturerLecturesByType(at, lecturer));
		object.setTotalNumberOfLecturesPerType(lecturesPerType);

		/////////////////////////////////////////////////

		final Collection<Lecture> lectures = new ArrayList<>();

		final Statistics coursesStatistics = new Statistics();
		final Collection<Course> courses = this.repository.findManyCoursesByLecturerId(lecturerId);
		final Collection<Double> coursesDuration = new ArrayList<>();

		for (final Course t : courses) {
			final Collection<Lecture> thislectures = this.repository.findManyLecturesByCourseId(t.getId());
			Double hours = 0.;
			for (final Lecture l : thislectures)
				hours += l.getLearningTime();

			lectures.addAll(lectures);
			coursesDuration.add(hours);
		}

		coursesStatistics.obtainValues(coursesDuration);
		object.setLecturerCourses(coursesStatistics);

		super.getBuffer().setData(object);

		///////////////////////////////////////////////

		final Statistics lecturesStatistics = new Statistics();
		final Collection<Double> lecturesDuration = new ArrayList<>();
		Collection<Lecture> lecturerLectures = new ArrayList<>();
		lecturerLectures = this.repository.findLecturesByLecturer(lecturer);
		for (final Lecture ts : lecturerLectures)
			lecturesDuration.add(ts.getLearningTime());

		lecturesStatistics.obtainValues(lecturesDuration);
		object.setLecturerLectures(lecturesStatistics);
	}

	@Override
	public void unbind(final LecturerDashboard object) {

		Tuple tuple;
		tuple = super.unbind(object, "lecturerLectures", "lecturerCourses");
		tuple.put("totalNumberOfHandsOnLectures", object.getTotalNumberOfLecturesPerType().get(ActivityType.HANDS_ON));
		tuple.put("totalNumberOfTheoryLectures", object.getTotalNumberOfLecturesPerType().get(ActivityType.THEORY));
		tuple.put("totalNumberOfBalancedLectures", object.getTotalNumberOfLecturesPerType().get(ActivityType.BALANCED));
		super.getResponse().setData(tuple);

	}
}
