/*
 * Copyright (C) 2018 The LineageOS Project
 * Copyright (C) 2018 CypherOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.aoscp.hardware.biometrics;

import android.os.HwBinder;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.RemoteException;
import android.os.ServiceManager;

import co.aoscp.internal.biomectrics.IExtBiometricsFingerprint;

public class BiometricsFingerprintExt extends IExtBiometricsFingerprint.Stub {

    public static final int MMI_TYPE_NAV_ENABLE = 41;
    public static final int MMI_TYPE_NAV_DISABLE = 42;

    private static final String DESCRIPTOR =
            "vendor.huawei.hardware.biometrics.fingerprint@2.1::IExtBiometricsFingerprint";
    private static final int TRANSACTION_sendCmdToHal = 20;
	
	// Service name
    private static final String EXT_BIOMECTRICS_SERVICE = "extbiomectrics";

    private static IHwBinder sBiometricsFingerprintExt;

    public BiometricsFingerprintExt() throws RemoteException {
		if (ServiceManager.getService(EXT_BIOMECTRICS_SERVICE) == null) {
            ServiceManager.addService(EXT_BIOMECTRICS_SERVICE, this);
        }
        sBiometricsFingerprintExt = HwBinder.getService(DESCRIPTOR, "default");
    }

	@Override
    public int sendCmdToHal(int cmdId) {
        if (sBiometricsFingerprintExt == null) {
            return -1;
        }

        HwParcel data = new HwParcel();
        HwParcel reply = new HwParcel();

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            data.writeInt32(cmdId);

            sBiometricsFingerprintExt.transact(TRANSACTION_sendCmdToHal, data, reply, 0);

            reply.verifySuccess();
            data.releaseTemporaryStorage();

            return reply.readInt32();
        } catch (Throwable t) {
            return -1;
        } finally {
            reply.release();
        }
    }
	
	@Override
    public int getCmdNavEnableId() {
		return MMI_TYPE_NAV_ENABLE;
	}

	@Override
    public int getCmdNavDisableId() {
		return MMI_TYPE_NAV_DISABLE;
	}
}