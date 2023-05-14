
<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="company.PracticumSession.list.label.title" path="title" width="40%"/>
	<acme:list-column code="company.PracticumSession.list.label.abstractSession" path="abstractSession" width="50%"/>
	<acme:list-column code="company.PracticumSession.list.label.extraSession" path="extraSession"/>
</acme:list>

<acme:button code="company.practicumSession.form.button.create" action="/company/practicum-session/create?practicumId=${practicumId}"/>

