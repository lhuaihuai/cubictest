/*******************************************************************************
 * Copyright (c) 2005, 2010  Christian Schwarz
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Christian Schwarz - initial API and implementation
 *******************************************************************************/
package org.cubictest.exporters.watir.ui;

import org.cubictest.common.utils.ErrorHandler;
import org.cubictest.common.utils.UserInfo;
import org.cubictest.export.CubicTestExport;
import org.cubictest.exporters.watir.converters.ContextConverter;
import org.cubictest.exporters.watir.converters.CustomTestStepConverter;
import org.cubictest.exporters.watir.converters.PageElementConverter;
import org.cubictest.exporters.watir.converters.TransitionConverter;
import org.cubictest.exporters.watir.converters.UrlStartPointConverter;
import org.cubictest.exporters.watir.holders.WatirHolder;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;

/**
 * Action for starting Watir export. Will be in context menu in the 
 * Navigator or Package Explorer.
 * 
 * @author chr_schwarz
 */
public class ExportToWatirAction implements IActionDelegate {
	ISelection selection;
	
	public static final String OK_MESSAGE = "Test exported OK to the \"generated\" directory.";
	
	/* 
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		
		try {
			//callback to CubicTest with the selected files
			CubicTestExport.exportSelection((IStructuredSelection) selection, "rb",
					UrlStartPointConverter.class, 
					TransitionConverter.class, 
					CustomTestStepConverter.class, 
					PageElementConverter.class, 
					ContextConverter.class,
					WatirHolder.class);
			UserInfo.showInfoDialog(OK_MESSAGE);
		} 
		catch (Exception e) {
			ErrorHandler.logAndShowErrorDialogAndRethrow("Error occured in CubicTest Watir exporter.", e);
		}
		
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

}
