
package acme.entities.campaign;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.entities.milestone.Milestone;
import acme.realms.Spokesperson;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Campaign extends AbstractEntity {

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
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				startMoment;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endMoment;

	@ValidUrl
	@Column
	private String				moreInfo;

	@Mandatory
	@Column
	private boolean				draftMode;


	// Derived attributes -----------------------------------------------------
	@Transient
	public Double getMonthsActive() {
		Double result = null;

		if (this.startMoment != null && this.endMoment != null) {
			long diffInMillis = this.endMoment.getTime() - this.startMoment.getTime();
			long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
			result = Math.round(diffInDays / 30.44 * 10.0) / 10.0;
		}

		return result;
	}

	@Transient
	public Double getEffort() {
		double result = 0.0;

		if (this.milestones != null && !this.milestones.isEmpty())
			for (Milestone milestone : this.milestones)
				if (milestone.getEffort() != null)
					result += milestone.getEffort();

		return result;
	}

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Spokesperson			spokesperson;

	@OneToMany(mappedBy = "campaign")
	private Collection<Milestone>	milestones;

}
