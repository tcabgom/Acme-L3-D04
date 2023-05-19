
package acme.features.authenticated.assistant;

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
import acme.roles.Assistant;

@Service
public class AuthenticatedAssistantUpdateService extends AbstractService<Authenticated, Assistant> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedAssistantRepository repository;
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
		status = super.getRequest().getPrincipal().hasRole(Assistant.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Assistant object;
		Principal principal;
		int userAccountId;
		final UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();

		object = this.repository.findOneAssistantByUserAccountId(userAccountId);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Assistant object) {
		assert object != null;
		super.bind(object, "supervisor", "expertiseFieldsList", "resume", "link");
	}

	@Override
	public void validate(final Assistant object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("supervisor"))
			super.state(auxiliaryService.validateString(object.getSupervisor()), "supervisor", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("expertiseFieldsList"))
			super.state(auxiliaryService.validateString(object.getExpertiseFieldsList()), "expertiseFieldsList", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("resume"))
			super.state(auxiliaryService.validateString(object.getResume()), "resume", "acme.validation.spam");

		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(auxiliaryService.validateString(object.getLink()), "link", "acme.validation.spam");

	}

	@Override
	public void perform(final Assistant object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Assistant object) {
		assert object != null;
		final Tuple tuple;
		tuple = super.unbind(object, "supervisor", "expertiseFieldsList", "resume", "link");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
