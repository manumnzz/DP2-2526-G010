<%--
- form.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
	<acme:form-textbox code="authenticated.strategy.form.label.ticker" path="ticker"/>
	<acme:form-textbox code="authenticated.strategy.form.label.name" path="name"/>
	<acme:form-textarea code="authenticated.strategy.form.label.description" path="description"/>
	<acme:form-moment code="authenticated.strategy.form.label.startMoment" path="startMoment"/>
	<acme:form-moment code="authenticated.strategy.form.label.endMoment" path="endMoment"/>
	<acme:form-url code="authenticated.strategy.form.label.moreInfo" path="moreInfo"/>
</acme:form>