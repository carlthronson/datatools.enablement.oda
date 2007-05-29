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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.Service;

import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.datatools.enablement.oda.ws.ui.Activator;
import org.eclipse.datatools.enablement.oda.ws.ui.i18n.Messages;
import org.eclipse.datatools.enablement.oda.ws.ui.util.Constants;
import org.eclipse.datatools.enablement.oda.ws.ui.util.WSConsole;
import org.eclipse.datatools.enablement.oda.ws.ui.util.WSUIUtil;
import org.eclipse.datatools.enablement.oda.ws.util.WSDLAdvisor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 */

public class OperationPage extends DataSetWizardPage
{

	private transient Tree operationTree;
	private transient Label operationName;
	private transient Label operationDescription;

	private String operationTrace = WSUIUtil.EMPTY_STRING;
	private String initOperationTrace = WSUIUtil.EMPTY_STRING;
	private String wsdlURI = WSUIUtil.EMPTY_STRING;

	private Image wsdlImage;
	private Image serviceImage;
	private Image portImage;
	private Image operationImage;

	private static String DEFAULT_MESSAGE = Messages.getString( "operationPage.message.default" );//$NON-NLS-1$

	/**
	 * 
	 * @param pageName
	 */
	public OperationPage( String pageName )
	{
		super( pageName );
		setMessage( DEFAULT_MESSAGE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#createPageCustomControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPageCustomControl( Composite parent )
	{
		initialImages( );
		setControl( createPageControl( parent ) );
		initializeControl( );
	}

	private void initialImages( )
	{
		wsdlImage = Activator.getDefault( )
				.getImageRegistry( )
				.get( Activator.ICON_WSDL );
		serviceImage = Activator.getDefault( )
				.getImageRegistry( )
				.get( Activator.ICON_SERVICE );
		portImage = Activator.getDefault( )
				.getImageRegistry( )
				.get( Activator.ICON_PORT );
		operationImage = Activator.getDefault( )
				.getImageRegistry( )
				.get( Activator.ICON_OPERATION );
	}

	private Control createPageControl( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 1, false );
		layout.verticalSpacing = 30;
		composite.setLayout( layout );
		GridData layoutData = new GridData( GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_FILL );
		composite.setLayoutData( layoutData );

		setupTreeComposite( composite );
		setupTextComposite( composite );

		return composite;
	}

	private void setupTreeComposite( Composite parent )
	{
		operationTree = new Tree( parent, SWT.BORDER
				| SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL );
		operationTree.setLayout( new GridLayout( ) );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.heightHint = 100;
		operationTree.setLayoutData( layoutData );

		operationTree.addSelectionListener( new SelectionAdapter( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected( SelectionEvent event )
			{
				handle( );
				testDirty( );
			}

			private void handle( )
			{
				TreeItem item = operationTree.getSelection( )[0];

				if ( item.getData( ) instanceof Operation )
				{
					Operation operation = (Operation) item.getData( );
					operationTrace = toOperationTrace( item );
					operationName.setText( WSUIUtil.getNonNullString( operation.getName( ) ) );
					operationDescription.setText( WSDLAdvisor.retrieveDocument( operation ) );
					setPageComplete( true );
				}
				else
				{
					operationName.setText( WSUIUtil.EMPTY_STRING );
					operationDescription.setText( WSUIUtil.EMPTY_STRING );
					setPageComplete( false );
				}
			}

			private void testDirty( )
			{
				if ( !WSUIUtil.isNull( initOperationTrace )
						&& !initOperationTrace.equals( operationTrace ) )
					setMessage( Messages.getString( "operationPage.message.operationChanged" ), //$NON-NLS-1$
							INFORMATION );

				else
					setMessage( DEFAULT_MESSAGE );
			}

		} );
	}

	// TODO refine me
	private String toOperationTrace( TreeItem item )
	{
		Service service = (Service) item.getParentItem( )
				.getParentItem( )
				.getData( );
		Port port = (Port) item.getParentItem( ).getData( );
		Operation operation = (Operation) item.getData( );
		return service.getQName( ).getLocalPart( )
				+ Constants.DELIMITER_OPEARTION + port.getName( )
				+ Constants.DELIMITER_OPEARTION + operation.getName( );
	}

	private void setupTextComposite( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 3, false );
		layout.verticalSpacing = 20;
		layout.horizontalSpacing = 30;
		composite.setLayout( layout );
		GridData layoutData = new GridData( GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_FILL );
		composite.setLayoutData( layoutData );

		Label label = new Label( composite, SWT.NONE );
		layoutData = new GridData( );
		layoutData.widthHint = 100;
		label.setLayoutData( layoutData );
		label.setText( Messages.getString( "operationPage.label.selectOpearation" ) );//$NON-NLS-1$

		operationName = new Label( composite, SWT.BORDER );
		layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.horizontalSpan = 2;
		operationName.setLayoutData( layoutData );

		label = new Label( composite, SWT.NONE );
		layoutData = new GridData( );
		layoutData.widthHint = 100;
		label.setLayoutData( layoutData );
		label.setText( Messages.getString( "operationPage.label.document" ) ); //$NON-NLS-1$

		operationDescription = new Label( composite, SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL );
		layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.horizontalSpan = 2;
		layoutData.heightHint = 40;
		operationDescription.setLayoutData( layoutData );
	}

	/**
	 * Initializes the page control with the last edited data set design.
	 */
	private void initializeControl( )
	{
		initWSConsole( );
		initFromModel( );

		populateTree( );
		setPageComplete( false );
	}

	private void initWSConsole( )
	{
		if ( !WSConsole.getInstance( ).isSessionOK( ) )
			WSConsole.getInstance( ).start( getInitializationDesign( ) );
	}

	private void initFromModel( )
	{
		wsdlURI = WSConsole.getInstance( )
				.getPropertyValue( Constants.WSDL_URI );
		operationTrace = WSConsole.getInstance( )
				.getPropertyValue( Constants.OPERATION_TRACE );
		initOperationTrace = WSConsole.getInstance( )
				.getPropertyValue( Constants.OPERATION_TRACE );
	}

	// TODO refine me
	private void populateTree( )
	{
		if ( WSUIUtil.isNull( wsdlURI ) )
			return;

		operationTree.removeAll( );

		TreeItem root = new TreeItem( operationTree, SWT.NONE );
		root.setText( wsdlURI );
		root.setImage( wsdlImage );
		Definition definition = WSDLAdvisor.getDefinition( wsdlURI );
		if ( definition == null )
			return;

		Map services = definition.getServices( );
		Iterator srcIT = services.keySet( ).iterator( );
		while ( srcIT.hasNext( ) )
		{
			Service service = (Service) services.get( srcIT.next( ) );
			TreeItem srcTI = populateTreeItem( root,
					service,
					service.getQName( ).getLocalPart( ),
					serviceImage );// TI: treeItem
			Map ports = service.getPorts( );
			Iterator prtIT = ports.keySet( ).iterator( );// IT:iterator
			while ( prtIT.hasNext( ) )
			{
				Port port = (Port) ports.get( prtIT.next( ) );
				TreeItem prtTI = populateTreeItem( srcTI,
						port,
						port.getName( ),
						portImage );
				List operations = port.getBinding( )
						.getPortType( )
						.getOperations( );
				for ( int i = 0; i < operations.size( ); i++ )
				{
					TreeItem treeItem = populateTreeItem( prtTI,
							operations.get( i ),
							( (Operation) operations.get( i ) ).getName( ),
							operationImage );
					if ( !WSUIUtil.isNull( operationTrace )
							&& operationTrace.equals( toOperationTrace( treeItem ) ) )
						highlight( treeItem );
				}
			}
		}
	}

	// TODO lazy load
	private TreeItem populateTreeItem( TreeItem parent, Object child,
			String text, Image image )
	{
		TreeItem item = new TreeItem( parent, SWT.NONE );
		item.setData( child );
		item.setText( text );
		item.setImage( image );

		return item;
	}

	private void highlight( TreeItem treeItem )
	{
		if ( WSUIUtil.isNull( operationTrace ) )
			return;

		FontData fontData = new FontData( WSUIUtil.EMPTY_STRING, 8, SWT.BOLD );
		treeItem.setFont( new Font( null, fontData ) );

		operationTree.setSelection( new TreeItem[]{
			treeItem
		} );
		operationTree.setFocus( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */
	protected DataSetDesign collectDataSetDesign( DataSetDesign design )
	{
		WSUIUtil.checkExisted( design );
		savePage( design );

		return design;
	}

	private void savePage( DataSetDesign design )
	{
		if ( !WSConsole.getInstance( ).isSessionOK( ) )
			return;

		design.getPrivateProperties( ).setProperty( Constants.OPERATION_TRACE,
				WSConsole.getInstance( )
						.getPropertyValue( Constants.OPERATION_TRACE ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#refresh(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */
	protected void refresh( DataSetDesign dataSetDesign )
	{
		super.refresh( dataSetDesign );

		refresh( );
	}

	private void refresh( )
	{
		initFromModel( );
		populateTree( );
		
		setMessage( DEFAULT_MESSAGE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#canLeave()
	 */
	protected boolean canLeave( )
	{
		saveToModle( );
		return super.canLeave( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 */
	public IWizardPage getNextPage( )
	{
		saveToModle( );

		IWizardPage page = super.getNextPage( );
		if ( page instanceof SOAPRequestPage )
			( (SOAPRequestPage) page ).refresh( );

		return page;
	}

	private void saveToModle( )
	{
		WSConsole.getInstance( ).setPropertyValue( Constants.OPERATION_TRACE,
				operationTrace );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#cleanup()
	 */
	protected void cleanup( )
	{
		WSConsole.getInstance( ).terminateSession( );
	}

}
