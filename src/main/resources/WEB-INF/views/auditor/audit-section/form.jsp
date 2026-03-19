<%--
- form.jsp
-
- Copyright (C) 2012-2026 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="auditor.auditSection.form.label.name" path="name"/>
	<acme:form-textarea code="auditor.auditSection.form.label.notes" path="notes"/>
	<acme:form-integer code="auditor.auditSection.form.label.hours" path="hours"/>
	<acme:form-select   code="sponsor.donation.form.label.kind"  path="kind" choices="${kinds}"/>				
	<input type="hidden" name="reportId" value="${reportId}"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.auditSection.form.button.create" action="/auditor/audit-section/create"/>
		</jstl:when>
		<jstl:when test="${_command == 'show'}">
			<jstl:if test="${auditReport.draftMode}">
				<acme:button code="auditor.auditSection.form.button.update" action="/auditor/audit-section/update?id=${id}"/>
				<acme:button code="auditor.auditSection.form.button.delete" action="/auditor/audit-section/delete?id=${id}"/>
			</jstl:if>
			<acme:button code="auditor.auditSection.form.button.back" action="/auditor/audit-section/list?reportId=${auditReport.id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'update'}">
			<acme:submit code="auditor.auditSection.form.button.update" action="/auditor/audit-section/update"/>
			<acme:button code="auditor.auditSection.form.button.cancel" action="/auditor/audit-section/show?id=${id}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>