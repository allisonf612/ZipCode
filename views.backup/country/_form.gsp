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

</div>

