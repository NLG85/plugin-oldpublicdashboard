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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

public class PublicDashboard
{

    private String _strPupClass;
    private String _template;

    private static final String MARK_PROFILE = "profile";

    private static final String PARAMETER_ID_USER = "id_user";

    public PublicDashboard( String strPupClass, String template )
    {
        _strPupClass = strPupClass;
        _template = template;
    }

    public String getPublicUserProfileClass( )
    {
        return _strPupClass;
    }

    public String getTemplate( )
    {
        return _template;
    }

    public String getDashboardData( HttpServletRequest request ) throws NoSuchMethodException, SecurityException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {

        String userID = request.getParameter( PARAMETER_ID_USER );

        PublicUserProfile.UserProfileBuilder builder = (PublicUserProfile.UserProfileBuilder) Class.forName( _strPupClass ).getConstructor( new Class [ ] {
                String.class
        } ).newInstance( userID );

        IPublicUserProfile pup = builder.withUserInfos( ).withActions( userID ).withCounter( userID ).build( );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_PROFILE, pup );
        HtmlTemplate template = AppTemplateService.getTemplate( _template, I18nService.getDefaultLocale( ), model );

        return template.getHtml( );

    }

}
