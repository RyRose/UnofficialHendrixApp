#!/bin/bash
cat email/alert.txt out/script.log > temp4325435.txt;
/usr/lib/sendmail RosadeApps@gmail.com < temp4325435.txt;
rm temp4325435.txt;
