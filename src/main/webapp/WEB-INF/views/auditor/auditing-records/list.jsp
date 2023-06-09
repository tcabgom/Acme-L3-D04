<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="auditor.records.list.label.subject" path="subject" width="20%"/>
	<acme:list-column code="auditor.records.list.label.mark" path="mark" width="20%"/>
	<acme:list-column code="auditor.records.list.label.draftMode" path="draftMode" width="20%"/>
</acme:list>

<jstl:if test="${published}">
	<acme:button code="auditor.records.form.button.create" action="/auditor/auditing-records/create?auditId=${auditId}"/>
</jstl:if>
<p><acme:print value="Los registros actualizados aparecer�n con un * en el t�tulo a menos que se utilize la longitud m�xima"></acme:print></p>





