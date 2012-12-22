<%@ page import="allison.zipcode.State" %>



<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="state.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${stateInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'abbreviation', 'error')} required">
	<label for="abbreviation">
		<g:message code="state.abbreviation.label" default="Abbreviation" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="abbreviation" maxlength="2" required="" value="${stateInstance?.abbreviation}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'countryCode', 'error')} required">
	<label for="countryCode">
		<g:message code="state.countryCode.label" default="Country Code" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="countryCode" maxlength="2" required="" value="${stateInstance?.countryCode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'country', 'error')} required">
	<label for="country">
		<g:message code="state.country.label" default="Country" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="country" name="country.id" from="${allison.zipcode.Country.list()}" optionKey="id" required="" value="${stateInstance?.country?.id}" class="many-to-one"/>
</div>


