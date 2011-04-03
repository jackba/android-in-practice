package com.manning.aip.monkeyrunner;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyDevice.TouchPressType;

public class MonkeyHelper {

    public static void tap(MonkeyDevice device, int x, int y) {
        device.touch(x, y, TouchPressType.DOWN_AND_UP);
        sleep(2000);
    }

    public static void press(MonkeyDevice device, String key) {
        String keyCode = "KEYCODE_" + key.toUpperCase();
        device.press(keyCode, TouchPressType.DOWN_AND_UP);
        sleep(2000);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
