
package acme.features.sponsor.sponsorship;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipCreateService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository	repository;

	private Sponsorship						sponsorship;


	@Override
	public void load() {
		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		Sponsor sponsor = this.repository.findSponsorByUserAccountId(userAccountId);

		this.sponsorship = new Sponsorship();
		this.sponsorship.setSponsor(sponsor);
		this.sponsorship.setDraftMode(true);
	}

	@Override
	public void authorise() {
		super.setAuthorised(true);
	}

	@Override
	public void bind() {
		super.bindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.sponsorship);

		if (!super.getResponse().getErrors().hasErrors("startMoment") && !super.getResponse().getErrors().hasErrors("endMoment")) {
			Date start = this.sponsorship.getStartMoment();
			Date end = this.sponsorship.getEndMoment();

			if (start != null && end != null && !start.before(end))
				super.state(false, "endMoment", "sponsor.sponsorship.error.start-before-end");
		}
	}

	@Override
	public void execute() {
		this.repository.save(this.sponsorship);
	}

	@Override
	public void unbind() {
		Tuple tuple = super.unbindObject(this.sponsorship, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
		tuple.put("draftMode", true);
	}
}
