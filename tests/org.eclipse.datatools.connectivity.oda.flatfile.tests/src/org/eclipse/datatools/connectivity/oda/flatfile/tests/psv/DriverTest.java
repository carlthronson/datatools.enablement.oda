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

package org.eclipse.datatools.connectivity.oda.flatfile.tests.psv;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.flatfile.CommonConstants;
import org.eclipse.datatools.connectivity.oda.flatfile.tests.DriverTestBase;
import org.eclipse.datatools.connectivity.oda.flatfile.tests.TestUtil;

/**
 * 
 */

public class DriverTest extends DriverTestBase
{
	/**
	 * 
	 * @throws OdaException
	 */
	public DriverTest( ) throws OdaException
	{
		super( );
	}

	/**
	 * 
	 */
	protected String getDelimiter( )
	{
		return CommonConstants.DELIMITER_PIPE_VALUE;
	}

	/**
	 * 
	 */
	protected String getDelimiterName( )
	{
		return CommonConstants.DELIMITER_PIPE;
	}

	/**
	 * 
	 */
	protected String getExtension( )
	{
		return TestUtil.PSV_EXTENSION;
	}
}
