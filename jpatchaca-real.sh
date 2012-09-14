#!/bin/bash

SCRIPT=`readlink -f "$0"`
BASE=`dirname "$SCRIPT"`

case `uname -m` in
	'i686')
	ARCH=i586
	;;
	'x86_64')
	ARCH=x64
	;;
esac

if [ ! -d "$BASE/jre7" ]
then
	rm -f /tmp/jre7.tar.gz
	echo "Getting jre7"
	wget -c --no-cookies --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F" "http://download.oracle.com/otn-pub/java/jdk/7u7-b10/jre-7u7-linux-$ARCH.tar.gz" --output-document "/tmp/jre7.tar.gz"
	mkdir "$BASE/jre7"
	tar -xzf /tmp/jre7.tar.gz -C "$BASE/jre7" --strip-components 1
fi

export PATH="$BASE/jre7/bin":$PATH
export JAVA_HOME="$BASE/jre7"

java -splash:"$BASE/splash.png" -jar "$BASE/jpatchaca-real.jar"