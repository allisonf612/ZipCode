
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
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-state" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list state">
			
				<g:if test="${stateInstance?.totalResultsCount}">
				<li class="fieldcontain">
					<span id="totalResultsCount-label" class="property-label"><g:message code="state.totalResultsCount.label" default="Total Results Count" /></span>
					
						<span class="property-value" aria-labelledby="totalResultsCount-label"><g:fieldValue bean="${stateInstance}" field="totalResultsCount"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${stateInstance?.abbreviation}">
				<li class="fieldcontain">
					<span id="abbreviation-label" class="property-label"><g:message code="state.abbreviation.label" default="Abbreviation" /></span>
					
						<span class="property-value" aria-labelledby="abbreviation-label"><g:fieldValue bean="${stateInstance}" field="abbreviation"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${stateInstance?.fullName}">
				<li class="fieldcontain">
					<span id="fullName-label" class="property-label"><g:message code="state.fullName.label" default="Full Name" /></span>
					
						<span class="property-value" aria-labelledby="fullName-label"><g:fieldValue bean="${stateInstance}" field="fullName"/></span>
					
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
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${stateInstance?.id}" />
					<g:link class="edit" action="edit" id="${stateInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
