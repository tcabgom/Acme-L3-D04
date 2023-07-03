
package acme.features.company.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.AuxiliaryService;
import acme.entities.lecture.Course;
import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumPublishService extends AbstractService<Company, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumRepository	repository;
	@Autowired
	private AuxiliaryService				auxiliaryService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		final Practicum object = this.repository.findPracticumById(super.getRequest().getData("id", int.class));

		status = object != null && super.getRequest().getPrincipal().hasRole(Company.class) && object.isDraftMode() && object.getCompany().getUserAccount().getId() == super.getRequest().getPrincipal().getAccountId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Practicum object = this.repository.findPracticumById(super.getRequest().getData("id", int.class));

		object.setDraftMode(true);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Practicum object) {
		assert object != null;

		final Course course = this.repository.findCourseById(super.getRequest().getData("course", int.class));

		super.bind(object, "code", "title", "abstractPracticum", "goals", "draftMode");

		object.setCourse(course);
	}

	@Override
	public void validate(final Practicum object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			final Practicum potentialDuplicate = this.repository.findPracticumByCode(object.getCode());
			super.state(potentialDuplicate == null || potentialDuplicate.equals(object), "code", "company.practicum.form.error.code");
		}

		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(!this.repository.findPracticumSessionByPracticum(object).isEmpty(), "draftMode", "company.practicum.form.error.noSessions");

		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(this.auxiliaryService.validateString(object.getCode()), "code", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.auxiliaryService.validateString(object.getTitle()), "title", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("abstractPracticum"))
			super.state(this.auxiliaryService.validateString(object.getAbstractPracticum()), "abstractPracticum", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(this.auxiliaryService.validateString(object.getGoals()), "goals", "acme.validation.spam");
	}

	@Override
	public void perform(final Practicum object) {
		assert object != null;

		object.setDraftMode(false);

		this.repository.save(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		Collection<Course> courses;
		SelectChoices choices;
		Collection<PracticumSession> sessions;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());
		sessions = this.repository.findPracticumSessionByPracticum(object);

		tuple = super.unbind(object, "code", "title", "abstractPracticum", "goals", "draftMode");
		tuple.put("readonly", !object.isDraftMode());
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("estimatedTime", object.getEstimatedTotalTimeInHours(sessions));

		super.getResponse().setData(tuple);
	}

}
