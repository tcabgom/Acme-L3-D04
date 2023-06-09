
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.AuxiliaryService;
import acme.entities.lecture.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialCreateService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepository	repository;
	@Autowired
	protected AuxiliaryService				auxiliaryService;

	// Abstract Service interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Assistant.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		final Tutorial object;
		object = new Tutorial();

		final Assistant assistant = this.repository.findOneAssistantById(super.getRequest().getPrincipal().getActiveRoleId());

		object.setAssistant(assistant);
		object.setDraftMode(true);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Tutorial object) {
		assert object != null;

		final int courseId = super.getRequest().getData("course", int.class);
		final Course course = this.repository.findOneCourseById(courseId);
		object.setCourse(course);

		super.bind(object, "code", "title", "tutorialAbstract", "goals");
	}

	@Override
	public void validate(final Tutorial object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			final Tutorial potentialDuplicate = this.repository.findOneTutorialByCode(object.getCode());
			super.state(potentialDuplicate == null, "code", "assistant.tutorial.form.error.code");
			super.state(this.auxiliaryService.validateString(object.getCode()), "code", "acme.validation.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.auxiliaryService.validateString(object.getTitle()), "title", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("tutorialAbstract"))
			super.state(this.auxiliaryService.validateString(object.getTutorialAbstract()), "tutorialAbstract", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("goals"))
			super.state(this.auxiliaryService.validateString(object.getGoals()), "goals", "acme.validation.spam");

	}

	@Override
	public void perform(final Tutorial object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;

		Tuple tuple;
		Collection<Course> courses;
		final SelectChoices choices;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "title", "tutorialAbstract", "goals", "draftMode");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}
