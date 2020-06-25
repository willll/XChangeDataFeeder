#!/bin/bash

mvn clean compile

mvn exec:java -Dexec.mainClass=Main -Dexec.args="-configfile configs/binance.cfg"