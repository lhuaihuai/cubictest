/*
 * Created on 14.jan.2006
 * 
 * This software is licensed under the terms of the GNU GENERAL PUBLIC LICENSE
 * Version 2, which can be found at http://www.gnu.org/copyleft/gpl.html
 *
 */
package org.cubictest.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.cubictest.model.AbstractPage;
import org.cubictest.model.ExtensionPoint;
import org.cubictest.model.ExtensionStartPoint;
import org.cubictest.model.ExtensionTransition;
import org.cubictest.model.Page;
import org.cubictest.model.SimpleTransition;
import org.cubictest.model.SubTestStartPoint;
import org.cubictest.model.Test;
import org.cubictest.model.TransitionNode;
import org.cubictest.model.UrlStartPoint;
import org.cubictest.ui.gef.interfaces.exported.ITestEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.draw2d.geometry.Point;


/**
 * Util for new test wizards.
 * 
 * @author chr_schwarz
 */
public class WizardUtils {
	

	/**
	 * Creates an empty test with a ExtensionStartPoint.
	 */
	public static Test createEmptyTest(String id, String name, 
			String description, IFile file, ExtensionPoint point) {

		Test test = createTest(id,name,description);

		ExtensionStartPoint esp = createExtensionStartPoint(file, point, test);
		test.setStartPoint(esp);
		
		ExtensionTransition startTransition = 
			new ExtensionTransition(esp, test.getPages().get(0),point);
		test.addTransition(startTransition);
		
		return test;
	}

	public static ExtensionStartPoint createExtensionStartPoint(IFile file, ExtensionPoint point, Test test) {
		ExtensionStartPoint esp = new ExtensionStartPoint(file.getProjectRelativePath().toPortableString(), file.getProject());
		esp.setProject(file.getProject());
		esp.setSourceExtensionPointName(point.getName());
		esp.setSourceExtensionPointPageId(point.getPageId());
		esp.setPosition(new Point(4, 4));
		return esp;
	}
	
	/**
	 * Creates an empty test with a UrlStartPoint.
	 */
	public static Test createEmptyTest(String id, String name, 
			String description, String url) {
		Test test = createTest(id,name,description);
		
		UrlStartPoint startpoint = createUrlStartPoint(url, test);
		test.setStartPoint(startpoint);
		
		SimpleTransition startTransition = new SimpleTransition(startpoint, test.getPages().get(0));	
		test.addTransition(startTransition);
		
		return test;
	}

	/**
	 * Creates an empty test with a SubTest start point.
	 */
	public static Test createEmptyTestWithSubTestStartPoint(String id, String name, String description) {
		Test test = createTest(id,name,description);
		
		SubTestStartPoint startpoint = createSubTestStartPoint(test);
		test.setStartPoint(startpoint);
		
		SimpleTransition startTransition = new SimpleTransition(startpoint, test.getPages().get(0));	
		test.addTransition(startTransition);
		
		return test;
	}
	
	public static UrlStartPoint createUrlStartPoint(String url, Test test) {
		UrlStartPoint startpoint = new UrlStartPoint();
		startpoint.setBeginAt(url);
		startpoint.setName("URL");
		startpoint.setPosition(new Point(4, 4));
		return startpoint;
	}
	
	public static SubTestStartPoint createSubTestStartPoint(Test test) {
		SubTestStartPoint startpoint = new SubTestStartPoint();
		startpoint.setName("SubTest start point");
		startpoint.setPosition(new Point(4, 4));
		return startpoint;
	}
	
	private static Test createTest(String id, String name, String description){
		Test test = new Test();
		test.setId(id);
		test.setName(name);
		test.setDescription(description);
		
		Page page = new Page();
		page.setPosition(new Point(ITestEditor.INITIAL_PAGE_POS_X, ITestEditor.INITIAL_PAGE_POS_Y));
		page.setDimension(TransitionNode.getDefaultDimension());
		page.setName("First Page");
		
		List<AbstractPage> pages = new ArrayList<AbstractPage>();
		pages.add(page);
		test.setPages(pages);
		
		return test;
	}
}
