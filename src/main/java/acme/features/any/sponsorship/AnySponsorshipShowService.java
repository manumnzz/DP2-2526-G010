
package acme.features.any.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipRepository;

@Service
public class AnySponsorshipShowService extends AbstractService<Any, Sponsorship> {

	@Autowired
	private AnySponsorshipRepository	repository;

	@Autowired
	private SponsorshipRepository		sponsorshipRepository;

	private Sponsorship					sponsorship;


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
		super.setAuthorised(this.sponsorship != null);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");

		super.getResponse().addData("monthsActive", this.sponsorship.getMonthsActive());
		super.getResponse().addData("totalMoney", this.sponsorship.getTotalMoney());

		super.getResponse().addData("sponsorId", this.sponsorship.getSponsor().getId());
		super.getResponse().addData("sponsorshipId", this.sponsorship.getId());
	}
}
