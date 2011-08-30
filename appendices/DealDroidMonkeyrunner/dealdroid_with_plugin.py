from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
from com.manning.aip.monkeyrunner import MonkeyHelper
import commands
import sys

print hello

devices = commands.getoutput('adb devices').strip().split('\n')[1:]
if len(devices) == 0:
  MonkeyRunner.alert("No devices found. Start an emulator or connect a device.", "No devices found", "Exit")
  sys.exit(1) 
elif len(devices) == 1:
  choice = 0
else:
  choice = MonkeyRunner.choice("More than one device found. Please select target device.", devices, "Select target device")

device_id = devices[choice].split('\t')[0]

device = MonkeyRunner.waitForConnection(5, device_id)

apk_path = device.shell('pm path com.manning.aip.dealdroid')
if apk_path.startswith('package:'):
    print "DealDroid already installed."
else:
    print "DealDroid not installed, installing APKs..."
    device.installPackage('../DealDroid/bin/DealDroid.apk')

print "Starting DealDroid..."
device.startActivity(component='com.manning.aip.dealdroid/.DealList')
MonkeyRunner.sleep(7)

MonkeyHelper.tap(device, 100, 450)
MonkeyHelper.tap(device, 100, 250)
MonkeyHelper.tap(device, 100, 150)
MonkeyHelper.press(device, 'menu')
MonkeyHelper.tap(device, 280, 450)
device.type("555-13456")
MonkeyHelper.press(device, 'back')
MonkeyHelper.press(device, 'back')
MonkeyHelper.press(device, 'back')
