<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form readonly="${readonly}">
    <acme:input-textbox code="administrator.configuration.form.label.currency" path="currency"/>
    <acme:input-textbox code="administrator.configuration.form.label.acceptedCurrencies" path="acceptedCurrencies"/>

    <acme:submit code="administrator.configuration.form.button.update" action="/administrator/configuration/update"/>
</acme:form>