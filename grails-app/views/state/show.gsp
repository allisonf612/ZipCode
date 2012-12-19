
<%@ page import="allison.zipcode.State" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'state.label', default: 'State')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-state" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-state" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list state">
			
				<g:if test="${stateInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="state.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${stateInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${stateInstance?.country}">
				<li class="fieldcontain">
					<span id="country-label" class="property-label"><g:message code="state.country.label" default="Country" /></span>
					
						<span class="property-value" aria-labelledby="country-label"><g:link controller="country" action="show" id="${stateInstance?.country?.id}">${stateInstance?.country?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${stateInstance?.zipcodes}">
				<li class="fieldcontain">
					<span id="zipcodes-label" class="property-label"><g:message code="state.zipcodes.label" default="Zipcodes" /></span>
					
						<g:each in="${stateInstance.zipcodes}" var="z">
						<span class="property-value" aria-labelledby="zipcodes-label"><g:link controller="zipcode" action="show" id="${z.id}">${z?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>
		</div>
	</body>
</html>
