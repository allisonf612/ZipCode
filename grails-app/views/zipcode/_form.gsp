<%@ page import="allison.zipcode.Zipcode" %>



<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'postalCode', 'error')} ">
	<label for="postalCode">
		<g:message code="zipcode.postalCode.label" default="Postal Code" />
		
	</label>
	<g:textField name="postalCode" maxlength="5" value="${zipcodeInstance?.postalCode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="zipcode.name.label" default="Name" />
		
	</label>
	<g:textField name="name" maxlength="100" value="${zipcodeInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'countryCode', 'error')} ">
	<label for="countryCode">
		<g:message code="zipcode.countryCode.label" default="Country Code" />
		
	</label>
	<g:textField name="countryCode" maxlength="20" value="${zipcodeInstance?.countryCode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'lat', 'error')} required">
	<label for="lat">
		<g:message code="zipcode.lat.label" default="Lat" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="lat" value="${fieldValue(bean: zipcodeInstance, field: 'lat')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'lng', 'error')} required">
	<label for="lng">
		<g:message code="zipcode.lng.label" default="Lng" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="lng" value="${fieldValue(bean: zipcodeInstance, field: 'lng')}" required=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'adminCode1', 'error')} ">
	<label for="adminCode1">
		<g:message code="zipcode.adminCode1.label" default="Admin Code1" />
		
	</label>
	<g:textField name="adminCode1" value="${zipcodeInstance?.adminCode1}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'adminName1', 'error')} ">
	<label for="adminName1">
		<g:message code="zipcode.adminName1.label" default="Admin Name1" />
		
	</label>
	<g:textField name="adminName1" value="${zipcodeInstance?.adminName1}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'adminCode2', 'error')} ">
	<label for="adminCode2">
		<g:message code="zipcode.adminCode2.label" default="Admin Code2" />
		
	</label>
	<g:textField name="adminCode2" maxlength="3" value="${zipcodeInstance?.adminCode2}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'adminName2', 'error')} ">
	<label for="adminName2">
		<g:message code="zipcode.adminName2.label" default="Admin Name2" />
		
	</label>
	<g:textField name="adminName2" maxlength="20" value="${zipcodeInstance?.adminName2}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'adminCode3', 'error')} ">
	<label for="adminCode3">
		<g:message code="zipcode.adminCode3.label" default="Admin Code3" />
		
	</label>
	<g:textField name="adminCode3" value="${zipcodeInstance?.adminCode3}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'adminName3', 'error')} ">
	<label for="adminName3">
		<g:message code="zipcode.adminName3.label" default="Admin Name3" />
		
	</label>
	<g:textField name="adminName3" value="${zipcodeInstance?.adminName3}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: zipcodeInstance, field: 'state', 'error')} required">
	<label for="state">
		<g:message code="zipcode.state.label" default="State" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="state" name="state.id" from="${allison.zipcode.State.list()}" optionKey="id" required="" value="${zipcodeInstance?.state?.id}" class="many-to-one"/>
</div>

