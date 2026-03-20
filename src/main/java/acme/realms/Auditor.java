
package acme.realms;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;  // IMPORTANTE: @ValidString
import acme.entities.audit.AuditReport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Auditor extends AbstractRole {

	private static final long			serialVersionUID	= 1L;

	@Mandatory
	@ValidString
	@Column(nullable = false)
	private String						firm;

	@Mandatory
	@ValidString
	@Column(nullable = false, length = 1000)
	private String						highlights;

	@Mandatory
	@ValidString
	@Column(nullable = false)
	private String						solicitor;

	@OneToMany(mappedBy = "auditor")
	private List<@Valid AuditReport>	auditReports;


	public Boolean isSolicitor() {
		return "true".equalsIgnoreCase(this.solicitor);
	}
}
