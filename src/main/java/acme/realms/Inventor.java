
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Inventor extends AbstractRole {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@Column
	private String				bio;

	@Mandatory
	@Valid
	@Column
	private String				kewWords;

	@Mandatory
	@Column
	private boolean				licensed;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
