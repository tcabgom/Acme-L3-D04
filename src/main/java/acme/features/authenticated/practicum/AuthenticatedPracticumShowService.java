
package acme.features.authenticated.practicum;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedPracticumShowService extends AbstractService<Authenticated, Practicum> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedPracticumRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Practicum object;

		object = this.repository.findPracticumById(super.getRequest().getData("id", int.class));

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Practicum object) {
		assert object != null;

		Tuple tuple;
		final Collection<PracticumSession> sessions = this.repository.findPracticumSessionsByCompanyId(object);

		tuple = super.unbind(object, "code", "title", "abstractPracticum", "goals", "draftMode");
		tuple.put("estimatedTotalTime", object.getEstimatedTotalTimeInHours(sessions));
		super.getResponse().setData(tuple);
	}

}
