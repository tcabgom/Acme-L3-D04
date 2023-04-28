
package acme.testing.any.peep;

import java.util.Collection;

import acme.entities.peep.Peep;

public interface AnyPeepTestRepository {

	Collection<Peep> findAllPeeps();

}
