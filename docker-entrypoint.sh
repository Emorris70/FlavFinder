#!/bin/bash
set -e
# Railway sets PORT dynamically; wire it into Tomcat's connector
PORT=${PORT:-8080}
sed -i "s/port=\"8080\"/port=\"$PORT\"/" /usr/local/tomcat/conf/server.xml
exec "$@"
