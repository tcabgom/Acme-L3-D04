
package acme.features.authenticated.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedTutorialListService extends AbstractService<Authenticated, Tutorial> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AuthenticatedTutorialRepository repository;

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
		status = super.getRequest().getPrincipal().isAuthenticated();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Tutorial> objects;
		final int masterId = super.getRequest().getData("tutorialId", int.class);
		objects = this.repository.findManyTutorialsByCourseId(masterId);
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		Tuple tuple;

		tuple = super.unbind(object, "title", "course.title", "code");
		super.getResponse().setData(tuple);
	}

}
