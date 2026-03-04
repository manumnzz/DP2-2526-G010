<%@page%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="spokesperson.milestone.list.label.title" path="title" width="30%"/>
	<acme:list-column code="spokesperson.milestone.list.label.effort" path="effort" width="20%"/>
	<acme:list-column code="spokesperson.milestone.list.label.kind" path="kind" width="20%"/>
	<acme:list-column code="spokesperson.milestone.list.label.campaign" path="campaign.ticker" width="30%"/>
</acme:list>

<acme:button code="spokesperson.milestone.list.button.create" action="/spokesperson/milestone/create?campaignId=${campaignId}"/>