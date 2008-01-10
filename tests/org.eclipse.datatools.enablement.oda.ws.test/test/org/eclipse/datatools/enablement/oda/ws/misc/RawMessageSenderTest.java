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

package org.eclipse.datatools.enablement.oda.ws.misc;

import org.eclipse.datatools.enablement.oda.ws.BaseTest;
import org.eclipse.datatools.enablement.oda.ws.soap.SOAPRequest;
import org.eclipse.datatools.enablement.oda.ws.util.TestConstants;
import org.eclipse.datatools.enablement.oda.ws.util.RawMessageSender;
import org.eclipse.datatools.enablement.oda.ws.util.WSDLAdvisor;

/**
 * 
 */

public class RawMessageSenderTest extends BaseTest
{

	public void testThread( )
	{
		String[] wsdlURIs = {
				TestConstants.WSDL_FILE_TEMPCONVERT,
				TestConstants.WSDL_FILE_STOCKQUOTES
		};
		String[] operationTraces = {
				TestConstants.OPERATION_TRACE_TEMPCONVERT,
				TestConstants.OPERATION_TRACE_STOCKQUOTES
		};

		String[][] parameters = {
				{
					"37"
				}, {
					"ibm"
				}
		};

		for ( int i = 0; i < wsdlURIs.length; i++ )
		{
			String spec = WSDLAdvisor.getLocationURI( wsdlURIs[i],
					operationTraces[i] );
			WSDLAdvisor wsdlAdvisor=new WSDLAdvisor();
			String query = wsdlAdvisor.getSOAPRequestTemplate( wsdlURIs[i],
					operationTraces[i] );
			String soapAction = WSDLAdvisor.getSOAPActionURI( wsdlURIs[i],
					operationTraces[i] );

			SOAPRequest soapRequest = new SOAPRequest( query );
			for ( int j = 0; j < parameters[i].length; j++ )
			{
				soapRequest.setParameterValue( j + 1, parameters[i][j] );
			}
			String message = soapRequest.toXML( );

			RawMessageSender rms = new RawMessageSender( spec,
					message,
					soapAction );

			assertEquals( 0, rms.getSOAPResponse( 0 ).getStreamType( ) );
		}
	}

}
