
package acme.entities.milestone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.datatypes.MilestoneKind;
import acme.entities.campaign.Campaign;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Milestone extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidHeader
	@Column
	private String				title;

	@Mandatory
	@ValidText
	@Column
	private String				achievements;

	@Mandatory
	@ValidNumber(min = 0)
	@Column
	private Double				effort;

	@Mandatory
	@Valid
	@Column
	private MilestoneKind		kind;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Campaign			campaign;

}
