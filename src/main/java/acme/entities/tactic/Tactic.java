
package acme.entities.tactic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidScore;
import acme.constraints.ValidLongText;
import acme.entities.strategy.Strategy;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tactic extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	// @ValidHeader
	@Column
	private String				name;

	@Mandatory
	@ValidLongText
	@Column
	private String				notes;

	@Mandatory
	@ValidScore
	@Column
	private Double				expectedPercentage;

	@Mandatory
	@Enumerated(EnumType.STRING)
	private TacticKind			kind;

	// Relationships ----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Strategy			strategy;

}
