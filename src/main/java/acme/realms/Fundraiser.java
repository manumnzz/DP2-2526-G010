
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidLongText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Fundraiser extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	// @ValidHeader (si existe)
	@Column
	private String				bank;

	@Mandatory
	@ValidLongText
	@Column
	private String				statement;

	@Mandatory
	@Column
	private Boolean				agent;
}
