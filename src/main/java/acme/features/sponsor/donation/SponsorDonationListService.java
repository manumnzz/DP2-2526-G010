
package acme.features.sponsor.donation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.sponsorships.Donation;
import acme.entities.sponsorships.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorDonationListService extends AbstractService<Sponsor, Donation> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorDonationRepository	repository;

	private Sponsorship					sponsorship;
	private Collection<Donation>		donations;

	// AbstractService interface ---------------------------------------------


	@Override
	public void load() {
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		this.sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);

		if (this.sponsorship != null)
			this.donations = this.repository.findManyDonationsBySponsorshipId(sponsorshipId);
	}

	@Override
	public void authorise() {
		boolean status = false;

		if (this.sponsorship != null) {
			int userAccountId;
			Sponsor sponsor;

			userAccountId = super.getRequest().getPrincipal().getAccountId();
			sponsor = this.repository.findSponsorByUserAccountId(userAccountId);

			status = sponsor != null && this.sponsorship.getSponsor().getId() == sponsor.getId();
		}

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		// IMPORTANT: do NOT add extra response data here; it breaks the list Tuple.
		super.unbindObjects(this.donations, "name", "kind", "money", "draftMode");
	}
}
