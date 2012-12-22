<%@ page import="allison.zipcode.State" %>



<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="state.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${stateInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'abbreviation', 'error')} ">
	<label for="abbreviation">
		<g:message code="state.abbreviation.label" default="Abbreviation" />
		
	</label>
	<g:textField name="abbreviation" maxlength="2" value="${stateInstance?.abbreviation}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'countryCode', 'error')} ">
	<label for="countryCode">
		<g:message code="state.countryCode.label" default="Country Code" />
		
	</label>
	<g:textField name="countryCode" maxlength="2" value="${stateInstance?.countryCode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'country', 'error')} required">
	<label for="country">
		<g:message code="state.country.label" default="Country" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="country" name="country.id" from="${allison.zipcode.Country.list()}" optionKey="id" required="" value="${stateInstance?.country?.id}" class="many-to-one"/>
</div>

%{--<div class="fieldcontain ${hasErrors(bean: stateInstance, field: 'zipcodes', 'error')} ">--}%
	%{--<label for="zipcodes">--}%
		%{--<g:message code="state.zipcodes.label" default="Zipcodes" />--}%
		%{----}%
	%{--</label>--}%
	%{----}%
%{--<ul class="one-to-many">--}%
%{--<g:each in="${stateInstance?.zipcodes?}" var="z">--}%
    %{--<li><g:link controller="zipcode" action="show" id="${z.id}">${z?.encodeAsHTML()}</g:link></li>--}%
%{--</g:each>--}%
%{--<li class="add">--}%
%{--<g:link controller="zipcode" action="create" params="['state.id': stateInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'zipcode.label', default: 'Zipcode')])}</g:link>--}%
%{--</li>--}%
%{--</ul>--}%

%{--</div>--}%

