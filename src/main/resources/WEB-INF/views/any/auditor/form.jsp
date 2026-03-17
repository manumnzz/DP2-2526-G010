<%@page%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:input-textbox code="any.auditReport.form.label.ticker" path="ticker"/>
	<acme:input-textbox code="any.auditReport.form.label.name" path="name"/>
	<acme:input-textarea code="any.auditReport.form.label.description" path="description"/>
	<acme:input-moment code="any.auditReport.form.label.startMoment" path="startMoment"/>
	<acme:input-moment code="any.auditReport.form.label.endMoment" path="endMoment"/>
	<acme:input-double code="any.auditReport.form.label.monthsActive" path="monthsActive"/>
	<acme:input-integer code="any.auditReport.form.label.hours" path="hours"/>
	<acme:input-url code="any.auditReport.form.label.moreInfo" path="moreInfo"/>
	<acme:input-textbox code="any.auditReport.form.label.auditor" path="auditorName"/>
</acme:form>