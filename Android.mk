# Copyright (C) 2018 CypherOS
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

ifneq ($(filter huawei motorola xiaomi, $(TARGET_BOARD_AOSCP_HARDWARE)),)
LOCAL_PATH := $(call my-dir)

HARDWARE_PATH := internal
BIOMECTRICS_PATH := src/co/aoscp/hardware/biomectrics

include $(CLEAR_VARS)
LOCAL_AIDL_INCLUDES := $(LOCAL_PATH)/$(HARDWARE_PATH)
LOCAL_SRC_FILES := $(call all-java-files-under, $(HARDWARE_PATH)) \
    $(call all-Iaidl-files-under, $(HARDWARE_PATH)) \
    $(call all-logtags-files-under, $(HARDWARE_PATH))
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE := hardware-aoscp
include $(BUILD_JAVA_LIBRARY)

ifneq ($(TARGET_AOSCP_BIOMECTRICS),)
include $(CLEAR_VARS)
LOCAL_MODULE := biomectrics-ext
LOCAL_JAVA_LIBRARIES := hardware-aoscp
LOCAL_SRC_FILES := $(call all-java-files-under,$(BIOMECTRICS_PATH))
LOCAL_MODULE_TAGS := optional
LOCAL_DEX_PREOPT := false
include $(BUILD_JAVA_LIBRARY)
endif
endif

include $(call all-makefiles-under,$(LOCAL_PATH))