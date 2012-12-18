<%@ page import="allison.zipcode.State" %>



<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'totalResultsCount', 'error')} required">
	<label for="totalResultsCount">
		<g:message code="state.totalResultsCount.label" default="Total Results Count" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="totalResultsCount" type="number" value="${stateInstance.totalResultsCount}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'abbreviation', 'error')} ">
	<label for="abbreviation">
		<g:message code="state.abbreviation.label" default="Abbreviation" />
		
	</label>
	<g:textField name="abbreviation" value="${stateInstance?.abbreviation}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'fullName', 'error')} ">
	<label for="fullName">
		<g:message code="state.fullName.label" default="Full Name" />
		
	</label>
	<g:textField name="fullName" value="${stateInstance?.fullName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'country', 'error')} required">
	<label for="country">
		<g:message code="state.country.label" default="Country" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="country" name="country.id" from="${allison.zipcode.Country.list()}" optionKey="id" required="" value="${stateInstance?.country?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'zipcodes', 'error')} ">
	<label for="zipcodes">
		<g:message code="state.zipcodes.label" default="Zipcodes" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${stateInstance?.zipcodes?}" var="z">
    <li><g:link controller="zipcode" action="show" id="${z.id}">${z?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="zipcode" action="create" params="['state.id': stateInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'zipcode.label', default: 'Zipcode')])}</g:link>
</li>
</ul>

</div>

