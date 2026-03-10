
package acme.features.any.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipRepository;

@Service
public class AnySponsorshipShowService extends AbstractService<Any, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnySponsorshipRepository	repository;

	@Autowired
	private SponsorshipRepository		sponsorshipRepository;

	private Sponsorship					sponsorship;

	// AbstractService interface ---------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.sponsorship = this.repository.findOnePublishedSponsorshipById(id);

		if (this.sponsorship != null)
			this.sponsorship.setRepository(this.sponsorshipRepository);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.sponsorship != null && !this.sponsorship.isDraftMode();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple;

		tuple = super.unbindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");

		Double monthsActive;
		monthsActive = this.sponsorship.getMonthsActive();

		tuple.put("monthsActive", monthsActive);
		tuple.put("totalMoney", this.sponsorship.getTotalMoney());

		tuple.put("sponsorId", this.sponsorship.getSponsor().getId());
		tuple.put("sponsorshipId", this.sponsorship.getId());

		tuple.put("readonly", true);
	}
}
