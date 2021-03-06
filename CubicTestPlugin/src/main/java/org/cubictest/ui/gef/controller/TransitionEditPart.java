/*******************************************************************************
 * Copyright (c) 2005, 2010 Stein K. Skytteren and Christian Schwarz
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Stein K. Skytteren and Christian Schwarz - initial API and implementation
 *******************************************************************************/
package org.cubictest.ui.gef.controller;

import java.beans.PropertyChangeListener;

import org.cubictest.model.PropertyAwareObject;
import org.cubictest.model.Transition;
import org.cubictest.ui.gef.policies.TransitionConnectionEditPolicy;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;


/**
 * @author SK Skytteren
 * Contoller for the <code>Transition</code> model.
 *
 */
public abstract class TransitionEditPart extends AbstractConnectionEditPart implements PropertyChangeListener{
	
	/**
	 * Constructor for <code>TransitionEditPart</code>.
	 * @param transition the model
	 */
	public TransitionEditPart(Transition transition) {
		super();
		setModel(transition);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ROLE,new TransitionConnectionEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
	}
	/**
	 * @see org.eclipse.gef.editparts.AbstractConnectionEditPart#createFigure()
	 */
	protected IFigure createFigure()
	{
		PolylineConnection conn = (PolylineConnection) super.createFigure();
		conn.setConnectionRouter(new BendpointConnectionRouter());
		conn.setTargetDecoration(new PolygonDecoration());
		conn.setToolTip(new Label("Connection that merges / connects two states (no user interaction, and thus no state change)"));
		
		return conn;
	}
	/*
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate(){
		super.activate();
		PropertyAwareObject propertyAwareObject = (PropertyAwareObject) getModel();
		propertyAwareObject.addPropertyChangeListener(this);
	}

	/*
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate(){
		super.deactivate();
		PropertyAwareObject propertyAwareObject = (PropertyAwareObject) getModel();
		propertyAwareObject.removePropertyChangeListener(this);
	}
	/**
	 * Sets the width of the line when selected
	 */
	public void setSelected(int value)
	{
		super.setSelected(value);
		if (value != EditPart.SELECTED_NONE)
			((PolylineConnection) getFigure()).setLineWidth(2);
		else
			((PolylineConnection) getFigure()).setLineWidth(1);
	}
	
}
