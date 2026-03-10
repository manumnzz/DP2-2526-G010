
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipRepository;

@Validator
public class SponsorshipValidator extends AbstractValidator<ValidSponsorship, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorshipRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidSponsorship annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Sponsorship sponsorship, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (sponsorship == null)
			result = true;
		else {
			{
				boolean uniqueTicker;
				Sponsorship existing;

				existing = this.repository.findSponsorshipByTicker(sponsorship.getTicker());
				uniqueTicker = existing == null || existing.equals(sponsorship);

				super.state(context, uniqueTicker, "ticker", "acme.validation.sponsorship.duplicated-ticker.message");
			}
			{
				boolean correctPeriod;
				Date start, end;

				start = sponsorship.getStartMoment();
				end = sponsorship.getEndMoment();

				correctPeriod = start == null || end == null || MomentHelper.isAfter(end, start);

				super.state(context, correctPeriod, "endMoment", "acme.validation.sponsorship.period.message");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}
}
