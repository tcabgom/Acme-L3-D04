
package acme.features.assistant.tutorialSession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.AuxiliaryService;
import acme.entities.enumerates.ActivityType;
import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionCreateService extends AbstractService<Assistant, TutorialSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AssistantTutorialSessionRepository	repository;
	@Autowired
	protected AuxiliaryService						auxiliaryService;

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
		status = tutorial != null && assistantId == tutorialOwnerId && tutorial.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final TutorialSession object;

		final int masterId = super.getRequest().getData("tutorialId", int.class);
		final Tutorial tutorial = this.repository.findOneTutorialById(masterId);

		object = new TutorialSession();
		object.setTutorial(tutorial);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final TutorialSession object) {
		assert object != null;
		super.bind(object, "title", "sessionAbstract", "sessionType", "sessionStart", "sessionEnd", "moreInfo");
	}

	@Override
	public void validate(final TutorialSession object) {
		assert object != null;

		boolean validSessionStart = true;

		if (!super.getBuffer().getErrors().hasErrors("sessionStart")) {
			final Date minimunValidStartDate = MomentHelper.deltaFromMoment(MomentHelper.getCurrentMoment(), 1, ChronoUnit.DAYS);
			validSessionStart = MomentHelper.isAfterOrEqual(object.getSessionStart(), minimunValidStartDate);
			super.state(validSessionStart, "sessionStart", "assistant.tutorialSession.form.error.sessionStart");
			super.state(validSessionStart, "sessionEnd", "assistant.tutorialSession.form.error.sessionStartIsAlreadyBad");
		}

		if (!super.getBuffer().getErrors().hasErrors("sessionEnd") && !super.getBuffer().getErrors().hasErrors("sessionStart")) {
			final Date minimunValidEndDate = MomentHelper.deltaFromMoment(object.getSessionStart(), 1, ChronoUnit.HOURS);
			final Date maximunValidEndDate = MomentHelper.deltaFromMoment(object.getSessionStart(), 5, ChronoUnit.HOURS);
			super.state(MomentHelper.isAfterOrEqual(object.getSessionEnd(), minimunValidEndDate) && MomentHelper.isBeforeOrEqual(object.getSessionEnd(), maximunValidEndDate), "sessionEnd", "assistant.tutorialSession.form.error.sessionEnd");
		}

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.auxiliaryService.validateString(object.getTitle()), "title", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("sessionAbstract"))
			super.state(this.auxiliaryService.validateString(object.getSessionAbstract()), "sessionAbstract", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("moreInfo"))
			super.state(this.auxiliaryService.validateString(object.getMoreInfo()), "moreInfo", "acme.validation.spam");

	}

	@Override
	public void perform(final TutorialSession object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final TutorialSession object) {
		assert object != null;

		Tuple tuple;
		final SelectChoices sessionTypeChoices = SelectChoices.from(ActivityType.class, object.getSessionType());

		tuple = super.unbind(object, "title", "sessionAbstract", "sessionStart", "sessionEnd", "moreInfo");
		tuple.put("sessionType", sessionTypeChoices.getSelected().getKey());
		tuple.put("sessionTypes", sessionTypeChoices);
		tuple.put("tutorialId", super.getRequest().getData("tutorialId", int.class));

		super.getResponse().setData(tuple);
	}

}
