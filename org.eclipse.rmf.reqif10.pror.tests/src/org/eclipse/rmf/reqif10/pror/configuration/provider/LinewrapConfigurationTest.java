/*******************************************************************************
 * Copyright (c) 2014 Formal Mind GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Jastram - initial API and implementation
 ******************************************************************************/
package org.eclipse.rmf.reqif10.pror.configuration.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.rmf.reqif10.pror.presentation.linewrap.LinewrapFactory;
import org.eclipse.rmf.reqif10.pror.presentation.linewrap.provider.LinewrapItemProviderAdapterFactory;
import org.junit.Before;


public class LinewrapConfigurationTest extends
		ProrPresentationConfigurationTest {
	
	@Before
	public void setup() {
		setFixture(LinewrapFactory.eINSTANCE.createLinewrapConfiguration());
	}

	@Override
	AdapterFactory createAdapterFactory() {
		return new LinewrapItemProviderAdapterFactory();
	}

}
