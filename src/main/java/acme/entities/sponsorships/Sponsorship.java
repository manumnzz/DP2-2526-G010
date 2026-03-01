
package acme.entities.sponsorships;

import java.time.Duration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Sponsor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sponsorship extends AbstractEntity {

	private static final long				serialVersionUID	= 1L;

	@Mandatory
	@ValidTicker
	@Column(unique = true)
	private String							ticker;

	@Mandatory
	@ValidText
	@Column
	private String							name;

	@Mandatory
	@ValidText
	@Column
	private String							description;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	private Moment							startMoment;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	private Moment							endMoment;

	@ValidUrl
	@Column
	private String							moreInfo;

	@Mandatory
	@Column
	private boolean							draftMode;

	// Derived attributes -----------------------------------------------------

	@Transient
	private transient SponsorshipRepository	repository;


	public void setRepository(final SponsorshipRepository repository) {
		this.repository = repository;
	}

	@Transient
	public double getMonthsActive() {

		Duration duration = MomentHelper.computeDuration(this.startMoment, this.endMoment);
		long days = duration.toDays();
		double months = days / 30.0;
		return Math.round(months * 10.0) / 10.0;
	}

	@Transient
	public Money getTotalMoney() {
		Money result;
		Double wrapper;

		wrapper = this.repository == null ? null : this.repository.computeTotalAmount(this.getId());

		result = new Money();
		result.setCurrency("EUR");
		result.setAmount(wrapper == null ? 0.0 : wrapper.doubleValue());

		return result;
	}


	// Relationships ----------------------------------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Sponsor sponsor;
}
