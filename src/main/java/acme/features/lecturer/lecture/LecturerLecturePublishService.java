
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
public class LecturerLecturePublishService extends AbstractService<Lecturer, Lecture> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected LecturerLectureRepository repository;
	@Autowired
	private AuxiliaryService auxiliaryService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		final int id = super.getRequest().getData("id", int.class);
		final Lecture object = this.repository.findLectureById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		super.getResponse().setAuthorised(object.getLecturer().getUserAccount().getId() == userAccountId && object.isDraftMode());
	}

	@Override
	public void load() {
		Lecture object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findLectureById(id);
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
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;

		Tuple tuple;

		final SelectChoices choices = SelectChoices.from(ActivityType.class, object.getKnowledge());

		tuple = super.unbind(object, "title", "lecAbstract", "learningTime", "body", "knowledge", "furtherInformation", "draftMode");
		tuple.put("knowledge", choices.getSelected().getKey());
		tuple.put("choices", choices);

		super.getResponse().setData(tuple);
	}
}
