
<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="lecturer.course.list.label.code" path="code"  width="30%" />
	<acme:list-column code="lecturer.course.list.label.title" path="title" width="40%" />
	<acme:list-column code="lecturer.course.list.label.retailPrice" path="retailPrice" width="20%" />
	<acme:list-column code="lecturer.course.list.label.draftMode" path="draftMode" width="10%" />
</acme:list>

<acme:button code="lecturer.course.list.button.create" action="/lecturer/course/create"/>