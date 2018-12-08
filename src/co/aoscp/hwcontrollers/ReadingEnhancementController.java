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

import android.util.Log;

/*
 * Reading Enhancement API
 *
 * Reading enhancement is support in aoscp but,
 * some devices have their own native implementations.
 * This API switches to the device native implementation
 * when reading enhancement is toggled.
 */

public class ReadingEnhancementController {

    /*
     * All HAF classes should export this boolean.
     * Real implementations must, of course, return true
     */
    public static boolean isSupported() {
        return false;
    }

    /*
     * Enable/Disable reading mode, which tints the display grayscale
     * We check that status with [boolean: state]
     */
    public static boolean setEnabled(boolean state) {
        return false;
    }
}