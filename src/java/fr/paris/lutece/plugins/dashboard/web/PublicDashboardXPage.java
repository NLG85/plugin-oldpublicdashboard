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

import fr.paris.lutece.plugins.dashboard.business.PublicDashboard;
import fr.paris.lutece.plugins.dashboard.business.PublicDashboardHome;
import fr.paris.lutece.plugins.dashboard.service.PublicDashboardService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage PublicDashboard xpages ( manage, create, modify, remove )
 */
@Controller( xpageName = "publicdashboard", pageTitleI18nKey = "dashboard.xpage.publicdashboard.pageTitle", pagePathI18nKey = "dashboard.xpage.publicdashboard.pagePathLabel" )
public class PublicDashboardXPage extends MVCApplication
{
    // Templates
    private static final String TEMPLATE_MANAGE_PUBLICDASHBOARDS = "/skin/plugins/dashboard/manage_publicdashboards.html";
    private static final String TEMPLATE_CREATE_PUBLICDASHBOARD = "/skin/plugins/dashboard/create_publicdashboard.html";
    private static final String TEMPLATE_MODIFY_PUBLICDASHBOARD = "/skin/plugins/dashboard/modify_publicdashboard.html";

    // Parameters
    private static final String PARAMETER_ID_PUBLICDASHBOARD = "id";
    private static final String PARAMETER_ID_USER = "id_user";

    // Markers
    private static final String MARK_PUBLICDASHBOARD_LIST = "publicdashboard_list";
    private static final String MARK_PUBLICDASHBOARD = "publicdashboard";

    private static final String MARK_LIST_DASHBOARDS_CONTENT = "listDashboardsContent";

    // Message
    private static final String MESSAGE_CONFIRM_REMOVE_PUBLICDASHBOARD = "dashboard.message.confirmRemovePublicDashboard";

    // Views
    private static final String VIEW_MANAGE_PUBLICDASHBOARDS = "managePublicDashboards";
    private static final String VIEW_CREATE_PUBLICDASHBOARD = "createPublicDashboard";
    private static final String VIEW_MODIFY_PUBLICDASHBOARD = "modifyPublicDashboard";

    private static final String VIEW_GET_DASHBOARDS = "getDashboards";

    // Actions
    private static final String ACTION_CREATE_PUBLICDASHBOARD = "createPublicDashboard";
    private static final String ACTION_MODIFY_PUBLICDASHBOARD = "modifyPublicDashboard";
    private static final String ACTION_REMOVE_PUBLICDASHBOARD = "removePublicDashboard";
    private static final String ACTION_CONFIRM_REMOVE_PUBLICDASHBOARD = "confirmRemovePublicDashboard";

    // Infos
    private static final String INFO_PUBLICDASHBOARD_CREATED = "dashboard.info.publicdashboard.created";
    private static final String INFO_PUBLICDASHBOARD_UPDATED = "dashboard.info.publicdashboard.updated";
    private static final String INFO_PUBLICDASHBOARD_REMOVED = "dashboard.info.publicdashboard.removed";

    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";

    private static final String TEMPLATE_GET_DASHBOARDS = "/skin/plugins/dashboard/get_dashboards.html";

    // Session variable to store working values
    private PublicDashboard _publicdashboard;

    /**
     * Get the list of dash boards of the current user
     * 
     * @param request
     *            The request, with the user logged in
     * @return The XPage to display the list of dash boards of the user
     * @throws UserNotSignedException
     *             If the user is not logged in
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @View( value = VIEW_GET_DASHBOARDS )
    public XPage getDashboards( HttpServletRequest request ) throws UserNotSignedException, NoSuchMethodException, SecurityException, ClassNotFoundException,
            InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Map<String, Object> model = new HashMap<String, Object>( );
        PublicDashboardService dashboardService = PublicDashboardService.getInstance( );

        // model.put( MARK_LIST_DASHBOARDS_CONTENT, dashboardService.getContent(request.getParameter(PARAMETER_ID_USER)) );
        model.put( MARK_LIST_DASHBOARDS_CONTENT, dashboardService.getContent( request ) );

        return getXPage( TEMPLATE_GET_DASHBOARDS, request.getLocale( ), model );
    }

    /**
     * return the form to manage publicdashboards
     * 
     * @param request
     *            The Http request
     * @return the html code of the list of publicdashboards
     */
    @View( value = VIEW_MANAGE_PUBLICDASHBOARDS, defaultView = true )
    public XPage getManagePublicDashboards( HttpServletRequest request )
    {
        _publicdashboard = null;
        Map<String, Object> model = getModel( );
        model.put( MARK_PUBLICDASHBOARD_LIST, PublicDashboardHome.getPublicDashboardsList( ) );

        return getXPage( TEMPLATE_MANAGE_PUBLICDASHBOARDS, getLocale( request ), model );
    }

    /**
     * Returns the form to create a publicdashboard
     *
     * @param request
     *            The Http request
     * @return the html code of the publicdashboard form
     */
    @View( VIEW_CREATE_PUBLICDASHBOARD )
    public XPage getCreatePublicDashboard( HttpServletRequest request )
    {
        _publicdashboard = ( _publicdashboard != null ) ? _publicdashboard : new PublicDashboard( );

        Map<String, Object> model = getModel( );
        model.put( MARK_PUBLICDASHBOARD, _publicdashboard );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_PUBLICDASHBOARD ) );

        return getXPage( TEMPLATE_CREATE_PUBLICDASHBOARD, getLocale( request ), model );
    }

    /**
     * Process the data capture form of a new publicdashboard
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_CREATE_PUBLICDASHBOARD )
    public XPage doCreatePublicDashboard( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _publicdashboard, request, getLocale( request ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_PUBLICDASHBOARD ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _publicdashboard ) )
        {
            return redirectView( request, VIEW_CREATE_PUBLICDASHBOARD );
        }

        PublicDashboardHome.create( _publicdashboard );
        addInfo( INFO_PUBLICDASHBOARD_CREATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_PUBLICDASHBOARDS );
    }

    /**
     * Manages the removal form of a publicdashboard whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException
     *             {@link fr.paris.lutece.portal.service.message.SiteMessageException}
     */
    @Action( ACTION_CONFIRM_REMOVE_PUBLICDASHBOARD )
    public XPage getConfirmRemovePublicDashboard( HttpServletRequest request ) throws SiteMessageException
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PUBLICDASHBOARD ) );

        UrlItem url = new UrlItem( getActionFullUrl( ACTION_REMOVE_PUBLICDASHBOARD ) );
        url.addParameter( PARAMETER_ID_PUBLICDASHBOARD, nId );

        SiteMessageService.setMessage( request, MESSAGE_CONFIRM_REMOVE_PUBLICDASHBOARD, SiteMessage.TYPE_CONFIRMATION, url.getUrl( ) );
        return null;
    }

    /**
     * Handles the removal form of a publicdashboard
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage publicdashboards
     */
    @Action( ACTION_REMOVE_PUBLICDASHBOARD )
    public XPage doRemovePublicDashboard( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PUBLICDASHBOARD ) );
        PublicDashboardHome.remove( nId );
        addInfo( INFO_PUBLICDASHBOARD_REMOVED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_PUBLICDASHBOARDS );
    }

    /**
     * Returns the form to update info about a publicdashboard
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_PUBLICDASHBOARD )
    public XPage getModifyPublicDashboard( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PUBLICDASHBOARD ) );

        if ( _publicdashboard == null || ( _publicdashboard.getId( ) != nId ) )
        {
            Optional<PublicDashboard> optPublicDashboard = PublicDashboardHome.findByPrimaryKey( nId );
            _publicdashboard = optPublicDashboard.orElseThrow( ( ) -> new AppException( ERROR_RESOURCE_NOT_FOUND ) );
            ;
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_PUBLICDASHBOARD, _publicdashboard );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_PUBLICDASHBOARD ) );

        return getXPage( TEMPLATE_MODIFY_PUBLICDASHBOARD, getLocale( request ), model );
    }

    /**
     * Process the change form of a publicdashboard
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     * @throws AccessDeniedException
     */
    @Action( ACTION_MODIFY_PUBLICDASHBOARD )
    public XPage doModifyPublicDashboard( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _publicdashboard, request, getLocale( request ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_PUBLICDASHBOARD ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _publicdashboard ) )
        {
            return redirect( request, VIEW_MODIFY_PUBLICDASHBOARD, PARAMETER_ID_PUBLICDASHBOARD, _publicdashboard.getId( ) );
        }

        PublicDashboardHome.update( _publicdashboard );
        addInfo( INFO_PUBLICDASHBOARD_UPDATED, getLocale( request ) );

        return redirectView( request, VIEW_MANAGE_PUBLICDASHBOARDS );
    }
}
