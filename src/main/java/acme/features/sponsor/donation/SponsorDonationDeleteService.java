
package acme.features.sponsor.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.sponsorships.Donation;
import acme.realms.Sponsor;

@Service
public class SponsorDonationDeleteService extends AbstractService<Sponsor, Donation> {

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

			status = sponsor != null && this.donation.isDraftMode() && this.donation.getSponsorship().isDraftMode() && this.donation.getSponsorship().getSponsor().getId() == sponsor.getId();
		}

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		;
	}

	@Override
	public void validate() {
		;
	}

	@Override
	public void execute() {
		this.repository.delete(this.donation);
	}

	@Override
	public void unbind() {
		;
	}
}
