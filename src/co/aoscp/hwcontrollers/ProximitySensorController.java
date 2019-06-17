/*
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

package co.aoscp.hwcontrollers;

import android.database.ContentObserver;
import android.util.Log;

/*
 * Doze Sensor API (Proximity)
 *
 * This API is for devices to trigger doze via the
 * proximity sensor, whether it be using the system
 * sensor as a type or the devices built in sensor 
 * configuration
 */

public class ProximitySensorController {

    /*
     * All HAF classes should export this boolean.
     * Real implementations must, of course, return true
     */
    public static boolean isSupported() {
        return false;
    }

    /*
     * Enable/Disable the sensor. This call is performed
     * in DozeSensors. We check that status with [boolean: listening]
     */
    public static boolean setSensor(boolean listening) {
        return false;
    }

	/*
     * Registers the system setting associated with this sensor.
     * The feature must support [proximity] as a sensor type
     */
    public static boolean registerDozeObserver(ContentObserver observer) {
        return false;
    }

	/*
     * Updates the sensor and it's listener. This call is performed
     * in DozeSensors
     */
    public static boolean updateSensor() {
        return false;
    }
}