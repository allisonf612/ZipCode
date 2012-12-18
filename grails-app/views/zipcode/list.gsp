
<%@ page import="allison.zipcode.Zipcode" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'zipcode.label', default: 'Zipcode')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-zipcode" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-zipcode" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="postalCode" title="${message(code: 'zipcode.postalCode.label', default: 'Postal Code')}" />
					
						<g:sortableColumn property="name" title="${message(code: 'zipcode.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="countryCode" title="${message(code: 'zipcode.countryCode.label', default: 'Country Code')}" />
					
						<g:sortableColumn property="lat" title="${message(code: 'zipcode.lat.label', default: 'Lat')}" />
					
						<g:sortableColumn property="lng" title="${message(code: 'zipcode.lng.label', default: 'Lng')}" />
					
						<g:sortableColumn property="adminCode1" title="${message(code: 'zipcode.adminCode1.label', default: 'Admin Code1')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${zipcodeInstanceList}" status="i" var="zipcodeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${zipcodeInstance.id}">${fieldValue(bean: zipcodeInstance, field: "postalCode")}</g:link></td>
					
						<td>${fieldValue(bean: zipcodeInstance, field: "name")}</td>
					
						<td>${fieldValue(bean: zipcodeInstance, field: "countryCode")}</td>
					
						<td>${fieldValue(bean: zipcodeInstance, field: "lat")}</td>
					
						<td>${fieldValue(bean: zipcodeInstance, field: "lng")}</td>
					
						<td>${fieldValue(bean: zipcodeInstance, field: "adminCode1")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${zipcodeInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
