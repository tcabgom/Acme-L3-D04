
package acme.features.company.practicumSession;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.AuxiliaryService;
import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.MomentHelper;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionCreateService extends AbstractService<Company, PracticumSession> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyPracticumSessionRepository	repository;
	@Autowired
	protected AuxiliaryService					auxiliaryService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("practicumId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Practicum object;

		object = this.repository.findPracticumById(super.getRequest().getData("practicumId", int.class));

		status = object != null && super.getRequest().getPrincipal().hasRole(object.getCompany());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final PracticumSession object = new PracticumSession();

		final Practicum practicum = this.repository.findPracticumById(super.getRequest().getData("practicumId", int.class));

		object.setPracticum(practicum);
		object.setExtraSession(practicum.isDraftMode() ? false : true);

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

		if (object.isExtraSession()) {
			final boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "company.practicumSession.form.error.confirmation");
		}

		if (!super.getBuffer().getErrors().hasErrors("finish"))
			super.state(MomentHelper.isLongEnough(object.getStart(), object.getFinish(), 1, ChronoUnit.WEEKS), "finish", "company.pacticumSession.form.error.not-long-enough");

		if (!super.getBuffer().getErrors().hasErrors("start"))
			super.state(MomentHelper.isAfter(object.getStart(), MomentHelper.deltaFromMoment(MomentHelper.getCurrentMoment(), 1, ChronoUnit.WEEKS)), "start", "company.practicumSession.form.error.sessionStart");

		if (!super.getBuffer().getErrors().hasErrors("finish"))
			super.state(!MomentHelper.isPresentOrPast(object.getStart()), "finish", "company.pacticumSession.form.error.last-date");

		if (!super.getBuffer().getErrors().hasErrors("start"))
			super.state(!MomentHelper.isPresentOrPast(object.getFinish()), "start", "company.pacticumSession.form.error.last-date");

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
		tuple.put("confirmation", false);

		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
