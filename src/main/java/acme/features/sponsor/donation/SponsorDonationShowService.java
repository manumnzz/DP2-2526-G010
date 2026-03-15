
package acme.features.sponsor.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.datatypes.DonationKind;
import acme.entities.sponsorships.Donation;
import acme.realms.Sponsor;

@Service
public class SponsorDonationShowService extends AbstractService<Sponsor, Donation> {

	@Autowired
	private SponsorDonationRepository	repository;

	private Donation					donation;


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.donation = this.repository.findOneDonationById(id);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.donation != null) {
			int userAccountId = super.getRequest().getPrincipal().getAccountId();
			Sponsor sponsor = this.repository.findSponsorByUserAccountId(userAccountId);

			status = sponsor != null && this.donation.getSponsorship().getSponsor().getId() == sponsor.getId();
		}

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		SelectChoices choices;
		Tuple tuple;

		choices = SelectChoices.from(DonationKind.class, this.donation.getKind());

		tuple = super.unbindObject(this.donation, "name", "notes", "money", "draftMode");
		tuple.put("kind", choices.getSelected().getKey());
		tuple.put("kinds", choices);

		tuple.put("sponsorshipId", this.donation.getSponsorship().getId());
		tuple.put("readonly", !this.donation.isDraftMode() || !this.donation.getSponsorship().isDraftMode());
	}
}
