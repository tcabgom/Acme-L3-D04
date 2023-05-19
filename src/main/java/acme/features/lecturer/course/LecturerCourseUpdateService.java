
package acme.features.lecturer.course;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.AuxiliaryService;
import acme.entities.lecture.Course;
import acme.entities.lecture.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseUpdateService extends AbstractService<Lecturer, Course> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerCourseRepository	repository;
	@Autowired
	private AuxiliaryService			auxiliaryService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Course object;
		final int id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId && object.isDraftMode());
	}

	@Override
	public void load() {
		Course object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);
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
			super.state(potentialDuplicate == null || potentialDuplicate.getId() == object.getId(), "code", "assistant.tutorial.form.error.code");
		}

		if (!super.getBuffer().getErrors().hasErrors("retailPrice")) {
			final Double amount = object.getRetailPrice().getAmount();
			super.state(amount < 1000000 && amount >= 0, "retailPrice", "lecturer.course.form.error.retailPrice");
		}

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.auxiliaryService.validateString(object.getCode()), "code", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.auxiliaryService.validateString(object.getTitle()), "title", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("courseAbstract"))
			super.state(this.auxiliaryService.validateString(object.getCourseAbstract()), "courseAbstract", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("furtherInformation"))
			super.state(this.auxiliaryService.validateString(object.getFurtherInformation()), "furtherInformation", "acme.validation.spam");
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

		final List<Lecture> lectures = this.repository.findLecturesByCourse(object.getId()).stream().collect(Collectors.toList());

		tuple.put("activityType", object.courseActivityType(lectures));
		tuple.put("readonly", false);

		boolean showPublish = false;
		showPublish = lectures.stream().allMatch(e -> e.isDraftMode() == false);

		tuple.put("showPublish", showPublish);

		super.getResponse().setData(tuple);
	}
}
