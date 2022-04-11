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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;

/**
 * This class provides instances management methods (create, find, ...) for PublicDashboard objects
 */
public final class PublicDashboardHome
{
    // Static variable pointed at the DAO instance
    private static IPublicDashboardDAO _dao = SpringContextService.getBean( "dashboard.publicDashboardDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "dashboard" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private PublicDashboardHome( )
    {
    }

    /**
     * Create an instance of the publicDashboard class
     * 
     * @param publicDashboard
     *            The instance of the PublicDashboard which contains the informations to store
     * @return The instance of publicDashboard which has been created with its primary key.
     */
    public static PublicDashboard create( PublicDashboard publicDashboard )
    {
        int nOrder = 1;
        List<PublicDashboard> listPublicDashboard = getPublicDashboardsListByOrder( );

        if ( !CollectionUtils.isEmpty( listPublicDashboard ) )
        {
            nOrder = listPublicDashboard.get( listPublicDashboard.size( ) - 1 ).getPosition( ) + 1;
        }

        publicDashboard.setPosition( nOrder );

        _dao.insert( publicDashboard, _plugin );

        return publicDashboard;
    }

    /**
     * Update of the publicDashboard which is specified in parameter
     * 
     * @param publicDashboard
     *            The instance of the PublicDashboard which contains the data to store
     * @return The instance of the publicDashboard which has been updated
     */
    public static PublicDashboard update( PublicDashboard publicDashboard )
    {
        _dao.store( publicDashboard, _plugin );

        return publicDashboard;
    }

    /**
     * Remove the publicDashboard whose identifier is specified in parameter
     * 
     * @param nKey
     *            The publicDashboard Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    /**
     * Returns an instance of a publicDashboard whose identifier is specified in parameter
     * 
     * @param nKey
     *            The publicDashboard primary key
     * @return an instance of PublicDashboard
     */
    public static Optional<PublicDashboard> findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the publicDashboard objects and returns them as a list
     * 
     * @return the list which contains the data of all the publicDashboard objects
     */
    public static List<PublicDashboard> getPublicDashboardsList( )
    {
        return _dao.selectPublicDashboardsList( _plugin );
    }

    /**
     * Load the id of all the publicDashboard objects and returns them as a list
     * 
     * @return the list which contains the id of all the publicDashboard objects
     */
    public static List<Integer> getIdPublicDashboardsList( )
    {
        return _dao.selectIdPublicDashboardsList( _plugin );
    }

    /**
     * Load the data of all the publicDashboard objects and returns them as a referenceList
     * 
     * @return the referenceList which contains the data of all the publicDashboard objects
     */
    public static ReferenceList getPublicDashboardsReferenceList( )
    {
        return _dao.selectPublicDashboardsReferenceList( _plugin );
    }

    /**
     * Load the data of all the publicDashboard objects and returns them as a list
     * 
     * @return the list which contains the data of all the publicDashboard objects
     */
    public static List<PublicDashboard> getPublicDashboardsListByOrder( )
    {
        return _dao.selectPublicDashboardsListByOrdre( _plugin );
    }
}
