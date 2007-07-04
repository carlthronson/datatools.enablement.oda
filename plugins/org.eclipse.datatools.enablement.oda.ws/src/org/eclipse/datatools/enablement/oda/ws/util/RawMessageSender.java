/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.datatools.enablement.oda.ws.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.datatools.enablement.oda.ws.soap.SOAPResponse;

/**
 * 
 */

public class RawMessageSender
{

	private String spec = WSUtil.EMPTY_STRING;
	private String message = WSUtil.EMPTY_STRING;
	private String soapAction = WSUtil.EMPTY_STRING;

	private SOAPResponse soapResponse;
	private HttpURLConnection connection;

	/**
	 * 
	 * @param spec
	 * @param message
	 * @param soapAction
	 */
	public RawMessageSender( String spec, String message, String soapAction )
	{
		this.spec = spec;
		this.message = message;
		this.soapAction = soapAction;
	}

	/**
	 * 
	 */
	public RawMessageSender( )
	{
	}

	/**
	 * 
	 * @param message
	 */
	public void setMessage( String message )
	{
		this.message = message;
	}

	/**
	 * 
	 * @param soapAction
	 */
	public void setSoapAction( String soapAction )
	{
		this.soapAction = soapAction;
	}

	/**
	 * 
	 * @param spec
	 */
	public void setSpec( String spec )
	{
		this.spec = spec;
	}

	/**
	 * 
	 * @return
	 */
	public SOAPResponse getSOAPResponse( )
	{
		Thread t = new Thread( new SOAPResponseCollector( ) );
		t.start( );
		while ( t.isAlive( ) )
		{
		}

		return soapResponse;
	}

	class SOAPResponseCollector implements Runnable
	{

		public void run( )
		{
			try
			{
				URL url = new URL( spec );

				connection = (HttpURLConnection) url.openConnection( );
				connection.setRequestMethod( "POST" ); //$NON-NLS-1$
				connection.setRequestProperty( "Content-Length", //$NON-NLS-1$
						String.valueOf( message.length( ) ) );
				connection.setRequestProperty( "Content-Type", "text/xml" );  //$NON-NLS-1$//$NON-NLS-2$
				connection.setRequestProperty( "Connection", "Close" ); //$NON-NLS-1$ //$NON-NLS-2$
				connection.setRequestProperty( "SoapAction", soapAction ); //$NON-NLS-1$
				connection.setDoOutput( true );

				PrintWriter pw = new PrintWriter( connection.getOutputStream( ) );
				pw.write( message );
				pw.flush( );

				connection.connect( );

				soapResponse = new SOAPResponse( connection.getInputStream( ) );
			}
			catch ( MalformedURLException e )
			{
				if ( !WSUtil.isNull( connection ) )
					soapResponse = new SOAPResponse( connection.getErrorStream( ),
							SOAPResponse.ERROR_STREAM,
							e.getMessage( ) );

			}
			catch ( IOException e )
			{
				if ( !WSUtil.isNull( connection ) )
					soapResponse = new SOAPResponse( connection.getErrorStream( ),
							SOAPResponse.ERROR_STREAM,
							e.getMessage( ) );
			}
		}
	}

}