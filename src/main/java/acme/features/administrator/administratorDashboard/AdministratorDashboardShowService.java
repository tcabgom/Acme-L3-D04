
package acme.features.administrator.administratorDashboard;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.bulletin.Bulletin;
import acme.entities.configuration.Configuration;
import acme.entities.note.Note;
import acme.entities.offer.Offer;
import acme.entities.peep.Peep;
import acme.forms.AdminDashboard;
import acme.forms.Statistics;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorDashboardShowService extends AbstractService<Administrator, AdminDashboard> {

	@Autowired
	protected AdministratorDashboardRepository repository;

	// Abstract Service interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		final AdminDashboard object = new AdminDashboard();

		Principal principal;
		int userAccountId;
		int assistantId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		assistantId = principal.getActiveRoleId();
		final Administrator administrator = this.repository.findOneAdministratorByUserAccountId(userAccountId);

		//////// Total Number Of Principals Per Role

		final Integer totalAuditors = this.repository.findNumOfAuditors();
		final Integer totalAdministrators = this.repository.findNumOfAdministrators();
		final Integer totalAssistants = this.repository.findNumOfAssistants();
		final Integer totalLecturers = this.repository.findNumOfLecturers();
		final Integer totalStudents = this.repository.findNumOfStudents();
		final Integer totalCompanys = this.repository.findNumOfCompanys();

		//////// Ratio of peeps with link and Email

		final Collection<Peep> peeps = this.repository.findAllPeeps();
		final Integer numPeeps = peeps.size();

		Double ratioPeepsWithEmailAndLink = 0.;
		double peepsWithEmailAndLink = 0.;

		for (final Peep peep : peeps)
			if (peep.getLink() != null && peep.getEmail() != null)
				peepsWithEmailAndLink++;
		ratioPeepsWithEmailAndLink = (double) Math.round(peepsWithEmailAndLink * 100 / numPeeps);

		////////Ratio of critical and non-critical bulletins 

		final Collection<Bulletin> bulletins = this.repository.findAllBulletins();
		final Integer numBulletins = bulletins.size();

		Double ratioCriticalBulletins = 0.;
		Double ratioNonCriticalBulletins = 0.;

		double criticalBulletins = 0.;
		double nonCriticalBulletins = 0.;

		for (final Bulletin bulletin : bulletins)
			if (bulletin.isCritical())
				criticalBulletins++;
			else
				nonCriticalBulletins++;

		ratioCriticalBulletins = (double) Math.round(criticalBulletins * 100 / numBulletins);
		ratioNonCriticalBulletins = (double) Math.round(nonCriticalBulletins * 100 / numBulletins);
		Math.round(nonCriticalBulletins * 100 / numBulletins);

		object.setTotalAdministrators(totalAdministrators);
		object.setTotalAssistants(totalAssistants);
		object.setTotalAuditors(totalAuditors);
		object.setTotalCompanys(totalCompanys);
		object.setTotalLecturers(totalLecturers);
		object.setTotalStudents(totalStudents);

		object.setLinkAndEmailPeepRatio(ratioPeepsWithEmailAndLink);
		object.setCriticalBulletinRatio(ratioCriticalBulletins);
		object.setNonCriticalBulletinRatio(ratioNonCriticalBulletins);

		//////// Average, minimum, maximum and standard deviations of offer's budget by currency

		final Map<String, Statistics> budgetStatisticsByCurrency = new HashMap<>();

		final Collection<Offer> offers = this.repository.findAllOffers();
		final Configuration configuration = this.repository.findConfiguration().get(0);
		final String[] currencies = configuration.getAcceptedCurrencies().split(",");

		for (final String currency : currencies) {

			final Statistics offerStatistics = new Statistics();
			final Collection<Double> values = new ArrayList<>();

			for (final Offer offer : offers)
				if (offer.getPrice().getCurrency().contains(currency))
					values.add(offer.getPrice().getAmount());

			offerStatistics.obtainValues(values);

			budgetStatisticsByCurrency.put(currency, offerStatistics);
		}

		//////// Average, minimum, maximum and standard deviations of notes posted in the last 10 weeks

		final Statistics noteStatistics = new Statistics();
		final Collection<Double> values = new ArrayList<>();
		final Collection<Note> notes = this.repository.findAllNotes();

		final Collection<Note> notesLast10Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(10, ChronoUnit.WEEKS))).collect(Collectors.toList());
		final Collection<Note> notesLast9Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(9, ChronoUnit.WEEKS))).collect(Collectors.toList());
		final Collection<Note> notesLast8Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(8, ChronoUnit.WEEKS))).collect(Collectors.toList());
		final Collection<Note> notesLast7Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(7, ChronoUnit.WEEKS))).collect(Collectors.toList());
		final Collection<Note> notesLast6Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(6, ChronoUnit.WEEKS))).collect(Collectors.toList());
		final Collection<Note> notesLast5Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(5, ChronoUnit.WEEKS))).collect(Collectors.toList());
		final Collection<Note> notesLast4Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(4, ChronoUnit.WEEKS))).collect(Collectors.toList());
		final Collection<Note> notesLast3Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(3, ChronoUnit.WEEKS))).collect(Collectors.toList());
		final Collection<Note> notesLast2Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(2, ChronoUnit.WEEKS))).collect(Collectors.toList());
		final Collection<Note> notesLast1Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.WEEKS))).collect(Collectors.toList());

		values.add(notesLast10Weeks.size() * 1.0);
		values.add(notesLast9Weeks.size() * 1.0);
		values.add(notesLast8Weeks.size() * 1.0);
		values.add(notesLast7Weeks.size() * 1.0);
		values.add(notesLast6Weeks.size() * 1.0);
		values.add(notesLast5Weeks.size() * 1.0);
		values.add(notesLast4Weeks.size() * 1.0);
		values.add(notesLast3Weeks.size() * 1.0);
		values.add(notesLast2Weeks.size() * 1.0);
		values.add(notesLast1Weeks.size() * 1.0);

		noteStatistics.obtainValues(values);

		object.setBudgetStatisticsByCurrency(budgetStatisticsByCurrency);
		object.setLinkAndEmailPeepRatio(ratioPeepsWithEmailAndLink);
		object.setCriticalBulletinRatio(ratioCriticalBulletins);
		object.setNonCriticalBulletinRatio(ratioNonCriticalBulletins);
		object.setNotesPostedInLast10Weeks(noteStatistics);
		object.setTotalAssistants(totalAssistants);
		object.setTotalAdministrators(totalAdministrators);
		object.setTotalCompanys(totalCompanys);
		object.setTotalLecturers(totalLecturers);
		object.setTotalStudents(totalStudents);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final AdminDashboard object) {

		Tuple tuple;
		tuple = super.unbind(object, "notesPostedInLast10Weeks");
		tuple.put("totalAuditors", object.getTotalAuditors());
		tuple.put("totalAdministrators", object.getTotalAdministrators());
		tuple.put("totalAssistants", object.getTotalAssistants());
		tuple.put("totalLecturers", object.getTotalLecturers());
		tuple.put("totalStudents", object.getTotalStudents());
		tuple.put("totalCompanys", object.getTotalCompanys());

		tuple.put("budgetStatisticsByCurrency", object.getBudgetStatisticsByCurrency());
		tuple.put("linkAndEmailPeepRatio", object.getLinkAndEmailPeepRatio());
		tuple.put("criticalBulletinRatio", object.getCriticalBulletinRatio());
		tuple.put("nonCriticalBulletinRatio", object.getNonCriticalBulletinRatio());

		super.getResponse().setData(tuple);

	}

}
