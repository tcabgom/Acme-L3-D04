<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="user-account.tutorial.list.label.title"  path="title"        width="45%"/>
	<acme:list-column code="user-account.tutorial.list.label.course" path="course.title" width="45%"/>
	<acme:list-column code="user-account.tutorial.list.label.code"   path="title"        width="10%"/>
</acme:list>