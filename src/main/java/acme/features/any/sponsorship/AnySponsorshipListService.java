
package acme.features.any.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipRepository;

@Service
public class AnySponsorshipListService extends AbstractService<Any, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnySponsorshipRepository	repository;

	@Autowired
	private SponsorshipRepository		sponsorshipRepository;

	private Collection<Sponsorship>		sponsorships;

	// AbstractService interface ---------------------------------------------


	@Override
	public void load() {
		this.sponsorships = this.repository.findManyPublishedSponsorships();

		for (Sponsorship s : this.sponsorships)
			s.setRepository(this.sponsorshipRepository);
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.sponsorships, "ticker", "name", "startMoment", "endMoment", "moreInfo");

	}
}
