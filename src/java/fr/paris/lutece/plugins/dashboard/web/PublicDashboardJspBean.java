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

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import fr.paris.lutece.plugins.dashboard.business.PublicDashboard;
import fr.paris.lutece.plugins.dashboard.business.PublicDashboardHome;
import fr.paris.lutece.plugins.dashboard.service.PublicDashboardService;

/**
 * This class provides the user interface to manage PublicDashboard features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManagePublicDashboards.jsp", controllerPath = "jsp/admin/plugins/dashboard/", right = "DASHBOARD_MANAGEMENT" )
public class PublicDashboardJspBean extends AbstractPublicDashboardJspBean
{
    // Templates
    private static final String TEMPLATE_MANAGE_PUBLICDASHBOARDS = "/admin/plugins/dashboard/manage_publicdashboards.html";
    private static final String TEMPLATE_CREATE_PUBLICDASHBOARD = "/admin/plugins/dashboard/create_publicdashboard.html";
    private static final String TEMPLATE_MODIFY_PUBLICDASHBOARD = "/admin/plugins/dashboard/modify_publicdashboard.html";

    // Parameters
    private static final String PARAMETER_ID_PUBLICDASHBOARD = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_PUBLICDASHBOARDS = "dashboard.manage_publicdashboards.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PUBLICDASHBOARD = "dashboard.modify_publicdashboard.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_PUBLICDASHBOARD = "dashboard.create_publicdashboard.pageTitle";

    // Markers
    private static final String MARK_PUBLICDASHBOARD_LIST = "publicdashboard_list";
    private static final String MARK_PUBLICDASHBOARD = "publicdashboard";
    private static final String MARK_LIST_TYPE_DASHBOARD = "listtypedashboard";
    private static final String MARK_LIST_LOCATION_DASHBOARD = "listlocationdashboard";

    private static final String JSP_MANAGE_PUBLICDASHBOARDS = "jsp/admin/plugins/dashboard/ManagePublicDashboards.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_PUBLICDASHBOARD = "dashboard.message.confirmRemovePublicDashboard";

    // Validations
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "dashboard.model.entity.publicdashboard.attribute.";

    // Views
    private static final String VIEW_MANAGE_PUBLICDASHBOARDS = "managePublicDashboards";
    private static final String VIEW_CREATE_PUBLICDASHBOARD = "createPublicDashboard";
    private static final String VIEW_MODIFY_PUBLICDASHBOARD = "modifyPublicDashboard";

    // Actions
    private static final String ACTION_CREATE_PUBLICDASHBOARD = "createPublicDashboard";
    private static final String ACTION_MODIFY_PUBLICDASHBOARD = "modifyPublicDashboard";
    private static final String ACTION_REMOVE_PUBLICDASHBOARD = "removePublicDashboard";
    private static final String ACTION_CONFIRM_REMOVE_PUBLICDASHBOARD = "confirmRemovePublicDashboard";
    private static final String ACTION_MOVE_UP_DASHBOARD = "moveUpDashboard";
    private static final String ACTION_MOVE_DOWN_DASHBOARD = "moveDownDashboard";

    // Infos
    private static final String INFO_PUBLICDASHBOARD_CREATED = "dashboard.info.publicdashboard.created";
    private static final String INFO_PUBLICDASHBOARD_UPDATED = "dashboard.info.publicdashboard.updated";
    private static final String INFO_PUBLICDASHBOARD_REMOVED = "dashboard.info.publicdashboard.removed";

    private static final String PARAMETER_ID = "id";

    // Errors
    private static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";

    // Session variable to store working values
    private PublicDashboard _publicdashboard;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_PUBLICDASHBOARDS, defaultView = true )
    public String getManagePublicDashboards( HttpServletRequest request )
    {
        _publicdashboard = null;

        PublicDashboardService dashboardService = PublicDashboardService.getInstance( );
        ReferenceList lstTypeDashboard = dashboardService.getPublicDashboardBuilders( );

        List<PublicDashboard> listPublicDashboards = PublicDashboardHome.getPublicDashboardsListByOrder( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_PUBLICDASHBOARD_LIST, listPublicDashboards, JSP_MANAGE_PUBLICDASHBOARDS );
        model.put( MARK_LIST_TYPE_DASHBOARD, lstTypeDashboard );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_PUBLICDASHBOARDS, TEMPLATE_MANAGE_PUBLICDASHBOARDS, model );
    }

    /**
     * Move up component
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display modify panel
     */
    @Action( ACTION_MOVE_UP_DASHBOARD )
    public String doMoveUpComponent( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID ) );

        Optional<PublicDashboard> opt_public_dashboard = PublicDashboardHome.findByPrimaryKey( nId );

        if ( opt_public_dashboard.isPresent( ) )
        {
            PublicDashboard publicDashboardSelected = opt_public_dashboard.get( );
            List<PublicDashboard> listPublicDashboard = PublicDashboardHome.getPublicDashboardsListByOrder( );

            int nbPosition = 0;

            for ( PublicDashboard dashboard : listPublicDashboard )
            {
                if ( ( dashboard.getId( ) == publicDashboardSelected.getId( ) ) && ( nbPosition != 0 ) )
                {
                    PublicDashboard dashboardPrev = listPublicDashboard.get( nbPosition - 1 );
                    int nNewPosition = dashboardPrev.getPosition( );
                    dashboardPrev.setPosition( publicDashboardSelected.getPosition( ) );
                    publicDashboardSelected.setPosition( nNewPosition );
                    PublicDashboardHome.update( dashboardPrev );
                    PublicDashboardHome.update( publicDashboardSelected );
                }

                nbPosition++;
            }

            // return redirect( request, VIEW_MANAGE_PUBLICDASHBOARDS, PARAMETER_ID, dashboardAssociationSelected.getIdPanel( ) );
        }

        return redirectView( request, VIEW_MANAGE_PUBLICDASHBOARDS );
    }

    /**
     * move down component
     *
     * @param request
     *            The Http request
     * @return The jsp URL to display modify panel
     */
    @Action( ACTION_MOVE_DOWN_DASHBOARD )
    public String doMoveDownComponent( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID ) );

        Optional<PublicDashboard> opt_public_dashboard = PublicDashboardHome.findByPrimaryKey( nId );

        if ( opt_public_dashboard.isPresent( ) )
        {
            PublicDashboard publicDashboardSelected = opt_public_dashboard.get( );
            List<PublicDashboard> listPublicDashboard = PublicDashboardHome.getPublicDashboardsListByOrder( );
            int nbPosition = 0;

            for ( PublicDashboard dashboard : listPublicDashboard )
            {
                if ( ( dashboard.getId( ) == publicDashboardSelected.getId( ) ) && ( nbPosition != ( listPublicDashboard.size( ) - 1 ) ) )
                {
                    PublicDashboard dashboardNext = listPublicDashboard.get( nbPosition + 1 );
                    int nNewPosition = dashboardNext.getPosition( );
                    dashboardNext.setPosition( publicDashboardSelected.getPosition( ) );
                    publicDashboardSelected.setPosition( nNewPosition );
                    PublicDashboardHome.update( dashboardNext );
                    PublicDashboardHome.update( publicDashboardSelected );
                }

                nbPosition++;
            }

            // return redirect( request, VIEW_MANAGE_PUBLICDASHBOARDS, PARAMETER_ID, dashboardAssociationSelected.getIdPanel( ) );
        }

        return redirectView( request, VIEW_MANAGE_PUBLICDASHBOARDS );
    }

    /**
     * Returns the form to create a publicdashboard
     *
     * @param request
     *            The Http request
     * @return the html code of the publicdashboard form
     */
    @View( VIEW_CREATE_PUBLICDASHBOARD )
    public String getCreatePublicDashboard( HttpServletRequest request )
    {
        _publicdashboard = ( _publicdashboard != null ) ? _publicdashboard : new PublicDashboard( );

        PublicDashboardService dashboardService = PublicDashboardService.getInstance( );
        ReferenceList lstTypeDashboard = dashboardService.getPublicDashboardBuilders( );
        // List<String> lstEmplacement = dashboardService.getListDashboardLocation();
        ReferenceList lstEmplacement = dashboardService.getListDashboardLocation( );

        Map<String, Object> model = getModel( );
        model.put( MARK_PUBLICDASHBOARD, _publicdashboard );
        model.put( MARK_LIST_TYPE_DASHBOARD, lstTypeDashboard );
        model.put( MARK_LIST_LOCATION_DASHBOARD, lstEmplacement );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_CREATE_PUBLICDASHBOARD ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_PUBLICDASHBOARD, TEMPLATE_CREATE_PUBLICDASHBOARD, model );
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
    public String doCreatePublicDashboard( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _publicdashboard, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_CREATE_PUBLICDASHBOARD ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _publicdashboard, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_PUBLICDASHBOARD );
        }

        PublicDashboardHome.create( _publicdashboard );
        addInfo( INFO_PUBLICDASHBOARD_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_PUBLICDASHBOARDS );
    }

    /**
     * Manages the removal form of a publicdashboard whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_PUBLICDASHBOARD )
    public String getConfirmRemovePublicDashboard( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PUBLICDASHBOARD ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_PUBLICDASHBOARD ) );
        url.addParameter( PARAMETER_ID_PUBLICDASHBOARD, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_PUBLICDASHBOARD, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a publicdashboard
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage publicdashboards
     */
    @Action( ACTION_REMOVE_PUBLICDASHBOARD )
    public String doRemovePublicDashboard( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PUBLICDASHBOARD ) );
        PublicDashboardHome.remove( nId );
        addInfo( INFO_PUBLICDASHBOARD_REMOVED, getLocale( ) );

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
    public String getModifyPublicDashboard( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PUBLICDASHBOARD ) );

        if ( _publicdashboard == null || ( _publicdashboard.getId( ) != nId ) )
        {
            Optional<PublicDashboard> optPublicDashboard = PublicDashboardHome.findByPrimaryKey( nId );
            _publicdashboard = optPublicDashboard.orElseThrow( ( ) -> new AppException( ERROR_RESOURCE_NOT_FOUND ) );
            ;
        }

        PublicDashboardService dashboardService = PublicDashboardService.getInstance( );
        ReferenceList lstTypeDashboard = dashboardService.getPublicDashboardBuilders( );
        // List<String> lstEmplacement = dashboardService.getListDashboardLocation();
        ReferenceList lstEmplacement = dashboardService.getListDashboardLocation( );

        Map<String, Object> model = getModel( );
        model.put( MARK_PUBLICDASHBOARD, _publicdashboard );
        model.put( MARK_LIST_TYPE_DASHBOARD, lstTypeDashboard );
        model.put( MARK_LIST_LOCATION_DASHBOARD, lstEmplacement );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, ACTION_MODIFY_PUBLICDASHBOARD ) );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_PUBLICDASHBOARD, TEMPLATE_MODIFY_PUBLICDASHBOARD, model );
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
    public String doModifyPublicDashboard( HttpServletRequest request ) throws AccessDeniedException
    {
        populate( _publicdashboard, request, getLocale( ) );

        if ( !SecurityTokenService.getInstance( ).validate( request, ACTION_MODIFY_PUBLICDASHBOARD ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }

        // Check constraints
        if ( !validateBean( _publicdashboard, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_PUBLICDASHBOARD, PARAMETER_ID_PUBLICDASHBOARD, _publicdashboard.getId( ) );
        }

        PublicDashboardHome.update( _publicdashboard );
        addInfo( INFO_PUBLICDASHBOARD_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_PUBLICDASHBOARDS );
    }
}
