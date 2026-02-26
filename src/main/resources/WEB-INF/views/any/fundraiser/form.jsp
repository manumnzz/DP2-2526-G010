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
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:form-textbox code="any.fundraiser.form.label.bank" path="bank"/>
	<acme:form-textarea code="any.fundraiser.form.label.statement" path="statement"/>
	<acme:form-checkbox code="any.fundraiser.form.label.agent" path="agent"/>
</acme:form>