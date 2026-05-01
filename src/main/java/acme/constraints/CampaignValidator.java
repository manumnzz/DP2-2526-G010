package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.campaign.Campaign;
import acme.entities.campaign.CampaignRepository;

@Validator
public class CampaignValidator extends AbstractValidator<ValidCampaign, Campaign> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CampaignRepository repository;

	// AbstractValidator interface --------------------------------------------

	@Override
	protected void initialise(final ValidCampaign annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Campaign campaign, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (campaign == null)
			result = true;
		else {

			// Ticker único
			{
				boolean uniqueCampaign;
				Campaign existing;

				existing = this.repository.findCampaignByTicker(campaign.getTicker());
				uniqueCampaign = existing == null || existing.equals(campaign);

				super.state(context, uniqueCampaign, "ticker", "acme.validation.campaign.duplicated-ticker.message");
			}

			// Intervalo de tiempo válido
			{
				boolean validTimeInterval;

				if (campaign.getStartMoment() != null && campaign.getEndMoment() != null)
					validTimeInterval = campaign.getStartMoment().before(campaign.getEndMoment());
				else
					validTimeInterval = true;

				super.state(context, validTimeInterval, "endMoment",
						"acme.validation.campaign.invalid-time-interval.message");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}
}