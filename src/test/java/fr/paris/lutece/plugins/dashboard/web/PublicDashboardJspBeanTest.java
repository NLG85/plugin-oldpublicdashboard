/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.dashboard.web;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.admin.AdminAuthenticationService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import java.util.List;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.web.LocalVariables;
import java.io.IOException;
import fr.paris.lutece.plugins.dashboard.business.PublicDashboard;
import fr.paris.lutece.plugins.dashboard.business.PublicDashboardHome;

/**
 * This is the business class test for the object PublicDashboard
 */
public class PublicDashboardJspBeanTest extends LuteceTestCase
{
    private static final String TYPEDASHBOARD1 = "Typedashboard1";
    private static final String TYPEDASHBOARD2 = "Typedashboard2";
    private static final int ORDRE1 = 1;
    private static final int ORDRE2 = 2;
    private static final String EMPLACEMENT1 = "Emplacement1";
    private static final String EMPLACEMENT2 = "Emplacement2";

    public void testJspBeans( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        MockServletConfig config = new MockServletConfig( );

        // display admin PublicDashboard management JSP
        PublicDashboardJspBean jspbean = new PublicDashboardJspBean( );
        String html = jspbean.getManagePublicDashboards( request );
        assertNotNull( html );

        // display admin PublicDashboard creation JSP
        html = jspbean.getCreatePublicDashboard( request );
        assertNotNull( html );

        // action create PublicDashboard
        request = new MockHttpServletRequest( );

        request.addParameter( "typedashboard", TYPEDASHBOARD1 );
        request.addParameter( "ordre", String.valueOf( ORDRE1 ) );
        request.addParameter( "emplacement", EMPLACEMENT1 );
        request.addParameter( "action", "createPublicDashboard" );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createPublicDashboard" ) );
        request.setMethod( "POST" );
        response = new MockHttpServletResponse( );
        AdminUser adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, adminUser );
            html = jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }
        catch( UserNotSignedException e )
        {
            fail( "user not signed in" );
        }

        // display modify PublicDashboard JSP
        request = new MockHttpServletRequest( );
        request.addParameter( "typedashboard", TYPEDASHBOARD1 );
        request.addParameter( "ordre", String.valueOf( ORDRE1 ) );
        request.addParameter( "emplacement", EMPLACEMENT1 );
        List<Integer> listIds = PublicDashboardHome.getIdPublicDashboardsList( );
        assertTrue( !listIds.isEmpty( ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        jspbean = new PublicDashboardJspBean( );

        assertNotNull( jspbean.getModifyPublicDashboard( request ) );

        // action modify PublicDashboard
        request = new MockHttpServletRequest( );
        response = new MockHttpServletResponse( );
        request.addParameter( "typedashboard", TYPEDASHBOARD2 );
        request.addParameter( "ordre", String.valueOf( ORDRE2 ) );
        request.addParameter( "emplacement", EMPLACEMENT2 );
        request.setRequestURI( "jsp/admin/plugins/example/ManagePublicDashboards.jsp" );
        // important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createPublicDashboard, qui est l'action par défaut
        request.addParameter( "action", "modifyPublicDashboard" );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyPublicDashboard" ) );
        adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, adminUser );
            html = jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }
        catch( UserNotSignedException e )
        {
            fail( "user not signed in" );
        }

        // get remove PublicDashboard
        request = new MockHttpServletRequest( );
        // request.setRequestURI("jsp/admin/plugins/example/ManagePublicDashboards.jsp");
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        jspbean = new PublicDashboardJspBean( );
        request.addParameter( "action", "confirmRemovePublicDashboard" );
        assertNotNull( jspbean.getModifyPublicDashboard( request ) );

        // do remove PublicDashboard
        request = new MockHttpServletRequest( );
        response = new MockHttpServletResponse( );
        request.setRequestURI( "jsp/admin/plugins/example/ManagePublicDashboardts.jsp" );
        // important pour que MVCController sache quelle action effectuer, sinon, il redirigera vers createPublicDashboard, qui est l'action par défaut
        request.addParameter( "action", "removePublicDashboard" );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removePublicDashboard" ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        request.setMethod( "POST" );
        adminUser = new AdminUser( );
        adminUser.setAccessCode( "admin" );

        try
        {
            AdminAuthenticationService.getInstance( ).registerUser( request, adminUser );
            html = jspbean.processController( request, response );

            // MockResponse object does not redirect, result is always null
            assertNull( html );
        }
        catch( AccessDeniedException e )
        {
            fail( "access denied" );
        }
        catch( UserNotSignedException e )
        {
            fail( "user not signed in" );
        }

    }
}
