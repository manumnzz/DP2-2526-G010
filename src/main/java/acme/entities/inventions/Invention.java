
package acme.entities.inventions;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidHeader;
import acme.constraints.ValidInvention;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Inventor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidInvention
@Table(indexes = {
	@Index(columnList = "draftMode"), //
	@Index(columnList = "inventor_id, id")
})
public class Invention extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidTicker
	@Column(unique = true)
	private String				ticker;

	@Mandatory
	@ValidHeader
	@Column
	private String				name;

	@Mandatory
	@ValidText
	@Column
	private String				description;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startMoment;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endMoment;

	@Optional
	@ValidUrl
	@Column
	private String				moreInfo;

	@Mandatory
	// HINT: @Valid by default.
	@Column
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	@Transient
	@Autowired
	private InventionRepository	repository;


	@Transient
	public Double getMonthsActive() {
		if (this.startMoment == null || this.endMoment == null)
			return 0.0;

		long milliseconds = this.endMoment.getTime() - this.startMoment.getTime();
		double days = milliseconds / (1000.0 * 60.0 * 60.0 * 24.0);

		return Math.max(0.0, days / 30.0);
	}

	@Transient
	public Money getCost() {
		Money result = new Money();

		Double amount = this.repository.getSummaryCostAmountByInventionId(this.getId());

		result.setCurrency("EUR");
		result.setAmount(amount != null ? amount : 0.0);

		return result;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne
	private Inventor inventor;

}
