<%--
- list.jsp
--%>

<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="sponsor.donation.list.label.name" path="name" width="45%"/>
	<acme:list-column code="sponsor.donation.list.label.money" path="money" width="25%"/>
	<acme:list-column code="sponsor.donation.list.label.kind" path="kind" width="20%"/>
	<acme:list-column code="sponsor.donation.list.label.draftMode" path="draftMode" width="10%"/>
</acme:list>

<jstl:if test="${param.draftMode == 'true'}">
	<acme:button code="sponsor.donation.list.button.create" action="/sponsor/donation/create?sponsorshipId=${param.sponsorshipId}"/>
</jstl:if>

<acme:button code="sponsor.donation.list.button.return" action="/sponsor/sponsorship/show?id=${param.sponsorshipId}"/>