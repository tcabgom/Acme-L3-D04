
package acme.features.student.enrolment;

import acme.entities.activity.Activity;
import acme.entities.enrolment.Enrolment;
import acme.entities.lecture.Course;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class StudentEnrolmentShowService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected StudentEnrolmentRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}
	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Enrolment enrolment = this.repository.findById(id);

		int studentRoleId = super.getRequest().getPrincipal().getActiveRoleId();
		Student student = enrolment.getStudent();
		Student currentStudent = this.repository.findStudentById(studentRoleId);

		boolean status = student.getId() == currentStudent.getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Enrolment object = this.repository.findById(id);

		List<Activity> activities = this.repository.findActivitiesByEnrolment(object.getId());
		object.setWorkTime(object.getEstimatedTotalTimeInHours(activities));
		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;

		Collection<Course> courses = this.repository.findAllPublishedCourses();
		SelectChoices choices = SelectChoices.from(courses, "title", object.getCourse());

		Tuple tuple = super.unbind(object, "code", "motivation", "goals", "workTime");
		tuple.put("readonly", object.isFinished());
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}
