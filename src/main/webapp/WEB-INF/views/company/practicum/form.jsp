<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="company.practicum.form.label.code" path="code"/>
	<acme:hidden-data path="company"/>
	<acme:input-textbox code="company.practicum.form.label.title" path="title"/>
	<acme:input-textarea code="company.practicum.form.label.abstractPracticum" path="abstractPracticum"/>
	<acme:input-textarea code="company.practicum.form.label.goals" path="goals"/>
	<acme:input-select code="company.practicum.form.label.course" path="course" choices="${courses}"/>
	<acme:input-double code="company.practicum.form.label.estimatedTime" path="estimatedTime" readonly="true"/>
	<acme:input-checkbox code="company.practicum.form.label.draftMode" path="draftMode" readonly="true"/>
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete')}">
    	<acme:button code="company.practicum.form.button.sessions" action="/company/practicum-session/list?practicumId=${id}"/>
	</jstl:if>
	<jstl:choose>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="company.practicum.form.button.create" action="/company/practicum/create"/>
        </jstl:when>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode==true}">
            <acme:submit code="company.practicum.form.button.update" action="/company/practicum/update"/>
            <acme:submit code="company.practicum.form.button.delete" action="/company/practicum/delete"/>
            <jstl:choose>
           			<jstl:when test="${anySessions}"> 
           				<acme:submit code="company.practicum.form.button.publish"  action="/company/practicum/publish"/> 
           			</jstl:when>
            		<jstl:otherwise> <p><acme:message code="company.practicum.form.label.anySession"/></p> </jstl:otherwise>
            	</jstl:choose>
        </jstl:when>
    </jstl:choose>
</acme:form>