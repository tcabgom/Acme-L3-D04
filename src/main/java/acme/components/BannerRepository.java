
package acme.components;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.banner.Banner;
import acme.framework.helpers.MomentHelper;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface BannerRepository extends AbstractRepository {

	@Query(value = "SELECT count(b) FROM Banner b WHERE b.displayPeriodInitial< :currentMoment AND b.displayPeriodEnding > :currentMoment")
	int countBanners(Date currentMoment);

	@Query(value = "SELECT b FROM Banner b")
	List<Banner> findAllBanners();

	default Banner findRandomBanner() {
		Banner result;
		int count, index;
		ThreadLocalRandom random;
		PageRequest page;
		List<Banner> list;

		count = this.countBanners(MomentHelper.getCurrentMoment());
		if (count == 0)
			result = null;
		else {
			random = ThreadLocalRandom.current();
			final Date currentMoment = MomentHelper.getCurrentMoment();
			index = random.nextInt(0, count);
			page = PageRequest.of(index, 1);
			list = this.findAllBanners().stream().filter(b -> MomentHelper.isBefore(b.getDisplayPeriodInitial(), currentMoment) && MomentHelper.isAfter(b.getDisplayPeriodEnding(), currentMoment)).collect(Collectors.toList());
			;
			result = list.isEmpty() ? null : list.get(index);
		}

		return result;
	}
}
