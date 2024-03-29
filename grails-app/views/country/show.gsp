
<%@ page import="allison.zipcode.Country" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'country.label', default: 'Country')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-country" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                <li><g:link action="load" id="${countryInstance?.id}">Load</g:link></li>
                <li><g:link action="clear" id="${countryInstance?.id}">Clear</g:link></li>
			</ul>
		</div>
		<div id="show-country" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list country">
			
				<g:if test="${countryInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="country.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${countryInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${countryInstance?.countryCode}">
				<li class="fieldcontain">
					<span id="countryCode-label" class="property-label"><g:message code="country.countryCode.label" default="Country Code" /></span>
					
						<span class="property-value" aria-labelledby="countryCode-label"><g:fieldValue bean="${countryInstance}" field="countryCode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${countryInstance?.states}">
				<li class="fieldcontain">
					<span id="states-label" class="property-label"><g:message code="country.states.label" default="States" /></span>
					
						<g:each in="${countryInstance.states}" var="s">
						<span class="property-value" aria-labelledby="states-label"><g:link controller="state" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
			</ol>

            <tc:tagCloud tags="${cloudData}" action="show" controller="state" id="${countryInstance?.id}" paramName="stateName" color="${[start: '#777', end: '#00f']}"/>

			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${countryInstance?.id}" />
					<g:link class="edit" action="edit" id="${countryInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
