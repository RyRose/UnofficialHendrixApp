#!/bin/bash
diff_staff=$(diff /var/www/html/staff.xml out/staff.xml);
version=$(cat /var/www/html/staff_version.txt);
if [ -n "$diff_staff" ]; then
    echo $(($version + 1)) > /var/www/html/staff_version.txt
    cp /var/www/html/staff.xml out/staff"$version".xml
    cp out/staff.xml /var/www/html/staff.xml;
fi
