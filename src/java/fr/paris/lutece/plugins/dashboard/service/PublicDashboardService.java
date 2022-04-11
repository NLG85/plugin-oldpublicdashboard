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
package fr.paris.lutece.plugins.dashboard.service;

import java.lang.reflect.InvocationTargetException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.dashboard.business.PublicDashboardHome;
import fr.paris.lutece.portal.service.security.RsaService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

public class PublicDashboardService
{

    private static Map<String, String> _mapDashboardTypes = new HashMap<>( );
    private static PublicDashboardService _instance;
    public static final String PROPERTY_ENCRYPT = "dashboard.encrypt.userid";
    public static final String URL_PUBLICDASHBOARD = "?page=publicdashboard&view=getDashboards&id_user=";

    public ReferenceList getPublicDashboardBuilders( )
    {

        ReferenceList lstDashboardBuilder = new ReferenceList( );
        Properties allProps = AppPropertiesService.getProperties( );
        Enumeration<?> enumKeys = allProps.propertyNames( );

        while ( enumKeys.hasMoreElements( ) )
        {
            String name = (String) enumKeys.nextElement( );
            if ( name.contains( "class.name" ) && name.contains( "dashboard" ) )
            {
                lstDashboardBuilder.addItem( AppPropertiesService.getProperty( name ), AppPropertiesService.getProperty( name ) );
            }
        }
        return lstDashboardBuilder;
    }

    public ReferenceList getListDashboardLocation( )
    {
        ReferenceList lstDashboardLocation = new ReferenceList( );
        Properties allProps = AppPropertiesService.getProperties( );
        Enumeration<?> enumKeys = allProps.propertyNames( );

        while ( enumKeys.hasMoreElements( ) )
        {
            String name = (String) enumKeys.nextElement( );
            if ( name.matches( "dashboard[.].*[.]template" ) )
            {
                lstDashboardLocation.addItem( AppPropertiesService.getProperty( name ), AppPropertiesService.getProperty( name ) );
            }
        }

        return lstDashboardLocation;
    }

    public List<String> getContent( HttpServletRequest request ) throws NoSuchMethodException, SecurityException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {

        List<String> html = new ArrayList<String>( );
        List<fr.paris.lutece.plugins.dashboard.business.PublicDashboard> lstpublicdashboard = PublicDashboardHome.getPublicDashboardsListByOrder( );
        for ( fr.paris.lutece.plugins.dashboard.business.PublicDashboard publicdashboard : lstpublicdashboard )
        {
            PublicDashboard dashboard = PublicDashboardService.getInstance( ).create( publicdashboard.getTypedashboard( ), publicdashboard.getLocation( ) );
            html.add( dashboard.getDashboardData( request ) );
        }
        return html;
    }

    private PublicDashboardService( )
    {

        Properties allProps = AppPropertiesService.getProperties( );
        Enumeration<?> enumKeys = allProps.propertyNames( );

        while ( enumKeys.hasMoreElements( ) )
        {
            String name = (String) enumKeys.nextElement( );
            if ( name.matches( "dashboard[.].*class[.]name" ) )
            {
                String [ ] strClassName = name.split( ".name" );
                StringBuilder strClassLocation = new StringBuilder( strClassName [0] );
                strClassLocation.append( ".location" );
                _mapDashboardTypes.put( AppPropertiesService.getProperty( name ), AppPropertiesService.getProperty( strClassLocation.toString( ) ) );
            }
        }

    }

    public static PublicDashboardService getInstance( )
    {
        if ( _instance == null )
            _instance = new PublicDashboardService( );
        return _instance;
    }

    public PublicDashboard create( String type, String template )
    {
        return new PublicDashboard( _mapDashboardTypes.get( type ), template );
    }

    public String getUrl( String userid ) throws GeneralSecurityException
    {
        StringBuilder url = new StringBuilder( AppPathService.getPortalUrl( ) );
        url.append( URL_PUBLICDASHBOARD );
        if ( AppPropertiesService.getPropertyBoolean( PROPERTY_ENCRYPT, false ) )
        {
            url.append( RsaService.encryptRsa( userid ) );
        }
        else
        {
            url.append( userid );
        }
        return url.toString( );
    }

}
