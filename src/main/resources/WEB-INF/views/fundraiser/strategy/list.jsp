<%--
- list.jsp
--%>

<%@page%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.strategy.list.label.ticker" path="ticker" width="20%"/>
	<acme:list-column code="authenticated.strategy.list.label.name" path="name" width="40%"/>
	<acme:list-column code="authenticated.strategy.list.label.startMoment" path="startMoment" width="20%"/>
	<acme:list-column code="authenticated.strategy.list.label.endMoment" path="endMoment" width="20%"/>
</acme:list>