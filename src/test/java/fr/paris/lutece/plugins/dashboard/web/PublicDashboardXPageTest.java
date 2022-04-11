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

import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.test.LuteceTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import java.util.List;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.plugins.dashboard.business.PublicDashboard;
import fr.paris.lutece.plugins.dashboard.business.PublicDashboardHome;

/**
 * This is the business class test for the object PublicDashboard
 */
public class PublicDashboardXPageTest extends LuteceTestCase
{
    private static final String TYPEDASHBOARD1 = "Typedashboard1";
    private static final String TYPEDASHBOARD2 = "Typedashboard2";
    private static final int ORDRE1 = 1;
    private static final int ORDRE2 = 2;
    private static final String EMPLACEMENT1 = "Emplacement1";
    private static final String EMPLACEMENT2 = "Emplacement2";

    public void testXPage( ) throws AccessDeniedException
    {
        // Xpage create test
        MockHttpServletRequest request = new MockHttpServletRequest( );
        MockHttpServletResponse response = new MockHttpServletResponse( );
        MockServletConfig config = new MockServletConfig( );

        PublicDashboardXPage xpage = new PublicDashboardXPage( );
        assertNotNull( xpage.getCreatePublicDashboard( request ) );

        request = new MockHttpServletRequest( );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "createPublicDashboard" ) );

        LocalVariables.setLocal( config, request, response );

        request.addParameter( "action", "createPublicDashboard" );
        request.addParameter( "typedashboard", TYPEDASHBOARD1 );
        request.addParameter( "ordre", String.valueOf( ORDRE1 ) );
        request.addParameter( "emplacement", EMPLACEMENT1 );
        request.setMethod( "POST" );

        assertNotNull( xpage.doCreatePublicDashboard( request ) );

        // modify PublicDashboard
        List<Integer> listIds = PublicDashboardHome.getIdPublicDashboardsList( );

        assertTrue( !listIds.isEmpty( ) );

        request = new MockHttpServletRequest( );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );

        assertNotNull( xpage.getModifyPublicDashboard( request ) );

        response = new MockHttpServletResponse( );
        request = new MockHttpServletRequest( );
        LocalVariables.setLocal( config, request, response );
        request.addParameter( "typedashboard", TYPEDASHBOARD2 );
        request.addParameter( "ordre", String.valueOf( ORDRE2 ) );
        request.addParameter( "emplacement", EMPLACEMENT2 );

        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "modifyPublicDashboard" ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        assertNotNull( xpage.doModifyPublicDashboard( request ) );

        // do confirm remove PublicDashboard
        request = new MockHttpServletRequest( );
        request.addParameter( "action", "confirmRemovePublicDashboard" );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "confirmRemovePublicDashboard" ) );
        ;
        request.setMethod( "GET" );

        try
        {
            xpage.getConfirmRemovePublicDashboard( request );
        }
        catch( Exception e )
        {
            assertTrue( e instanceof SiteMessageException );
        }

        // do remove PublicDashboard
        response = new MockHttpServletResponse( );
        request = new MockHttpServletRequest( );
        LocalVariables.setLocal( config, request, response );
        request.addParameter( "action", "removePublicDashboard" );
        request.addParameter( "token", SecurityTokenService.getInstance( ).getToken( request, "removePublicDashboard" ) );
        request.addParameter( "id", String.valueOf( listIds.get( 0 ) ) );
        assertNotNull( xpage.doRemovePublicDashboard( request ) );

    }

}
