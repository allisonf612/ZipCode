
<%@ page import="allison.zipcode.Zipcode" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'zipcode.label', default: 'Zipcode')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-zipcode" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-zipcode" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list zipcode">
			
				<g:if test="${zipcodeInstance?.postalCode}">
				<li class="fieldcontain">
					<span id="postalCode-label" class="property-label"><g:message code="zipcode.postalCode.label" default="Postal Code" /></span>
					
						<span class="property-value" aria-labelledby="postalCode-label"><g:fieldValue bean="${zipcodeInstance}" field="postalCode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="zipcode.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${zipcodeInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.countryCode}">
				<li class="fieldcontain">
					<span id="countryCode-label" class="property-label"><g:message code="zipcode.countryCode.label" default="Country Code" /></span>
					
						<span class="property-value" aria-labelledby="countryCode-label"><g:fieldValue bean="${zipcodeInstance}" field="countryCode"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.lat}">
				<li class="fieldcontain">
					<span id="lat-label" class="property-label"><g:message code="zipcode.lat.label" default="Lat" /></span>
					
						<span class="property-value" aria-labelledby="lat-label"><g:fieldValue bean="${zipcodeInstance}" field="lat"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.lng}">
				<li class="fieldcontain">
					<span id="lng-label" class="property-label"><g:message code="zipcode.lng.label" default="Lng" /></span>
					
						<span class="property-value" aria-labelledby="lng-label"><g:fieldValue bean="${zipcodeInstance}" field="lng"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.adminCode1}">
				<li class="fieldcontain">
					<span id="adminCode1-label" class="property-label"><g:message code="zipcode.adminCode1.label" default="Admin Code1" /></span>
					
						<span class="property-value" aria-labelledby="adminCode1-label"><g:fieldValue bean="${zipcodeInstance}" field="adminCode1"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.adminName1}">
				<li class="fieldcontain">
					<span id="adminName1-label" class="property-label"><g:message code="zipcode.adminName1.label" default="Admin Name1" /></span>
					
						<span class="property-value" aria-labelledby="adminName1-label"><g:fieldValue bean="${zipcodeInstance}" field="adminName1"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.adminCode2}">
				<li class="fieldcontain">
					<span id="adminCode2-label" class="property-label"><g:message code="zipcode.adminCode2.label" default="Admin Code2" /></span>
					
						<span class="property-value" aria-labelledby="adminCode2-label"><g:fieldValue bean="${zipcodeInstance}" field="adminCode2"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.adminName2}">
				<li class="fieldcontain">
					<span id="adminName2-label" class="property-label"><g:message code="zipcode.adminName2.label" default="Admin Name2" /></span>
					
						<span class="property-value" aria-labelledby="adminName2-label"><g:fieldValue bean="${zipcodeInstance}" field="adminName2"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.adminCode3}">
				<li class="fieldcontain">
					<span id="adminCode3-label" class="property-label"><g:message code="zipcode.adminCode3.label" default="Admin Code3" /></span>
					
						<span class="property-value" aria-labelledby="adminCode3-label"><g:fieldValue bean="${zipcodeInstance}" field="adminCode3"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.adminName3}">
				<li class="fieldcontain">
					<span id="adminName3-label" class="property-label"><g:message code="zipcode.adminName3.label" default="Admin Name3" /></span>
					
						<span class="property-value" aria-labelledby="adminName3-label"><g:fieldValue bean="${zipcodeInstance}" field="adminName3"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${zipcodeInstance?.state}">
				<li class="fieldcontain">
					<span id="state-label" class="property-label"><g:message code="zipcode.state.label" default="State" /></span>
					
						<span class="property-value" aria-labelledby="state-label"><g:link controller="state" action="show" id="${zipcodeInstance?.state?.id}">${zipcodeInstance?.state?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${zipcodeInstance?.id}" />
					<g:link class="edit" action="edit" id="${zipcodeInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
