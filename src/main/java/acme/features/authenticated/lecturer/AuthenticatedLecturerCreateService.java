
package acme.features.authenticated.lecturer;

import acme.components.AuxiliaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class AuthenticatedLecturerCreateService extends AbstractService<Authenticated, Lecturer> {
	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedLecturerRepository repository;
	@Autowired
	private AuxiliaryService auxiliaryService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = !super.getRequest().getPrincipal().hasRole(Lecturer.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Lecturer object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Lecturer();
		object.setUserAccount(userAccount);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecturer object) {
		assert object != null;
		super.bind(object, "almaMater", "resume", "qualifications", "link");
	}

	@Override
	public void validate(final Lecturer object) {
		assert object != null;

		if(!super.getBuffer().getErrors().hasErrors("almaMater"))
			super.state(auxiliaryService.validateString(object.getAlmaMater()), "almaMater", "acme.validation.spam");

		if(!super.getBuffer().getErrors().hasErrors("resume"))
			super.state(auxiliaryService.validateString(object.getResume()), "resume", "acme.validation.spam");

		if(!super.getBuffer().getErrors().hasErrors("qualifications"))
			super.state(auxiliaryService.validateString(object.getQualifications()), "qualifications", "acme.validation.spam");

		if(!super.getBuffer().getErrors().hasErrors("link"))
			super.state(auxiliaryService.validateString(object.getLink()), "link", "acme.validation.spam");

	}

	@Override
	public void perform(final Lecturer object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Lecturer object) {
		assert object != null;
		final Tuple tuple;
		tuple = super.unbind(object, "almaMater", "resume", "qualifications", "link");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}
}
