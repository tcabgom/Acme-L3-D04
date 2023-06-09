
package acme.features.assistant.tutorialSession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionListService extends AbstractService<Assistant, TutorialSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {

		boolean status;

		status = super.getRequest().hasData("tutorialId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int masterId;
		int tutorialOwnerId;
		int assistantId;
		Tutorial tutorial;

		masterId = super.getRequest().getData("tutorialId", int.class);
		tutorial = this.repository.findOneTutorialById(masterId);
		assistantId = super.getRequest().getPrincipal().getAccountId();
		tutorialOwnerId = tutorial.getAssistant().getUserAccount().getId();
		status = tutorial != null && assistantId == tutorialOwnerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TutorialSession> objects;
		final int masterId;

		masterId = super.getRequest().getData("tutorialId", int.class);
		objects = this.repository.findManyTutorialSessionsByMasterId(masterId);

		super.getBuffer().setData(objects);

		Tutorial tutorial;
		final boolean draftMode;

		tutorial = this.repository.findOneTutorialById(masterId);
		draftMode = tutorial.isDraftMode();

		super.getResponse().setGlobal("tutorialId", masterId);
		super.getResponse().setGlobal("draftMode", draftMode);
	}

	@Override
	public void unbind(final TutorialSession object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "sessionType");
		super.getResponse().setData(tuple);
	}

}
