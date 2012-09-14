#!/bin/bash

mkdir target/jpatchaca-real
cp target/jpatchacareal-0.0.1-SNAPSHOT-jar-with-dependencies.jar target/jpatchaca-real/jpatchaca-real.jar
cp jpatchaca-real.sh \
	jpatchaca-real-setup.sh \
	splash.png \
	icon.png \
	target/jpatchaca-real/

~/makeself-2.1.5/makeself.sh \
	--notemp \
	target/jpatchaca-real \
	target/jpatchaca-real-setup.analfa-test.sh \
	"JPatchaca Real Setup - Analfa Test" \
	./jpatchaca-real-setup.sh