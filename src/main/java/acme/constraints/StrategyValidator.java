
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.strategy.Strategy;
import acme.entities.strategy.StrategyRepository;

@Validator
public class StrategyValidator extends AbstractValidator<ValidStrategy, Strategy> {

	@Autowired
	private StrategyRepository repository;


	@Override
	protected void initialise(final ValidStrategy annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Strategy strategy, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (strategy == null)
			result = true;
		else {

			{
				boolean unique;
				Strategy existing;

				existing = this.repository.findStrategyByTicker(strategy.getTicker());
				unique = existing == null || existing.equals(strategy);

				super.state(context, unique, "ticker", "acme.validation.strategy.duplicated-ticker.message");
			}

			{
				boolean correctPeriod;
				Date start, end;

				start = strategy.getStartMoment();
				end = strategy.getEndMoment();

				correctPeriod = start == null || end == null || MomentHelper.isAfter(end, start);
				super.state(context, correctPeriod, "endMoment", "acme.validation.strategy.period.message");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}
}
