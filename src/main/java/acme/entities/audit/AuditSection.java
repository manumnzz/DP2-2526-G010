
package acme.entities.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditSection extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidHeader
	@Column(nullable = false)
	private String				name;

	@Mandatory
	@ValidText
	@Column(nullable = false, length = 2000)
	private String				notes;

	@Mandatory
	@ValidNumber(min = 1, max = 1000)
	@Positive
	@Column(nullable = false)
	private Integer				hours;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SectionKind			kind;

	// Relationships ----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private AuditReport			auditReport;
}
