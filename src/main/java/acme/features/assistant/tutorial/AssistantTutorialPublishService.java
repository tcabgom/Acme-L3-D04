
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialPublishService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		final boolean status;
		final int id;
		Tutorial tutorial;
		final Assistant assistant;

		id = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findOneTutorialById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();

		final boolean userIsAssistant = super.getRequest().getPrincipal().hasRole(Assistant.class);
		final boolean tutorialIsNotPublished = tutorial.isDraftMode();
		final boolean assistantOwnsTutorial = tutorial.getAssistant().getUserAccount().getId() == userAccountId;
		final boolean tutorialHasSessions = this.repository.findManyTutorialSessionsByTutorialId(tutorial).size() > 0;

		super.getResponse().setAuthorised(userIsAssistant && tutorialIsNotPublished && assistantOwnsTutorial && tutorialHasSessions);

	}

	@Override
	public void load() {
		Tutorial object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTutorialById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Tutorial object) {
		assert object != null;

		int courseId;
		Course course;

		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, "code", "title", "tutorialAbstract", "goals", "draftMode");
		object.setCourse(course);
	}

	@Override
	public void validate(final Tutorial object) {
		assert object != null;

	}

	@Override
	public void perform(final Tutorial object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;

		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;

		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "title", "tutorialAbstract", "goals", "draftMode");
		final int numberOfSessions = this.repository.findManyTutorialSessionsByTutorialId(object).size();
		tuple.put("numberOfSessions", numberOfSessions);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);

		super.getResponse().setData(tuple);
	}

}
