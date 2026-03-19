
package acme.features.sponsor.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.DonationKind;
import acme.entities.sponsorships.Donation;
import acme.entities.sponsorships.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorDonationCreateService extends AbstractService<Sponsor, Donation> {

	@Autowired
	private SponsorDonationRepository	repository;

	private Sponsorship					sponsorship;
	private Donation					donation;


	@Override
	public void load() {
		int sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);

		this.sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);

		this.donation = new Donation();
		this.donation.setSponsorship(this.sponsorship);
		this.donation.setDraftMode(true);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.sponsorship != null) {
			int userAccountId = super.getRequest().getPrincipal().getAccountId();
			Sponsor sponsor = this.repository.findSponsorByUserAccountId(userAccountId);

			status = sponsor != null && this.sponsorship.isDraftMode() && this.sponsorship.getSponsor().getId() == sponsor.getId();
		}

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.donation, "name", "notes", "money", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.donation);
	}

	@Override
	public void execute() {
		this.repository.save(this.donation);
	}

	@Override
	public void unbind() {
		SelectChoices choices = SelectChoices.from(DonationKind.class, this.donation.getKind());
		Tuple tuple = super.unbindObject(this.donation, "name", "notes", "money", "kind", "draftMode");

		tuple.put("kinds", choices);
		tuple.put("sponsorshipId", this.sponsorship.getId());
		tuple.put("readonly", !this.donation.isDraftMode() || !this.donation.getSponsorship().isDraftMode());
	}
}
