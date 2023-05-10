
package acme.features.authenticated.company;

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
import acme.roles.Company;

@Service
public class AuthenticatedCompanyCreateService extends AbstractService<Authenticated, Company> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedCompanyRepository repository;
	@Autowired
	protected AuxiliaryService auxiliaryService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = !super.getRequest().getPrincipal().hasRole(Company.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Company object;
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getAccountId();
		final UserAccount userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Company();
		object.setUserAccount(userAccount);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Company object) {
		assert object != null;
		super.bind(object, "name", "VATNumber", "summary", "link");
	}

	@Override
	public void validate(final Company object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("VATNumber")) {
			final Company potencialDuplicate = this.repository.findOneCompanyByVATNumber(object.getVATNumber());
			super.state(potencialDuplicate == null, "VATNumber", "authenticated.company.form.error.code");
		}

		if(!super.getBuffer().getErrors().hasErrors("name")) {
			super.state(auxiliaryService.validateString(object.getName()), "name", "acme.validation.spam");
		}

		if(!super.getBuffer().getErrors().hasErrors("summary")) {
			super.state(auxiliaryService.validateString(object.getSummary()), "summary", "acme.validation.spam");
		}

		if(!super.getBuffer().getErrors().hasErrors("link")) {
			super.state(auxiliaryService.validateString(object.getLink()), "link", "acme.validation.spam");
		}

	}

	@Override
	public void perform(final Company object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Company object) {
		assert object != null;
		final Tuple tuple;
		tuple = super.unbind(object, "name", "VATNumber", "summary", "link");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
