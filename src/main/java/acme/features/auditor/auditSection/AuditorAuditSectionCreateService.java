
package acme.features.auditor.auditSection;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.principals.Principal;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditReport;
import acme.entities.audit.AuditSection;
import acme.entities.audit.SectionKind;
import acme.realms.Auditor;

@Service
public class AuditorAuditSectionCreateService extends AbstractService<Auditor, AuditSection> {

	@Autowired
	private AuditorAuditSectionRepository	repository;

	private AuditSection					section;
	private int								reportId;  // Añade esto


	@Override
	public void authorise() {
		boolean status;
		AuditReport report;
		Principal principal;

		this.reportId = super.getRequest().getData("reportId", int.class);  // Guardar en variable de instancia
		report = this.repository.findAuditReportById(this.reportId);
		principal = super.getRequest().getPrincipal();

		status = report != null && report.isDraftMode() && principal.hasRealmOfType(Auditor.class) && report.getAuditor().getId() == principal.getActiveRealm().getId();

		super.setAuthorised(status);
	}

	@Override
	public void load() {
		AuditReport report = this.repository.findAuditReportById(this.reportId);  // Usar la variable guardada

		this.section = super.newObject(AuditSection.class);
		this.section.setAuditReport(report);
		this.section.setHours(1);
		this.section.setKind(SectionKind.PRELIMINARY);
	}

	@Override
	public void bind() {
		// Primero, recuperar el report usando el reportId guardado
		AuditReport report = this.repository.findAuditReportById(this.reportId);

		// Hacer el bind normal
		super.bindObject(this.section, "name", "notes", "hours");

		// Asignar el kind
		String kindStr = super.getRequest().getData("kind", String.class);
		if (kindStr != null)
			this.section.setKind(SectionKind.valueOf(kindStr));

		// RE-ASIGNAR EL REPORT (esto es crucial)
		this.section.setAuditReport(report);
	}

	@Override
	public void execute() {
		System.out.println("=== EXECUTE ===");
		System.out.println("Name: " + this.section.getName());
		System.out.println("Notes: " + this.section.getNotes());
		System.out.println("Hours: " + this.section.getHours());
		System.out.println("Kind: " + this.section.getKind());
		System.out.println("Report ID: " + (this.section.getAuditReport() != null ? this.section.getAuditReport().getId() : "null"));

		this.repository.save(this.section);
	}

	@Override
	public void validate() {
		// Validaciones
		if (this.section.getName() == null || this.section.getName().trim().isEmpty())
			super.state(false, "name", "auditor.auditSection.error.name-required");
		if (this.section.getHours() <= 0)
			super.state(false, "hours", "auditor.auditSection.error.hours-positive");
	}

	@Override
	public void unbind() {
		Tuple tuple;
		Collection<SectionKind> kinds;
		SelectChoices choices;

		kinds = Arrays.asList(SectionKind.values());
		choices = SelectChoices.from(SectionKind.class, this.section.getKind());
		tuple = super.unbindObject(this.section, "name", "notes", "hours", "kind");

		tuple.put("kinds", choices);
		tuple.put("reportId", this.reportId);  // Añadir reportId al tuple para el formulario

		super.getResponse().setData(tuple);
	}
}
