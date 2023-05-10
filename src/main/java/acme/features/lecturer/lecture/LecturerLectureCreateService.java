
package acme.features.lecturer.lecture;

import acme.components.AuxiliaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.enumerates.ActivityType;
import acme.entities.lecture.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureCreateService extends AbstractService<Lecturer, Lecture> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureRepository repository;
	@Autowired
	protected AuxiliaryService auxiliaryService;

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
		Lecture object;
		object = new Lecture();
		object.setDraftMode(true);
		final Lecturer lecturer = this.repository.findOneLecturerById(super.getRequest().getPrincipal().getActiveRoleId());
		object.setLecturer(lecturer);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecture object) {
		assert object != null;
		super.bind(object, "title", "lecAbstract", "learningTime", "body", "knowledge", "furtherInformation");
	}

	@Override
	public void validate(final Lecture object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(auxiliaryService.validateString(object.getTitle()), "title", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("lecAbstract"))
			super.state(auxiliaryService.validateString(object.getLecAbstract()), "lecAbstract", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("body"))
			super.state(auxiliaryService.validateString(object.getBody()), "body", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("furtherInformation"))
			super.state(auxiliaryService.validateString(object.getFurtherInformation()), "furtherInformation", "acme.validation.spam");

	}

	@Override
	public void perform(final Lecture object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		Tuple tuple;

		final SelectChoices choices = SelectChoices.from(ActivityType.class, object.getKnowledge());

		tuple = super.unbind(object, "title", "lecAbstract", "learningTime", "body", "knowledge", "furtherInformation", "draftMode", "lecturer");

		tuple.put("knowledge", choices.getSelected().getKey());
		tuple.put("choices", choices);
		super.getResponse().setData(tuple);
	}
}
