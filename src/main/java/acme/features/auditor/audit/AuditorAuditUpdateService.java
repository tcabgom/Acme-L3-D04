
package acme.features.auditor.audit;

import java.util.Collection;

import acme.components.AuxiliaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.lecture.Course;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditUpdateService extends AbstractService<Auditor, Audit> {

	@Autowired
	protected AuditorAuditRepository repository;
	@Autowired
	private AuxiliaryService auxiliaryService;


	@Override
	public void check() {
		boolean status;
		Audit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditById(id);

		status = object.getAuditor().getId() == super.getRequest().getPrincipal().getActiveRoleId() && object.isDraftMode() && super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Auditor.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Audit object;

		final int auditorID = super.getRequest().getPrincipal().getActiveRoleId();
		final Auditor auditor = this.repository.findAuditorById(auditorID);

		object = new Audit();
		object.setAuditor(auditor);
		object.setDraftMode(true);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Audit object) {
		assert object != null;
		final int auditorID = super.getRequest().getPrincipal().getActiveRoleId();
		final Auditor auditor = this.repository.findAuditorById(auditorID);
		final int courseId = super.getRequest().getData("course", int.class);
		final Course course = this.repository.findCourseById(courseId);

		object.setCourse(course);
		object.setAuditor(auditor);
		object.setDraftMode(true);

		super.bind(object, "code", "conclusion", "strongPoints", "weakPoints");

	}

	@Override
	public void validate(final Audit object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(object.isDraftMode(), "draftMode", "administrator.audit.form.error.draftMode");

		if(!super.getBuffer().getErrors().hasErrors("code")) {
			super.state(auxiliaryService.validateString(object.getCode()), "code", "acme.validation.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("conclusion")) {
			super.state(auxiliaryService.validateString(object.getConclusion()), "conclusion", "acme.validation.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("strongPoints")) {
			super.state(auxiliaryService.validateString(object.getStrongPoints()), "strongPoints", "acme.validation.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("weakPoints")) {
			super.state(auxiliaryService.validateString(object.getWeakPoints()), "weakPoints", "acme.validation.spam");
		}
	}

	@Override
	public void perform(final Audit object) {
		assert object != null;

		this.repository.save(object);

	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Tuple tuple;
		final SelectChoices choices;
		final Collection<Course> courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());

		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints", "draftMode");
		tuple.put("readonly", false);
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("published", object.isDraftMode());

		super.getResponse().setData(tuple);
	}

}
