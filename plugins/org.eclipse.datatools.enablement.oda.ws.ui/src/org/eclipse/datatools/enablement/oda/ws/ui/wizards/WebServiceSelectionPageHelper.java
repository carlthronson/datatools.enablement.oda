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

package org.eclipse.datatools.enablement.oda.ws.ui.wizards;

import java.util.Properties;

import org.eclipse.datatools.enablement.oda.ws.ui.i18n.Messages;
import org.eclipse.datatools.enablement.oda.ws.ui.util.WSUIUtil;
import org.eclipse.datatools.enablement.oda.ws.util.Constants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class WebServiceSelectionPageHelper
{

	private WizardPage wizardPage;
	private PreferencePage propertyPage;

	private transient Text wsdlURI;
	private transient Text soapEndPoint;
	private transient Text customClass;

	static final String DEFAULT_MESSAGE = Messages.getString( "webServiceSelectionPage.message.default" ); //$NON-NLS-1$

	WebServiceSelectionPageHelper( WizardPage page )
	{
		wizardPage = page;
	}

	WebServiceSelectionPageHelper( PreferencePage page )
	{
		propertyPage = page;
	}

	/**
	 * 
	 * @param parent
	 */
	void createCustomControl( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 1, false );
		layout.verticalSpacing = 10;
		composite.setLayout( layout );

		setupWSDLGroup( composite );
		setupEndPointGroup( composite );
		setupCustomClassGroup( composite );
	}

	private void setupWSDLGroup( Composite parent )
	{
		Group group = new Group( parent, SWT.SHADOW_ETCHED_IN );
		group.setLayout( new GridLayout( 3, false ) );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		group.setLayoutData( layoutData );
		group.setText( Messages.getString( "webServiceSelectionPage.group.wsdl" ) );//$NON-NLS-1$

		Label label = new Label( group, SWT.NONE );
		layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.horizontalSpan = 3;
		layoutData.heightHint = 20;
		label.setLayoutData( layoutData );
		label.setText( Messages.getString( "webServiceSelectionPage.label.wsdl" ) );//$NON-NLS-1$

		label = new Label( group, SWT.NONE );
		layoutData = new GridData( );
		label.setLayoutData( layoutData );
		label.setText( Messages.getString( "webServiceSelectionPage.label.wsdlURI" ) );//$NON-NLS-1$

		wsdlURI = new Text( group, SWT.BORDER );
		layoutData = new GridData( GridData.FILL_HORIZONTAL );
		wsdlURI.setLayoutData( layoutData );
		wsdlURI.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				verifyPage( );
			}
		} );

		Button button = new Button( group, SWT.NONE );
		layoutData = new GridData( );
		layoutData.widthHint = 70;
		button.setLayoutData( layoutData );
		button.setText( Messages.getString( "webServiceSelectionPage.button.browse" ) ); //$NON-NLS-1$
		button.addSelectionListener( new SelectionAdapter( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected( SelectionEvent e )
			{
				FileDialog dialog = new FileDialog( PlatformUI.getWorkbench( )

				.getDisplay( ).getActiveShell( ), SWT.OPEN );
				dialog.setFilterExtensions( new String[]{
						"*.wsdl", "*.*"
				} );
				if ( wsdlURI.getText( ) != null
						&& wsdlURI.getText( ).trim( ).length( ) > 0 )
				{
					dialog.setFilterPath( wsdlURI.getText( ) );
				}

				String selectedLocation = dialog.open( );
				if ( selectedLocation != null )
				{
					wsdlURI.setText( selectedLocation );
				}
			}

		} );
	}

	private void setupEndPointGroup( Composite parent )
	{
		Group group = new Group( parent, SWT.SHADOW_ETCHED_IN );
		group.setLayout( new GridLayout( 1, false ) );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		group.setLayoutData( layoutData );
		group.setText( Messages.getString( "webServiceSelectionPage.group.endPoint" ) );//$NON-NLS-1$

		Label label = new Label( group, SWT.NONE );
		layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.heightHint = 20;
		label.setLayoutData( layoutData );
		label.setText( Messages.getString( "webServiceSelectionPage.label.endPoint" ) );//$NON-NLS-1$

		soapEndPoint = new Text( group, SWT.BORDER );
		layoutData = new GridData( GridData.FILL_HORIZONTAL );
		soapEndPoint.setLayoutData( layoutData );
		soapEndPoint.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				verifyPage( );
			}
		} );
	}

	private void setupCustomClassGroup( Composite parent )
	{
		Group group = new Group( parent, SWT.SHADOW_ETCHED_IN );
		group.setLayout( new GridLayout( 1, false ) );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		group.setLayoutData( layoutData );
		group.setText( Messages.getString( "webServiceSelectionPage.group.customClass" ) );//$NON-NLS-1$

		Label label = new Label( group, SWT.NONE );
		layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.heightHint = 20;
		label.setLayoutData( layoutData );
		label.setText( Messages.getString( "webServiceSelectionPage.label.customClass" ) );//$NON-NLS-1$

		customClass = new Text( group, SWT.BORDER );
		layoutData = new GridData( GridData.FILL_HORIZONTAL );
		customClass.setLayoutData( layoutData );
	}

	/**
	 * SoapEndPoint or at least a wsdl file with a workable soapEndpoint is
	 * expected here
	 * 
	 * @param props
	 * @return
	 */
	Properties collectCustomProperties( Properties props )
	{
		if ( props == null )
			props = new Properties( );

		Properties prop = new Properties( );
		prop.setProperty( Constants.SOAP_ENDPOINT, soapEndPoint.getText( ) );
		prop.setProperty( Constants.CUSTOM_CONNECTION_CLASS,
				customClass.getText( ) );
		prop.setProperty( Constants.WSDL_URI, wsdlURI.getText( ) );

		return prop;
	}

	/**
	 * 
	 * @param profileProps
	 */
	void initCustomControl( Properties profileProps )
	{
		if ( profileProps == null || profileProps.isEmpty( ) )
		{
			setPageComplete( false );
			setMessage( DEFAULT_MESSAGE, IMessageProvider.NONE );
			return; // nothing to initialize
		}

		wsdlURI.setText( WSUIUtil.getNonNullString( profileProps.getProperty( Constants.WSDL_URI ) ) );
		soapEndPoint.setText( WSUIUtil.getNonNullString( profileProps.getProperty( Constants.SOAP_ENDPOINT ) ) );
		customClass.setText( WSUIUtil.getNonNullString( profileProps.getProperty( Constants.CUSTOM_CONNECTION_CLASS ) ) );
	}

	private void verifyPage( )
	{
		if ( WSUIUtil.isNull( soapEndPoint.getText( ) )
				&& WSUIUtil.isNull( wsdlURI.getText( ) ) )
		{
			setPageComplete( false );
			setMessage( Messages.getString( "webServiceSelectionPage.message.error" ),
					IMessageProvider.ERROR );
		}
		else
		{
			setPageComplete( true );
			setMessage( DEFAULT_MESSAGE, IMessageProvider.NONE );
		}
	}

	/**
	 * 
	 * @param complete
	 */
	private void setPageComplete( boolean complete )
	{
		if ( wizardPage != null )
			wizardPage.setPageComplete( complete );
		else if ( propertyPage != null )
			propertyPage.setValid( complete );
	}

	/**
	 * 
	 * @param newMessage
	 * @param newType
	 */
	private void setMessage( String newMessage, int newType )
	{
		if ( wizardPage != null )
			wizardPage.setMessage( newMessage, newType );
		else if ( propertyPage != null )
			propertyPage.setMessage( newMessage, newType );
	}

}