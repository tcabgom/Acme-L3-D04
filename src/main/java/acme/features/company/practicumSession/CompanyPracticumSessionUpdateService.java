
package acme.features.company.practicumSession;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.AuxiliaryService;
import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionUpdateService extends AbstractService<Company, PracticumSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionRepository	repository;
	@Autowired
	private AuxiliaryService					auxiliaryService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("practicumId", int.class) && super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Practicum object;

		object = this.repository.findPracticumById(super.getRequest().getData("practicumId", int.class));

		status = object != null && (super.getRequest().getPrincipal().hasRole(object.getCompany()) || !object.isDraftMode());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final PracticumSession object = new PracticumSession();

		final Practicum practicum = this.repository.findPracticumById(super.getRequest().getData("practicumId", int.class));

		object.setPracticum(practicum);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final PracticumSession object) {
		assert object != null;

		final Practicum practicum = this.repository.findPracticumById(super.getRequest().getData("practicumId", int.class));

		object.setPracticum(practicum);

		super.bind(object, "title", "abstractSession", "start", "finish", "link", "extraSession");
	}

	@Override
	public void validate(final PracticumSession object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("finish")) {
			super.state(MomentHelper.isLongEnough(object.getStart(), object.getFinish(), 1, ChronoUnit.WEEKS), "finish", "company.pacticumSession.form.error.not-long-enough");
			super.state(!MomentHelper.isAfterOrEqual(object.getStart(), object.getFinish()), "finish", "company.pacticumSession.form.error.not-long-enough");
			super.state(!MomentHelper.isPresentOrPast(object.getFinish()), "finish", "company.pacticumSession.form.error.last-date");
		}

		if (!super.getBuffer().getErrors().hasErrors("start")) {
			super.state(MomentHelper.isAfter(object.getStart(), MomentHelper.deltaFromMoment(MomentHelper.getCurrentMoment(), 1, ChronoUnit.WEEKS)), "start", "company.practicumSession.form.error.sessionStart");
			super.state(!MomentHelper.isPresentOrPast(object.getStart()), "start", "company.pacticumSession.form.error.last-date");
		}

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(this.auxiliaryService.validateString("title"), "title", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("abstractSession"))
			super.state(this.auxiliaryService.validateString("abstractSession"), "abstractSession", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(this.auxiliaryService.validateString("link"), "link", "acme.validation.spam");

	}

	@Override
	public void perform(final PracticumSession object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final PracticumSession object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "abstractSession", "start", "finish", "link", "extraSession");
		tuple.put("practicumId", super.getRequest().getData("practicumId", int.class));
		tuple.put("draftMode", object.getPracticum().isDraftMode());

		super.getResponse().setData(tuple);
	}

}
