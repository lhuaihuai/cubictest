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

import java.beans.PropertyChangeEvent;

import org.cubictest.common.utils.ViewUtil;
import org.cubictest.model.Identifier;
import org.cubictest.model.PageElement;
import org.cubictest.ui.gef.directEdit.CubicTestDirectEditManager;
import org.cubictest.ui.gef.directEdit.CubicTestEditorLocator;
import org.cubictest.ui.gef.policies.PageElementComponentEditPolicy;
import org.cubictest.ui.gef.policies.PageElementDirectEditPolicy;
import org.cubictest.ui.gef.view.CubicTestGroupFigure;
import org.cubictest.ui.gef.view.CubicTestLabel;
import org.cubictest.ui.gef.view.TestStepLabel;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Image;


public abstract class PageElementEditPart extends PropertyChangePart {
	
	protected CubicTestDirectEditManager manager;

	
	@Override
	public void propertyChange(PropertyChangeEvent evt){
		super.propertyChange(evt);
		if(PageElement.DIRECT_EDIT_IDENTIFIER.equals(evt.getPropertyName())){
			Identifier oldId = (Identifier) evt.getOldValue();
			Identifier newId = (Identifier) evt.getNewValue();

			if (oldId != null) {
				oldId.removePropertyChangeListener(this);
			}
			
			if (newId != null) {
				newId.addPropertyChangeListener(this);
			}
		}
		else if (Identifier.PROBABILITY.equals(evt.getPropertyName())) {
			refreshVisuals();
		}
	}
		
	@Override
	public void activate() {
		super.activate();
		for (Identifier id : getModel().getIdentifiers()) {
			id.addPropertyChangeListener(this);
		}
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		for (Identifier id : getModel().getIdentifiers()) {
			id.removePropertyChangeListener(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new PageElementComponentEditPolicy());
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PageElementDirectEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, null);
	}
	
	@Override
	protected IFigure createFigure() {
		TestStepLabel label = new TestStepLabel(getModel().getDirectEditIdentifier().getValue());
		label.setIcon(getImage(getModel().isNot()));
		
		label.setLayoutManager(ViewUtil.getFlowLayout());
		label.setLabelAlignment(PositionConstants.LEFT);
		label.setOpaque(true);
		label.setTooltipText(getToolTipText());
		return label;
	}
	
	protected String getToolTipText(){
		String not = getModel().isNot()? "NOT " : "";
		return "Check " + not + getModel().getType().toLowerCase() + 
			" present: $labelText";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#performRequest(org.eclipse.gef.Request)
	 */
	@Override
	public void performRequest(Request request) {
		if(request.getType() == RequestConstants.REQ_DIRECT_EDIT || 
				request.getType() == RequestConstants.REQ_OPEN){
			startDirectEdit();
		}
		super.performRequest(request);
	}
	
	public void startDirectEdit(){
		if (manager == null)
			manager = new CubicTestDirectEditManager(this,
					TextCellEditor.class,
					new CubicTestEditorLocator(
							((CubicTestLabel)getFigure())),
							getModel().getText());
		manager.setText(getModel().getText());
		manager.show();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#setSelected(int)
	 */
	@Override
	public void setSelected(int value) {
		super.setSelected(value);
		if (getFigure() instanceof CubicTestLabel){
			CubicTestLabel figure = (CubicTestLabel) getFigure();
			if (value == EditPart.SELECTED_NONE)
				figure.setSelected(false);
			else
				figure.setSelected(true);
			figure.repaint();
		}
		
		// If this is the last element created, start direct edit
		CommandStack stack = getViewer().getEditDomain().getCommandStack();
		if (manager == null && ViewUtil.pageElementHasJustBeenCreated(stack, getModel()))
			startDirectEdit();
	}
	
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		PageElement element = getModel();
		
		Image newImage = getImage(element.isNot());
		if(getFigure() instanceof TestStepLabel){
			TestStepLabel figure = (TestStepLabel)getFigure();
			if(newImage != null)
				figure.setIcon(newImage);
			figure.setText(element.getText());
			figure.setStatus(element.getStatus());
		}else{
			CubicTestGroupFigure figure = (CubicTestGroupFigure)getFigure();
			figure.setText(element.getText());
			if(newImage != null)
				figure.getHeader().setIcon(newImage);
			figure.setStatus(element.getStatus());
		}
		
		if (manager != null)
			manager.setText(element.getText());
		figure.repaint();
		super.refreshVisuals();
	}
	
	protected abstract Image getImage(boolean isNot);
	
	public final String getType(){
		return getModel().getType();
	}
	
	@Override
	public PageElement getModel() {
		return (PageElement) super.getModel();
	}

}
