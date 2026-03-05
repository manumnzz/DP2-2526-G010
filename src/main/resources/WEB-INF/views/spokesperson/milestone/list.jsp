<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<acme:list>
	<acme:list-column code="spokesperson.milestone.list.label.title" path="title" width="30%"/>
	<acme:list-column code="spokesperson.milestone.list.label.effort" path="effort" width="20%"/>
	<acme:list-column code="spokesperson.milestone.list.label.kind" path="kind" width="20%"/>
	<acme:list-column code="spokesperson.milestone.list.label.campaign" path="campaign.ticker" width="30%"/>
</acme:list>

<jstl:if test="${draftMode}">
	<acme:button code="spokesperson.milestone.list.button.create" action="/spokesperson/milestone/create?campaignId=${campaignId}"/>
</jstl:if>