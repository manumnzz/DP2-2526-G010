<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:form-textbox code="auditor.auditReport.form.label.ticker" path="ticker"/>
	<acme:form-textbox code="auditor.auditReport.form.label.name" path="name"/>
	<acme:form-textarea code="auditor.auditReport.form.label.description" path="description"/>
	<acme:form-moment code="auditor.auditReport.form.label.startMoment" path="startMoment"/>
	<acme:form-moment code="auditor.auditReport.form.label.endMoment" path="endMoment"/>
	<acme:form-url code="auditor.auditReport.form.label.moreInfo" path="moreInfo"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:form-checkbox code="auditor.auditReport.form.label.draftMode" path="draftMode"/>
		</jstl:when>
		<jstl:otherwise>
			<acme:form-checkbox code="auditor.auditReport.form.label.draftMode" path="draftMode" readonly="true"/>
		</jstl:otherwise>
	</jstl:choose>
	
	<jstl:if test="${_command != 'create'}">
		<acme:form-moment code="auditor.auditReport.form.label.monthsActive" path="monthsActive" readonly="true"/>
		<acme:form-integer code="auditor.auditReport.form.label.hours" path="hours" readonly="true"/>
	</jstl:if>
	
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="auditor.auditReport.form.button.create" action="/auditor/audit-report/create"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'show'}">
			<acme:button code="auditor.auditReport.form.button.sections" action="/auditor/audit-section/list?reportId=${id}"/>
			
			<jstl:if test="${draftMode}">
				<acme:button code="auditor.auditReport.form.button.update" action="/auditor/audit-report/update?id=${id}"/>
				<acme:button code="auditor.auditReport.form.button.publish" action="/auditor/audit-report/publish?id=${id}"/>
				<acme:button code="auditor.auditReport.form.button.delete" action="/auditor/audit-report/delete?id=${id}"/>
			</jstl:if>
		</jstl:when>
		
		<jstl:when test="${_command == 'update'}">
			<acme:submit code="auditor.auditReport.form.button.update" action="/auditor/audit-report/update"/>
			<acme:button code="auditor.auditReport.form.button.cancel" action="/auditor/audit-report/show?id=${id}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>