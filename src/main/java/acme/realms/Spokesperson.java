
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Spokesperson extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column
	private String				cv;

	@Mandatory
	@Column
	private String				achievements;

	@Mandatory
	@Column
	private boolean				licensed;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}