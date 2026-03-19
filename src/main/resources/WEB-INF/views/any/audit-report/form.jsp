<%@page%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:form-textbox code="any.auditReport.form.label.ticker" path="ticker"/>
	<acme:form-textbox code="any.auditReport.form.label.name" path="name"/>
	<acme:form-textarea code="any.auditReport.form.label.description" path="description"/>
	<acme:form-moment code="any.auditReport.form.label.startMoment" path="startMoment"/>
	<acme:form-moment code="any.auditReport.form.label.endMoment" path="endMoment"/>
	<acme:form-double code="any.auditReport.form.label.monthsActive" path="monthsActive"/>
	<acme:form-integer code="any.auditReport.form.label.hours" path="hours"/>
	<acme:form-url code="any.auditReport.form.label.moreInfo" path="moreInfo"/>
	<acme:form-textbox code="any.auditReport.form.label.auditor" path="auditorName"/>
</acme:form>