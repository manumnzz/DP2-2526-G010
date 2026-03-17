<%--
- list.jsp
--%>

<%@page%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.donation.list.label.name" path="name" width="50%"/>
	<acme:list-column code="any.donation.list.label.money" path="money" width="25%"/>
	<acme:list-column code="any.donation.list.label.kind" path="kind" width="25%"/>
</acme:list>