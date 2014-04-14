/**
 * Copyright (c) 2014 Formal Mind GmbH.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Jastram - initial API and implementation
 * 
 */
package org.eclipse.rmf.tests.reqif10.serialization.uc005;

import org.eclipse.rmf.reqif10.ReqIF;
import org.eclipse.rmf.tests.reqif10.serialization.util.AbstractTestCase;
import org.eclipse.rmf.tests.reqif10.serialization.util.CommonSystemAttributes;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoundtripFilteringTests extends AbstractTestCase implements CommonSystemAttributes {

	@BeforeClass
	public static void setupOnce() throws Exception {
		AbstractTestCase.setupOnce();

		String filename = getWorkingFileName("RountripFiltering.reqif"); //$NON-NLS-1$
		System.out.println(filename);
		ReqIF reqif = new RoundtripFilteringModelBuilder().getReqIF();
		saveReqIFFile(reqif, filename);
	}

	@Test
	public void validate() throws Exception {

	}
}
