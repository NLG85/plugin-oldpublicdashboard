<jsp:useBean id="publicdashboardPublicDashboard" scope="session" class="fr.paris.lutece.plugins.dashboard.web.PublicDashboardJspBean" />
<% String strContent = publicdashboardPublicDashboard.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
