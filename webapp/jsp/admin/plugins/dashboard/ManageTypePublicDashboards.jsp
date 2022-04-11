<jsp:useBean id="publicdashboardTypePublicDashboard" scope="session" class="fr.paris.lutece.plugins.dashboard.web.TypePublicDashboardJspBean" />
<% String strContent = publicdashboardTypePublicDashboard.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
