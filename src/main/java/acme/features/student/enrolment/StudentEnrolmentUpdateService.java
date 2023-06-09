
package acme.features.student.enrolment;

import acme.components.AuxiliaryService;
import acme.entities.enrolment.Enrolment;
import acme.entities.lecture.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StudentEnrolmentUpdateService extends AbstractService<Student, Enrolment> {

	@Autowired
	protected StudentEnrolmentRepository repository;
	@Autowired
	private AuxiliaryService auxiliaryService;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		Enrolment object = this.repository.findById(id);

		Enrolment enrolment = this.repository.findById(id);

		int studentRoleId = super.getRequest().getPrincipal().getActiveRoleId();
		Student student = enrolment.getStudent();
		Student currentStudent = this.repository.findStudentById(studentRoleId);

		boolean status = !enrolment.isFinished() && student.getId() == currentStudent.getId();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int id = super.getRequest().getData("id", int.class);
		Enrolment object = this.repository.findById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Enrolment object) {
		assert object != null;

		final int courseId = super.getRequest().getData("course", int.class);
		final Course course = this.repository.findCourseById(courseId);
		object.setCourse(course);

		super.bind(object, "code", "motivation", "goals");

	}

	@Override
	public void validate(final Enrolment object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			final Enrolment potentialDuplicate = this.repository.findByCode(object.getCode());
			super.state(potentialDuplicate == null || potentialDuplicate.getId() == object.getId(), "code", "student.enrolment.form.error.code");
			super.state(auxiliaryService.validateString(object.getCode()), "code", "acme.validation.spam" );
		}

		if (!super.getBuffer().getErrors().hasErrors("motivation")) {
			super.state(auxiliaryService.validateString(object.getMotivation()), "motivation", "acme.validation.spam" );
		}

		if (!super.getBuffer().getErrors().hasErrors("goals")) {
			super.state(auxiliaryService.validateString(object.getGoals()), "goals", "acme.validation.spam" );
		}

	}

	@Override
	public void perform(final Enrolment object) {
		assert object != null;
		this.repository.save(object);

	}

	@Override
	public void unbind(final Enrolment object) {
		assert object != null;

		Collection<Course> courses = this.repository.findAllPublishedCourses();
		SelectChoices choices = SelectChoices.from(courses, "title", object.getCourse());

		Tuple tuple = super.unbind(object, "code", "motivation", "goals");
		tuple.put("readonly", object.isFinished());
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}
