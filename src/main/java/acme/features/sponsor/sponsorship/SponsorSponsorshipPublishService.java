
package acme.features.sponsor.sponsorship;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Donation;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipRepository;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository	repository;

	@Autowired
	private SponsorshipRepository			sponsorshipRepository; // para transients si los muestras

	private Sponsorship						sponsorship;


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		this.sponsorship = this.repository.findOneSponsorshipById(id);

		if (this.sponsorship != null)
			this.sponsorship.setRepository(this.sponsorshipRepository);
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
		super.validateObject(this.sponsorship);

		int count = this.repository.countDonationsBySponsorshipId(this.sponsorship.getId());
		super.state(count > 0, "*", "sponsor.sponsorship.error.no-donations");

		Date now = MomentHelper.getCurrentMoment();

		if (!super.getResponse().getErrors().hasErrors("startMoment") && this.sponsorship.getStartMoment() != null)
			super.state(this.sponsorship.getStartMoment().after(now), "startMoment", "sponsor.sponsorship.error.start-future");

		if (!super.getResponse().getErrors().hasErrors("endMoment") && this.sponsorship.getEndMoment() != null)
			super.state(this.sponsorship.getEndMoment().after(now), "endMoment", "sponsor.sponsorship.error.end-future");

		if (this.sponsorship.getStartMoment() != null && this.sponsorship.getEndMoment() != null)
			super.state(this.sponsorship.getStartMoment().before(this.sponsorship.getEndMoment()), "endMoment", "sponsor.sponsorship.error.start-before-end");
	}

	@Override
	public void execute() {
		this.sponsorship.setDraftMode(false);
		this.repository.save(this.sponsorship);

		Collection<Donation> donations = this.repository.findManyDonationsBySponsorshipId(this.sponsorship.getId());
		for (Donation d : donations) {
			d.setDraftMode(false);
			this.repository.save(d);
		}
	}

	@Override
	public void unbind() {
		Tuple tuple;
		boolean readonly;

		tuple = super.unbindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");

		tuple.put("monthsActive", this.sponsorship.getMonthsActive());
		tuple.put("totalMoney", this.sponsorship.getTotalMoney());

		tuple.put("id", this.sponsorship.getId());

		readonly = !this.sponsorship.isDraftMode();
		tuple.put("readonly", readonly);
	}
}
