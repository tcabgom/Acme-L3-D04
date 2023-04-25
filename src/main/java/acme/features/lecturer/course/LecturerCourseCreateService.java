
package acme.features.lecturer.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Course;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseCreateService extends AbstractService<Lecturer, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		final Principal principal = super.getRequest().getPrincipal();
		super.getResponse().setAuthorised(principal.hasRole(Lecturer.class));
	}

	@Override
	public void load() {
		final Course object = new Course();
		object.setLecturer(this.repository.findOneLecturerById(super.getRequest().getPrincipal().getActiveRoleId()));
		object.setDraftMode(true);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Course object) {
		assert object != null;
		super.bind(object, "code", "title", "courseAbstract", "retailPrice", "furtherInformation");
	}

	@Override
	public void validate(final Course object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			final Course potentialDuplicate = this.repository.findOneCourseByCode(object.getCode());
			super.state(potentialDuplicate == null || potentialDuplicate.getId() == object.getId(), "code", "lecturer.course.form.error.code");
		}

		if (!super.getBuffer().getErrors().hasErrors("retailPrice")) {
			final Double amount = object.getRetailPrice().getAmount();
			super.state(amount < 1000000 && amount >= 0, "retailPrice", "lecturer.course.form.error.retailPrice");
		}

	}

	@Override
	public void perform(final Course object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "courseAbstract", "retailPrice", "furtherInformation", "draftMode", "lecturer");

		super.getResponse().setData(tuple);
	}

}
