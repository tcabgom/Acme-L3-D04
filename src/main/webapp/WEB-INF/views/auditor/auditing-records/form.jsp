<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="auditor.records.form.label.subject" path="subject"/>
	<acme:hidden-data path="audit"/>
	<acme:input-select code="auditor.records.form.label.mark" path="mark" choices="${marks}"/>
	<acme:input-textbox code="auditor.records.form.label.assesment" path="assesment"/>
	<acme:input-moment code="auditor.records.form.label.auditingPeriodInitial" path="auditingPeriodInitial"/>
	<acme:input-moment code="auditor.records.form.label.auditingPeriodEnd" path="auditingPeriodEnd"/>
	<acme:input-url code="auditor.records.form.label.furtherInformation" path="furtherInformation"/>
	<acme:input-checkbox code="auditor.records.form.label.draftMode" path="draftMode" readonly="true"/>
	<acme:input-checkbox code="auditor.records.form.label.confirmation" path="confirmation"/>
	<p><acme:print value="La confirmación solo es necesaria en la actualización y publicación"></acme:print></p>
	<jstl:choose>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="auditor.records.form.button.create" action="/auditor/auditing-records/create?auditId=${auditId }"/>
        </jstl:when>
        <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">
        <jstl:if test="${published}">
        	<acme:submit code="auditor.records.form.button.update" action="/auditor/auditing-records/update?auditId=${auditId}"/>
            <acme:submit code="auditor.records.form.button.delete" action="/auditor/auditing-records/delete?auditId=${auditId}"/>
            <acme:submit code="auditor.records.form.button.publish" action="/auditor/auditing-records/publish?auditId=${auditId}"/>
        </jstl:if>    
        </jstl:when>
    </jstl:choose>
</acme:form>