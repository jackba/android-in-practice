#!/bin/sh
jar cvfm bin/plugin.jar manifest.txt -C bin .
monkeyrunner -plugin bin/plugin.jar dealdroid_with_plugin.py