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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicUserProfile implements IPublicUserProfile
{

    protected String _idUser = "";
    protected Map<String, String> _mapUserInfos = new HashMap<>( );
    protected List<Counter> _listCounters = new ArrayList<>( );
    protected List<Action> _listActions = new ArrayList<>( );

    private PublicUserProfile( )
    {
    }

    public PublicUserProfile( UserProfileBuilder builder )
    {
        this._idUser = builder.getUserId( );
        this._mapUserInfos = builder.getUserInfos( );
        this._listActions = builder.getActions( );
        this._listCounters = builder.getCounters( );
    }

    // getters and setters
    public String getUserId( )
    {
        return _idUser;
    }

    public List<Counter> getCounters( )
    {
        return _listCounters;
    }

    public List<Action> getActions( )
    {
        return _listActions;
    }

    public Map<String, String> getUserInfos( )
    {
        return _mapUserInfos;
    }

    // builder
    public static class UserProfileBuilder
    {

        protected String _idUser = "";
        protected Map<String, String> _mapUserInfos = new HashMap<>( );
        protected List<Counter> _listCounters = new ArrayList<>( );
        protected List<Action> _listActions = new ArrayList<>( );

        // getters
        public String getUserId( )
        {
            return _idUser;
        }

        public Map<String, String> getUserInfos( )
        {
            return _mapUserInfos;
        }

        public List<Counter> getCounters( )
        {
            return _listCounters;
        }

        public List<Action> getActions( )
        {
            return _listActions;
        }

        // builder constructor
        public UserProfileBuilder( String userid )
        {
            this._idUser = userid;
        }

        // builder methods
        public UserProfileBuilder withUserInfos( )
        {
            return this;
        }

        public UserProfileBuilder withCounter( String guid )
        {
            return this;
        }

        public UserProfileBuilder withActions( String guid )
        {
            return this;
        }

        public PublicUserProfile build( )
        {
            return new PublicUserProfile( this );
        }
    }

}
