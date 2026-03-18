package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.inventions.Invention;
import acme.entities.inventions.InventionRepository;

@Validator
public class InventionValidator extends AbstractValidator<ValidInvention, Invention> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private InventionRepository repository;

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidInvention annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Invention invention, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (invention == null)
			result = true;
		else {

			// Ticker único
			{
				boolean uniqueInvention;
				Invention existing;

				existing = this.repository.findInventionByTicker(invention.getTicker());
				uniqueInvention = existing == null || existing.equals(invention);

				super.state(context, uniqueInvention, "ticker", "acme.validation.invention.duplicated-ticker.message");
			}

			// Start moment en el futuro
			{
				boolean validStartMoment;
				Date start;
				Date now;

				start = invention.getStartMoment();
				now = MomentHelper.getCurrentMoment();

				validStartMoment = start == null || MomentHelper.isAfter(start, now);

				super.state(context, validStartMoment, "startMoment", "acme.validation.invention.start-moment-future.message");
			}

			// End moment en el futuro
			{
				boolean validEndMoment;
				Date end;
				Date now;

				end = invention.getEndMoment();
				now = MomentHelper.getCurrentMoment();

				validEndMoment = end == null || MomentHelper.isAfter(end, now);

				super.state(context, validEndMoment, "endMoment", "acme.validation.invention.end-moment-future.message");
			}

			// End moment posterior a start moment
			{
				boolean correctPeriod;
				Date start, end;

				start = invention.getStartMoment();
				end = invention.getEndMoment();

				correctPeriod = start == null || end == null || MomentHelper.isAfter(end, start);

				super.state(context, correctPeriod, "endMoment", "acme.validation.invention.period.message");
			}

			// Ticker no demasiado largo
			{
				boolean validTickerLength;
				String ticker;

				ticker = invention.getTicker();
				validTickerLength = ticker == null || ticker.trim().length() <= 75;

				super.state(context, validTickerLength, "ticker", "acme.validation.invention.ticker-too-long.message");
			}

			// Name no demasiado largo
			{
				boolean validNameLength;
				String name;

				name = invention.getName();
				validNameLength = name == null || name.trim().length() <= 75;

				super.state(context, validNameLength, "name", "acme.validation.invention.name-too-long.message");
			}

			// Description no demasiado larga
			{
				boolean validDescriptionLength;
				String description;

				description = invention.getDescription();
				validDescriptionLength = description == null || description.trim().length() <= 255;

				super.state(context, validDescriptionLength, "description", "acme.validation.invention.description-too-long.message");
			}

			// More info no demasiado largo
			{
				boolean validMoreInfoLength;
				String moreInfo;

				moreInfo = invention.getMoreInfo();
				validMoreInfoLength = moreInfo == null || moreInfo.trim().length() <= 255;

				super.state(context, validMoreInfoLength, "moreInfo", "acme.validation.invention.more-info-too-long.message");
			}

			// Inventor obligatorio por seguridad
			{
				boolean hasInventor;

				hasInventor = invention.getInventor() != null;

				super.state(context, hasInventor, "inventor", "acme.validation.invention.inventor-required.message");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}
}