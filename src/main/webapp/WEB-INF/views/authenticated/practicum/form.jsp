<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form readonly="true">
	<acme:input-textbox code="authenticated.practicum.form.label.code" path="code"/>
	<acme:input-textbox code="authenticated.practicum.form.label.title" path="title"/>
	<acme:input-textarea code="authenticated.practicum.form.label.abstractPracticum" path="abstractPracticum"/>
	<acme:input-textarea code="authenticated.practicum.form.label.goals" path="goals"/>
	<acme:input-double code="authenticated.practicum.form.label.estimatedTotalTime" path="estimatedTotalTime"/>
	<acme:input-textbox code="authenticated.practicum.form.label.draftMode" path="draftMode"/>
</acme:form>