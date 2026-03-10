
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.datatypes.Money;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.sponsorships.Donation;

@Validator
public class DonationValidator extends AbstractValidator<ValidDonation, Donation> {

	@Override
	protected void initialise(final ValidDonation annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Donation donation, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (donation == null)
			result = true;
		else {
			{
				boolean euroCurrency;
				Money money;

				money = donation.getMoney();

				euroCurrency = money == null || "EUR".equals(money.getCurrency());

				super.state(context, euroCurrency, "money", "acme.validation.donation.currency.message");
			}

			result = !super.hasErrors(context);
		}

		return result;
	}
}
