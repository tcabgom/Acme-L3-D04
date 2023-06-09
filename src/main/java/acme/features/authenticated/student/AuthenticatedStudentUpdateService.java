
package acme.features.authenticated.student;

import acme.components.AuxiliaryService;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;
import acme.roles.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedStudentUpdateService extends AbstractService<Authenticated, Student> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedStudentRepository repository;
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
		status = super.getRequest().getPrincipal().hasRole(Student.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Principal principal = super.getRequest().getPrincipal();
		int userAccountId = principal.getAccountId();

		Student object = this.repository.findStudentByUserAccountId(userAccountId);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Student object) {
		assert object != null;
		super.bind(object, "statement", "strongFeatures", "weakFeatures", "furtherInformation");
	}

	@Override
	public void validate(final Student object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("statement")) {
			super.state(auxiliaryService.validateString(object.getStatement()), "statement","acme.validation.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("strongFeatures")) {
			super.state(auxiliaryService.validateString(object.getStrongFeatures()), "strongFeatures","acme.validation.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("weakFeatures")) {
			super.state(auxiliaryService.validateString(object.getWeakFeatures()), "weakFeatures","acme.validation.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("furtherInformation")) {
			super.state(auxiliaryService.validateString(object.getFurtherInformation()), "furtherInformation","acme.validation.spam");
		}
	}

	@Override
	public void perform(final Student object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Student object) {
		assert object != null;
		final Tuple tuple;
		tuple = super.unbind(object, "statement", "strongFeatures", "weakFeatures", "furtherInformation");
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
