
package acme.features.administrator.offer;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.AuxiliaryService;
import acme.entities.offer.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferCreateService extends AbstractService<Administrator, Offer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AdministratorOfferRepository	repository;
	@Autowired
	protected AuxiliaryService				auxiliaryService;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Offer object;
		object = new Offer();

		final Date moment;
		moment = MomentHelper.getCurrentMoment();
		object.setInstantiatiation(moment);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Offer object) {
		assert object != null;

		super.bind(object, "header", "summary", "availabilityPeriodStart", "availabilityPeriodEnd", "price", "moreInfo");
	}

	@Override
	public void validate(final Offer object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("availabilityPeriodStart")) {
			final Date minimunValidStartDate = MomentHelper.deltaFromMoment(object.getInstantiatiation(), 1, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getAvailabilityPeriodStart(), minimunValidStartDate), "availabilityPeriodStart", "administrator.offer.form.error.availabilityPeriodStart");
		}

		if (!super.getBuffer().getErrors().hasErrors("availabilityPeriodEnd")) {
			final Date minimunValidEndDate = MomentHelper.deltaFromMoment(object.getAvailabilityPeriodStart(), 7, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfterOrEqual(object.getAvailabilityPeriodEnd(), minimunValidEndDate), "availabilityPeriodEnd", "administrator.offer.form.error.availabilityPeriodEnd");
		}

		if (!super.getBuffer().getErrors().hasErrors("price")) {
			final Double amount = object.getPrice().getAmount();
			super.state(amount < 1000000000 && amount >= 0, "price", "administrator.offer.form.error.price");
		}
		if (!super.getBuffer().getErrors().hasErrors("header"))
			super.state(this.auxiliaryService.validateString(object.getHeader()), "header", "acme.validation.spam");
		if (!super.getBuffer().getErrors().hasErrors("summary"))
			super.state(this.auxiliaryService.validateString(object.getSummary()), "summary", "acme.validation.spam");
		if (!super.getBuffer().getErrors().hasErrors("moreInfo"))
			super.state(this.auxiliaryService.validateString(object.getMoreInfo()), "moreInfo", "acme.validation.spam");
	}

	@Override
	public void perform(final Offer object) {
		assert object != null;
		this.repository.save(object);
	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "instantiatiation", "header", "summary", "availabilityPeriodStart", "availabilityPeriodEnd", "price", "moreInfo");

		super.getResponse().setData(tuple);
	}

}
