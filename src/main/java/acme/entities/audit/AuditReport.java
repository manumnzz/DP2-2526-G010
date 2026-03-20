
package acme.entities.audit;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidUrl;
import acme.client.helpers.MomentHelper;
import acme.constraints.ValidAuditReport;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Auditor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAuditReport
public class AuditReport extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidTicker
	@Column(unique = true, nullable = false)
	private String				ticker;

	@Mandatory
	@ValidHeader
	@Column(nullable = false)
	private String				name;

	@Mandatory
	@ValidText
	@Column(nullable = false, length = 2000)
	private String				description;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date				startMoment;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date				endMoment;

	@ValidUrl
	@Column
	private String				moreInfo;

	@Mandatory
	@Column(nullable = false)
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Double getMonthsActive() {
		if (this.startMoment == null || this.endMoment == null)
			return 0.0;
		Duration duration = MomentHelper.computeDuration(this.startMoment, this.endMoment);
		double months = duration.toDays() / 30.44;
		return Math.round(months * 10) / 10.0;
	}

	//@Transient
	//public Integer getTHours() {
	//	if (this.sections == null || this.sections.isEmpty())
	//		return 0;
	//	return this.sections.stream().mapToInt(AuditSection::getHours).sum();
	//}


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Auditor				auditor;

	@NotEmpty
	@OneToMany(mappedBy = "auditReport")
	private List<AuditSection>	sections;
}
