
package acme.features.authenticated.auditor;

import acme.components.AuxiliaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuthenticatedAuditorUpdateService extends AbstractService<Authenticated, Auditor> {

	@Autowired
	protected AutheticatedAuditorRepository repository;
	@Autowired
	private AuxiliaryService auxiliaryService;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Auditor.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Auditor object;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		object = this.repository.findAuditorByUserAccountId(userAccountId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Auditor object) {
		assert object != null;
		super.bind(object, "firm", "proffesionalID", "certifications", "link");
	}

	@Override
	public void validate(final Auditor object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("firm"))
			super.state(auxiliaryService.validateString(object.getFirm()), "firm","acme.validation.spam");

		if(!super.getBuffer().getErrors().hasErrors("proffesionalID"))
			super.state(auxiliaryService.validateString(object.getProffesionalID()), "proffesionalID","acme.validation.spam");

		if(!super.getBuffer().getErrors().hasErrors("certifications"))
			super.state(auxiliaryService.validateString(object.getCertifications()), "certifications","acme.validation.spam");

		if(!super.getBuffer().getErrors().hasErrors("link"))
			super.state(auxiliaryService.validateString(object.getLink()), "link","acme.validation.spam");

	}

	@Override
	public void perform(final Auditor object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Auditor object) {
		assert object != null;
		final Tuple tuple;
		tuple = super.unbind(object, "firm", "proffesionalID", "certifications", "link");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
