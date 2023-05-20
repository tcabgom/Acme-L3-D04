
package acme.features.company.companyDashboard;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.forms.CompanyDashboard;
import acme.forms.Statistics;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyDashboardShowService extends AbstractService<Company, CompanyDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected CompanyDashboardRepository repository;

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
		final CompanyDashboard object = new CompanyDashboard();
		final Company company = this.repository.findCompanyByUserAccountId(super.getRequest().getPrincipal().getAccountId());
		final Map<String, Integer> practicaNumberPerMonth = new HashMap<>();

		final Statistics sessionsStatistics = new Statistics();
		final Collection<Double> sessionsDuration = new ArrayList<>();
		final Collection<PracticumSession> practicumSession = this.repository.findAllPracticumSessionByCompany(company.getId());

		final Statistics practicumStatistics = new Statistics();
		final Collection<Double> practicumDuration = new ArrayList<>();
		final Collection<Practicum> practicum = this.repository.findAllPracticumByCompany(company.getId());

		final int hoursInMilliseconds = 3600000;
		final int minutesInMilliseconds = 60000;

		for (int i = 1; i < 13; i++)
			practicaNumberPerMonth.put(Month.of(i).toString(), this.repository.findNumberOfPracticaByMonthAndCompany(i, MomentHelper.getCurrentMoment().getYear(), company.getId()));

		for (final PracticumSession ps : practicumSession) {

			Double thisSessionDuration;

			final double thisSessionStartTime = ps.getStart().getTime();
			final double thisSessionEndTime = ps.getFinish().getTime();

			final double thisSessionHours = Math.abs(thisSessionEndTime / hoursInMilliseconds - thisSessionStartTime / hoursInMilliseconds);
			final double thisSessionMinutes = Math.abs(thisSessionEndTime / minutesInMilliseconds - thisSessionStartTime / minutesInMilliseconds) % 60 * 0.01;

			thisSessionDuration = thisSessionHours + thisSessionMinutes;
			sessionsDuration.add(thisSessionDuration);
		}
		sessionsStatistics.obtainValues(sessionsDuration);

		for (final Practicum p : practicum) {
			final Double duration = p.getEstimatedTotalTimeInHours(this.repository.findPracticumSessionByPracticum(p));

			practicumDuration.add(duration);
		}
		practicumStatistics.obtainValues(practicumDuration);

		object.setTotalPracticesPerMonth(practicaNumberPerMonth);
		object.setSessionStatistic(sessionsStatistics);
		object.setPracticumStatistic(practicumStatistics);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final CompanyDashboard object) {

		Tuple tuple;
		tuple = super.unbind(object, "sessionStatistic", "practicumStatistic", "totalPracticesPerMonth");

		super.getResponse().setData(tuple);
	}

}
