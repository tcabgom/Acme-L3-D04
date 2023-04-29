
package acme.features.company.companyDashboard;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Company;

@Repository
public interface CompanyDashboardRepository extends AbstractRepository {

	@Query("select c from Company c where c.userAccount.id = :id")
	Company findCompanyByUserAccountId(int id);

	@Query("select p from Practicum p where p.company.id = :id")
	Collection<Practicum> findPracticumsByCompanyId(int id);

	@Query("select COUNT(ps) from PracticumSession ps where MONTH(ps.startWeek) = :month and ps.practicum.company.id = :id")
	Integer findNumberOfPracticaByMonthAndCompany(int month, int id);

	@Query("select COUNT(ps) from PracticumSession ps where ps.practicum.company.id = :id")
	int findNumberOfSessionByCompany(int id);

	@Query("select ps from PracticumSession ps where ps.practicum.company.id = :id")
	Collection<PracticumSession> findAllPracticumSessionByCompany(int id);

	@Query("select p from Practicum p where p.company.id = :id")
	Collection<Practicum> findAllPracticumByCompany(int id);

	@Query("select ps from PracticumSession ps where ps.practicum = :practicum")
	Collection<PracticumSession> findPracticumSessionByPracticum(Practicum practicum);

}
