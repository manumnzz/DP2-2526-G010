
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.sponsorships.Donation;
import acme.entities.sponsorships.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipDeleteService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository	repository;

	private Sponsorship						sponsorship;


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.sponsorship = this.repository.findOneSponsorshipById(id);
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
		;
	}

	@Override
	public void validate() {
		;
	}

	@Override
	public void execute() {
		Collection<Donation> donations = this.repository.findManyDonationsBySponsorshipId(this.sponsorship.getId());
		this.repository.deleteAll(donations);
		this.repository.delete(this.sponsorship);
	}

	@Override
	public void unbind() {
		;
	}
}
