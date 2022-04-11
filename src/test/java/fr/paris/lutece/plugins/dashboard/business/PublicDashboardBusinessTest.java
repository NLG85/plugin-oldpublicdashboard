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
package fr.paris.lutece.plugins.dashboard.business;

import fr.paris.lutece.test.LuteceTestCase;

import java.util.Optional;

/**
 * This is the business class test for the object PublicDashboard
 */
public class PublicDashboardBusinessTest extends LuteceTestCase
{
    private static final String TYPEDASHBOARD1 = "Typedashboard1";
    private static final String TYPEDASHBOARD2 = "Typedashboard2";
    private static final int ORDRE1 = 1;
    private static final int ORDRE2 = 2;
    private static final String EMPLACEMENT1 = "Emplacement1";
    private static final String EMPLACEMENT2 = "Emplacement2";

    /**
     * test PublicDashboard
     */
    public void testBusiness( )
    {
        // Initialize an object
        PublicDashboard publicDashboard = new PublicDashboard( );
        publicDashboard.setTypedashboard( TYPEDASHBOARD1 );
        publicDashboard.setPosition( ORDRE1 );
        publicDashboard.setLocation( EMPLACEMENT1 );

        // Create test
        PublicDashboardHome.create( publicDashboard );
        Optional<PublicDashboard> optPublicDashboardStored = PublicDashboardHome.findByPrimaryKey( publicDashboard.getId( ) );
        PublicDashboard publicDashboardStored = optPublicDashboardStored.orElse( new PublicDashboard( ) );
        assertEquals( publicDashboardStored.getTypedashboard( ), publicDashboard.getTypedashboard( ) );
        assertEquals( publicDashboardStored.getPosition( ), publicDashboard.getPosition( ) );
        assertEquals( publicDashboardStored.getLocation( ), publicDashboard.getLocation( ) );

        // Update test
        publicDashboard.setTypedashboard( TYPEDASHBOARD2 );
        publicDashboard.setPosition( ORDRE2 );
        publicDashboard.setLocation( EMPLACEMENT2 );
        PublicDashboardHome.update( publicDashboard );
        optPublicDashboardStored = PublicDashboardHome.findByPrimaryKey( publicDashboard.getId( ) );
        publicDashboardStored = optPublicDashboardStored.orElse( new PublicDashboard( ) );

        assertEquals( publicDashboardStored.getTypedashboard( ), publicDashboard.getTypedashboard( ) );
        assertEquals( publicDashboardStored.getPosition( ), publicDashboard.getPosition( ) );
        assertEquals( publicDashboardStored.getLocation( ), publicDashboard.getLocation( ) );

        // List test
        PublicDashboardHome.getPublicDashboardsList( );

        // Delete test
        PublicDashboardHome.remove( publicDashboard.getId( ) );
        optPublicDashboardStored = PublicDashboardHome.findByPrimaryKey( publicDashboard.getId( ) );
        publicDashboardStored = optPublicDashboardStored.orElse( new PublicDashboard( ) );
        assertNull( publicDashboardStored );

    }

}
