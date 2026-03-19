<%@page%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:form-textbox code="any.auditor.form.label.fullName" path="userAccount.identity.fullName"/>
	<acme:form-email code="any.auditor.form.label.email" path="userAccount.identity.email"/>
	<acme:form-textbox code="any.auditor.form.label.firm" path="firm"/>
	<acme:form-textarea code="any.auditor.form.label.highlights" path="highlights"/>
	<acme:form-checkbox code="any.auditor.form.label.solicitor" path="solicitor"/>
</acme:form>