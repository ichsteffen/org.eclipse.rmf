/*******************************************************************************
 * Copyright (c) 2011, 2012 itemis GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Nirmal Sasidharan - initial API and implementation
 ******************************************************************************/

package org.eclipse.rmf.rif11.xsd.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.rmf.rif11.xsd.RifPackage;
import org.eclipse.rmf.rif11.xsd.SPECGROUP;
import org.eclipse.rmf.rif11.xsd.TARGETGROUPType;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TARGETGROUP Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.rmf.rif11.xsd.impl.TARGETGROUPTypeImpl#getSPECGROUPREF <em>SPECGROUPREF</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TARGETGROUPTypeImpl extends EObjectImpl implements TARGETGROUPType {
	/**
	 * The cached value of the '{@link #getSPECGROUPREF() <em>SPECGROUPREF</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSPECGROUPREF()
	 * @generated
	 * @ordered
	 */
	protected SPECGROUP sPECGROUPREF;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TARGETGROUPTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RifPackage.Literals.TARGETGROUP_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SPECGROUP getSPECGROUPREF() {
		return sPECGROUPREF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSPECGROUPREF(SPECGROUP newSPECGROUPREF) {
		SPECGROUP oldSPECGROUPREF = sPECGROUPREF;
		sPECGROUPREF = newSPECGROUPREF;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RifPackage.TARGETGROUP_TYPE__SPECGROUPREF, oldSPECGROUPREF, sPECGROUPREF));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RifPackage.TARGETGROUP_TYPE__SPECGROUPREF:
				return getSPECGROUPREF();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case RifPackage.TARGETGROUP_TYPE__SPECGROUPREF:
				setSPECGROUPREF((SPECGROUP)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case RifPackage.TARGETGROUP_TYPE__SPECGROUPREF:
				setSPECGROUPREF((SPECGROUP)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case RifPackage.TARGETGROUP_TYPE__SPECGROUPREF:
				return sPECGROUPREF != null;
		}
		return super.eIsSet(featureID);
	}

} //TARGETGROUPTypeImpl
