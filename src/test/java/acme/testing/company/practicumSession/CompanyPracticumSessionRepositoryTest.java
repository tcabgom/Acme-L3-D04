
package acme.testing.company.practicumSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.entities.practicum.Practicum;
import acme.framework.repositories.AbstractRepository;

public interface CompanyPracticumSessionRepositoryTest extends AbstractRepository {

	@Query("select p from Practicum p where p.company.name = :company")
	Collection<Practicum> findAllPracticumByCompany(String company);
}
