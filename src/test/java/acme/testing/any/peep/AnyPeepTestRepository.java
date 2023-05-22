
package acme.testing.any.peep;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.peep.Peep;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AnyPeepTestRepository extends AbstractRepository {

	@Query("select p from Peep p")
	Collection<Peep> findAllPeeps();

}
