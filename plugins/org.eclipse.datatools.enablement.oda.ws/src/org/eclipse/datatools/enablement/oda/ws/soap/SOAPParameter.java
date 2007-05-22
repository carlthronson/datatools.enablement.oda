/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.datatools.enablement.oda.ws.soap;

import java.sql.Types;

import org.eclipse.datatools.enablement.oda.ws.util.WSUtil;

/**
 * 
 */

public class SOAPParameter
{

	private int id;
	private String name;
	private int type;
	private String value;

	/**
	 * 
	 * @param id
	 * @param name
	 */
	public SOAPParameter( int id, String name )
	{
		this( id, name, Types.VARCHAR );
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 */
	public SOAPParameter( int id, String name, int type )
	{
		this( id, name, type, WSUtil.EMPTY_STRING );
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param defaultValue
	 */
	public SOAPParameter( int id, String name, String defaultValue )
	{
		this( id, name, Types.VARCHAR, defaultValue );
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param defaultValue
	 */
	public SOAPParameter( int id, String name, int type, String defaultValue )
	{
		this.id = id;
		this.name = name;
		this.type = type;
		this.value = defaultValue;
	}

	/**
	 * 
	 * @return
	 */
	public int getId( )
	{
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public String getName( )
	{
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public int getType( )
	{
		return type;
	}

	/**
	 * 
	 * @return
	 */
	public String getDefaultValue( )
	{
		return value;
	}

	/**
	 * 
	 * @param value
	 */
	public void setDefaultValue( String value )
	{
		this.value = value;
	}

}
