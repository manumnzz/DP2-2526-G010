
package acme.features.auditor.auditReport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.audit.AuditReport;
import acme.entities.audit.AuditSection;
import acme.realms.Auditor;

@Repository
public interface AuditorAuditReportRepository extends AbstractRepository {

	@Query("SELECT ar FROM AuditReport ar WHERE ar.auditor.id = :auditorId")
	Collection<AuditReport> findAuditReportsByAuditorId(int auditorId);

	@Query("SELECT ar FROM AuditReport ar WHERE ar.id = :id")
	AuditReport findAuditReportById(int id);

	@Query("SELECT a FROM Auditor a WHERE a.id = :id")
	Auditor findAuditorById(int id);

	@Query("SELECT s FROM AuditSection s WHERE s.auditReport.id = :reportId")
	Collection<AuditSection> findSectionsByReportId(int reportId);
}
