<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:hidden-data path="practicum"/>
	<acme:input-textbox code="company.practicumSession.form.label.title" path="title"/>	
	<acme:input-textarea code="company.practicumSession.form.label.abstractSession" path="abstractSession"/>	
	<acme:input-moment code="company.practicumSession.form.label.start" path="start"/>	
	<acme:input-moment code="company.practicumSession.form.label.finish" path="finish"/>	
	<acme:input-url code="company.practicumSession.form.label.link" path="link"/>	
	<acme:input-checkbox code="company.practicumSession.form.label.extraSession" path="extraSession" readonly="true"/>
	
	<jstl:choose>
        <jstl:when test="${_command == 'create'}">
        	<jstl:if test="${extraSession}">
        			<acme:input-checkbox code="company.practicumSession.form.error.confirmation" path="confirmation"/>
        	</jstl:if>
            <acme:submit code="company.practicumSession.form.button.create" action="/company/practicum-session/create?practicumId=${practicumId}"/>
        </jstl:when>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode==true}">
            <acme:submit code="company.practicumSession.form.button.update" action="/company/practicum-session/update?practicumId=${practicumId}"/>
            <acme:submit code="company.practicumSession.form.button.delete" action="/company/practicum-session/delete?practicumId=${practicumId}"/>
        </jstl:when>
    </jstl:choose>
</acme:form>