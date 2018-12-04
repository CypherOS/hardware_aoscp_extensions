/*
 * Copyright (C) 2015-2016 The CyanogenMod Project
 *               2017-2018 The LineageOS Project
 *               2018 CypherOS
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
package co.aoscp.server;

import android.content.Context;
import android.content.Intent;
import android.Manifest;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;

import aoscp.content.HardwareContext;
import aoscp.content.HardwareIntent;
import aoscp.hardware.DeviceHardwareManager;
import aoscp.hardware.IDeviceHardwareService;

import co.aoscp.hwcontrollers.DisplayEngineController;

import com.android.server.HwSystemService;

import java.util.ArrayList;

/** @hide */
public class DeviceHardwareService extends HwSystemService {

    private static final String TAG = DeviceHardwareService.class.getSimpleName();

    private final Context mContext;
    private final HardwareInterface mHwImpl;

    private interface HardwareInterface {
        public int getSupportedFeatures();
        public boolean get(int feature);
        public boolean set(int feature, boolean enable);

        // DisplayEngine
        public int[] getDisplayModes();
        public int getCurrentDisplayMode();
        public int getDefaultDisplayMode();
        public boolean setDisplayMode(int mode, boolean makeDefault);
        public String getDisplayModeName(int mode);
    }

    private class LegacyHardware implements HardwareInterface {

        private int mSupportedFeatures = 0;

        public LegacyHardware() {
            if (DisplayEngineController.isSupported())
                mSupportedFeatures |= DeviceHardwareManager.FEATURE_DISPLAY_ENGINE;
        }

        public int getSupportedFeatures() {
            return mSupportedFeatures;
        }

        public boolean get(int feature) {
            return false;
        }

        public boolean set(int feature, boolean enable) {
            return false;
        }

        public int[] getDisplayModes() {
            return DisplayEngineController.getAvailableModes();
        }

        public int getCurrentDisplayMode() {
            return DisplayEngineController.getCurrentMode();
        }

        public int getDefaultDisplayMode() {
            return DisplayEngineController.getDefaultMode();
        }

        public boolean setDisplayMode(int mode, boolean makeDefault) {
            return DisplayEngineController.setMode(mode, makeDefault);
        }

        public String getDisplayModeName(int mode) {
            return DisplayEngineController.getModeName(mode);
        }
    }

    private HardwareInterface getImpl(Context context) {
        return new LegacyHardware();
    }

    public DeviceHardwareService(Context context) {
        super(context);
        mContext = context;
        mHwImpl = getImpl(context);
        publishBinderService(HardwareContext.DEVICE_HARDWARE_SERVICE, mService);
    }

    @Override
    public String getHardwareFeatures() {
        return HardwareContext.Features.HARDWARE_AOSCP;
    }

    @Override
    public void onBootPhase(int phase) {
        if (phase == PHASE_BOOT_COMPLETED) {
            Intent intent = new Intent(HardwareIntent.ACTION_INITIALIZE_DEVICE_HARDWARE);
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            mContext.sendBroadcastAsUser(intent, UserHandle.ALL,
                    android.Manifest.permission.DEVICE_HARDWARE_ACCESS);
        }
    }

    @Override
    public void onStart() {
    }

    private final IBinder mService = new IDeviceHardwareService.Stub() {

        private boolean isSupported(int feature) {
            return (getSupportedFeatures() & feature) == feature;
        }

        @Override
        public int getSupportedFeatures() {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.DEVICE_HARDWARE_ACCESS, null);
            return mHwImpl.getSupportedFeatures();
        }

        @Override
        public boolean get(int feature) {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.DEVICE_HARDWARE_ACCESS, null);
            if (!isSupported(feature)) {
                Log.e(TAG, "feature " + feature + " is not supported");
                return false;
            }
            return mHwImpl.get(feature);
        }

        @Override
        public boolean set(int feature, boolean enable) {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.DEVICE_HARDWARE_ACCESS, null);
            if (!isSupported(feature)) {
                Log.e(TAG, "feature " + feature + " is not supported");
                return false;
            }
            return mHwImpl.set(feature, enable);
        }

        @Override
        public int[] getDisplayModes() {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.DEVICE_HARDWARE_ACCESS, null);
            if (!isSupported(DeviceHardwareManager.FEATURE_DISPLAY_ENGINE)) {
                Log.e(TAG, "Display modes are not supported");
                return null;
            }
            return mHwImpl.getDisplayModes();
        }

        @Override
        public int getCurrentDisplayMode() {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.DEVICE_HARDWARE_ACCESS, null);
            if (!isSupported(DeviceHardwareManager.FEATURE_DISPLAY_ENGINE)) {
                Log.e(TAG, "Display modes are not supported");
                return -1;
            }
            return mHwImpl.getCurrentDisplayMode();
        }

        @Override
        public int getDefaultDisplayMode() {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.DEVICE_HARDWARE_ACCESS, null);
            if (!isSupported(DeviceHardwareManager.FEATURE_DISPLAY_ENGINE)) {
                Log.e(TAG, "Display modes are not supported");
                return -1;
            }
            return mHwImpl.getDefaultDisplayMode();
        }

        @Override
        public boolean setDisplayMode(int mode, boolean makeDefault) {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.DEVICE_HARDWARE_ACCESS, null);
            if (!isSupported(DeviceHardwareManager.FEATURE_DISPLAY_ENGINE)) {
                Log.e(TAG, "Display modes are not supported");
                return false;
            }
            return mHwImpl.setDisplayMode(mode, makeDefault);
        }

        @Override
        public String getDisplayModeName(int mode) {
            mContext.enforceCallingOrSelfPermission(
                    android.Manifest.permission.DEVICE_HARDWARE_ACCESS, null);
            if (!isSupported(DeviceHardwareManager.FEATURE_DISPLAY_ENGINE)) {
                Log.e(TAG, "Display modes are not supported");
                return null;
            }
            return mHwImpl.getDisplayModeName(mode);
        }
    };
}