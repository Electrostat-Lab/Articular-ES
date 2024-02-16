#!/bin/sh

# Maven sonatype stuff
# ---------------------
sonatype_url="https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
repository="ossrh"
groupId="io.github.software-hardware-codesign"
maven_version="3.9.6"
maven_bin="./apache-maven-$maven_version/bin/mvn"

lib_pomFile="./helper-scripts/project-impl/publishing/articular.pom"

passphrase="avrsandbox"

lib_artifactId_release="articular-es"

settings="./helper-scripts/project-impl/publishing/maven-settings.xml"
lib_build_dir="./articular-es/build/libs"
# ---------------------
