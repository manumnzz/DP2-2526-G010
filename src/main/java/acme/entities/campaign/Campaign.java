
package acme.entities.campaign;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidUrl;
import acme.entities.spokesperson.Spokesperson;
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
	@Column(unique = true)
	private String				ticker;

	@Mandatory
	@Column
	private String				name;

	@Mandatory
	@Column
	private String				description;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Moment				startMoment;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Moment				endMoment;

	@ValidUrl
	@Column
	private String				moreInfo;

	@Mandatory
	@Column
	private Double				monthsActive;

	@Mandatory
	@Column
	private Double				effort;

	@Mandatory
	@Column
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Spokesperson		spokesperson;

}
