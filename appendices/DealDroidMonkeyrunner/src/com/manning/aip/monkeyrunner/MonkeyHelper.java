package com.manning.aip.monkeyrunner;

import com.android.monkeyrunner.MonkeyDevice;
import com.android.monkeyrunner.MonkeyDevice.TouchPressType;

import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;

public class MonkeyHelper {

    public static void tap(MonkeyDevice device, int x, int y) {
        PyObject[] args = { new PyInteger(x), new PyInteger(y),
                new PyString(TouchPressType.DOWN_AND_UP.name()) };
        device.touch(args, null);
        sleep(2000);
    }

    public static void press(MonkeyDevice device, String key) {
        String keyCode = "KEYCODE_" + key.toUpperCase();
        PyObject[] args = { new PyString(keyCode), new PyString(TouchPressType.DOWN_AND_UP.name()),
                new PyString("") };
        device.press(args, null);
        sleep(2000);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
