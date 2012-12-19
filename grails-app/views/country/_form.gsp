<%@ page import="allison.zipcode.Country" %>



<div class="fieldcontain ${hasErrors(bean: countryInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="country.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${countryInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: countryInstance, field: 'countryCode', 'error')} ">
	<label for="countryCode">
		<g:message code="country.countryCode.label" default="Country Code" />
		
	</label>
	<g:textField name="countryCode" maxlength="2" value="${countryInstance?.countryCode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: countryInstance, field: 'states', 'error')} ">
	<label for="states">
		<g:message code="country.states.label" default="States" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${countryInstance?.states?}" var="s">
    <li><g:link controller="state" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="state" action="create" params="['country.id': countryInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'state.label', default: 'State')])}</g:link>
</li>
</ul>

</div>

