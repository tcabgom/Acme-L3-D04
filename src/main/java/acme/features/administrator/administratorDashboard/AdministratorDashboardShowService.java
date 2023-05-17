//
//package acme.features.administrator.administratorDashboard;
//
//import java.time.temporal.ChronoUnit;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashMap;S
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import acme.entities.bulletin.Bulletin;
//import acme.entities.configuration.Configuration;
//import acme.entities.note.Note;
//import acme.entities.offer.Offer;
//import acme.entities.peep.Peep;
//import acme.forms.AdminDashboard;
//import acme.forms.Statistics;
//import acme.framework.components.accounts.Administrator;
//import acme.framework.components.accounts.Principal;
//import acme.framework.components.models.Tuple;
//import acme.framework.helpers.MomentHelper;
//import acme.framework.services.AbstractService;
//
//@Service
//public class AdministratorDashboardShowService extends AbstractService<Administrator, AdminDashboard> {
////
////	@Autowired
////	protected AdministratorDashboardRepository repository;
////
////	// Abstract Service interface ----------------------------------------------
////
////
////	@Override
////	public void check() {
////		super.getResponse().setChecked(true);
////	}
////
////	@Override
////	public void authorise() {
////		super.getResponse().setAuthorised(true);
////	}
////
////	@Override
////	public void load() {
////		final AdminDashboard object = new AdminDashboard();
////
////		Principal principal;
////		int userAccountId;
////		int assistantId;
////
////		principal = super.getRequest().getPrincipal();
////		userAccountId = principal.getAccountId();
////		assistantId = principal.getActiveRoleId();
////		final Administrator administrator = this.repository.findOneAdministratorByUserAccountId(userAccountId);
////
////		//////// Total Number Of Principals Per Role
////
////		final Integer totalAuditors = this.repository.findNumOfAuditors();
////		final Integer totalAdministrators = this.repository.findNumOfAdministrators();
////		final Integer totalAssistants = this.repository.findNumOfAssistants();
////		final Integer totalLecturers = this.repository.findNumOfLecturers();
////		final Integer totalStudents = this.repository.findNumOfStudents();
////		final Integer totalCompanys = this.repository.findNumOfCompanys();
////
////		//////// Ratio of peeps with link and Email
////
////		final Collection<Peep> peeps = this.repository.findAllPeeps();
////		final Integer numPeeps = peeps.size();
////
////		Double ratioPeepsWithEmailAndLink = 0.;
////		double peepsWithEmailAndLink = 0.;
////
////		for (final Peep peep : peeps)
////			if (!peep.getLink().isEmpty() && !peep.getEmail().isEmpty())
////				peepsWithEmailAndLink++;
////		ratioPeepsWithEmailAndLink = peepsWithEmailAndLink / numPeeps;
////
////		////////Ratio of critical and non-critical bulletins 
////
////		final Collection<Bulletin> bulletins = this.repository.findAllBulletins();
////		final Integer numBulletins = bulletins.size();
////
////		Double ratioCriticalBulletins = 0.;
////		Double ratioNonCriticalBulletins = 0.;
////
////		double criticalBulletins = 0.;
////		double nonCriticalBulletins = 0.;
////
////		for (final Bulletin bulletin : bulletins)
////			if (bulletin.isCritical())
////				criticalBulletins++;
////			else
////				nonCriticalBulletins++;
////
////		ratioCriticalBulletins = criticalBulletins / numBulletins;
////		ratioNonCriticalBulletins = nonCriticalBulletins / numBulletins;
////
////		object.setTotalAdministrators(totalAdministrators);
////		object.setTotalAssistants(totalAssistants);
////		object.setTotalAuditors(totalAuditors);
////		object.setTotalCompanys(totalCompanys);
////		object.setTotalLecturers(totalLecturers);
////		object.setTotalStudents(totalStudents);
////
////		object.setLinkAndEmailPeepRatio(ratioPeepsWithEmailAndLink);
////		object.setCriticalBulletinRatio(ratioCriticalBulletins);
////		object.setNonCriticalBulletinRatio(ratioNonCriticalBulletins);
////
////		//////// Average, minimum, maximum and standard deviations of offer's budget by currency
////
////		final Map<String, Statistics> budgetStatisticsByCurrency = new HashMap<>();
////
////		final Collection<Offer> offers = this.repository.findAllOffers();
////		final Configuration configuration = this.repository.findConfiguration();
////		final String[] currencies = configuration.getAceptedCurrencies().split(",");
////
////		for (final String currency : currencies) {
////
////			final Statistics offerStatistics = new Statistics();
////			final Collection<Double> values = new ArrayList<>();
////
////			for (final Offer offer : offers)
////				if (offer.getPrice().getCurrency() == currency)
////					values.add(offer.getPrice().getAmount());
////
////			offerStatistics.obtainValues(values);
////
////			budgetStatisticsByCurrency.put(currency, offerStatistics);
////		}
////
////		//////// Average, minimum, maximum and standard deviations of notes posted in the last 10 weeks
////
////		final Statistics noteStatistics = new Statistics();
////		final Collection<Double> values = new ArrayList<>();
////		final Collection<Note> notes = this.repository.findAllNotes();
////		final Date currentMoment = MomentHelper.getCurrentMoment();
////
////		final Collection<Note> notesLast10Weeks = notes.stream().filter(note -> MomentHelper.isBefore(note.getInstantiationMoment(), MomentHelper.deltaFromCurrentMoment(10, ChronoUnit.WEEKS))).collect(Collectors.toList());
////
////	}
////
////	@Override
////	public void unbind(final AdminDashboard object) {
////
////		Tuple tuple;
////
////		tuple = super.unbind(object, "notesPostedInLast10Weeks");
////		tuple.put("totalAuditors", object.getTotalAuditors());
////		tuple.put("totalAdministrators", object.getTotalAdministrators());
////		tuple.put("totalAssistants", object.getTotalAssistants());
////		tuple.put("totalLecturers", object.getTotalLecturers());
////		tuple.put("totalStudents", object.getTotalStudents());
////		tuple.put("totalCompanys", object.getTotalStudents());
////
////		tuple.put("budgetStatisticsByCurrency", object.getBudgetStatisticsByCurrency());
////		tuple.put("linkAndEmailPeepRatio", object.getLinkAndEmailPeepRatio());
////		tuple.put("criticalBulletinRatio", object.getCriticalBulletinRatio());
////		tuple.put("nonCriticalBulletinRatio", object.getNonCriticalBulletinRatio());
////
////		super.getResponse().setData(tuple);
////
////	}
//
//}
