#!/bin/bash

function generateGenericPom() {
    local groupId=$1
    local artifactId=$2
    local version=$3
    local name=$4
    local description=$5
    local url=$6
    local license_name=$7
    local license_url=$8
    local developer_name=$9
    local developer_mail=${10}
    local organization_name=${11}
    local organization_url=${12}
    local scm_conn=${13}
    local output=${14}

    config="<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\"> \
    <modelVersion>4.0.0</modelVersion> \
    <groupId>${groupId}</groupId> \
    <artifactId>${artifactId}</artifactId> \
    <version>${version}</version> \
    <name>${name}</name> \
    <packaging>jar</packaging> \
    <description>${description}</description> \
    <url>${url}</url> \
    <licenses>  \
      <license> \
          <name>${license_name}</name> \
          <url>${license_url}</url> \
      </license> \
    </licenses> \
    <developers> \
      <developer> \
        <name>${developer_name}</name> \
        <email>${developer_mail}</email> \
        <organization>${organization_name}</organization> \
        <organizationUrl>${organization_url}</organizationUrl> \
      </developer> \
    </developers> \
    <scm> \
      <connection>${scm_conn}</connection> \
      <developerConnection>${scm_conn}</developerConnection> \
      <url>${url}</url> \
    </scm> \
</project> \
"
    echo $config > ${output}
}

function publishBuild() {
    local artifactId=$1
    local artifact=$2
    local version=$3
    local javadoc_jar=$4
    local sources_jar=$5
    local pomFile=$6

    ${maven_bin} gpg:sign-and-deploy-file -s ${settings} -Durl=${sonatype_url} \
                                                         -DartifactId=${artifactId} \
                                                         -DrepositoryId=${repository} \
                                                         -Dversion=${version} \
                                                         -DpomFile=${pomFile} \
                                                         -Dgpg.passphrase=${passphrase} \
                                                         -Dfile=${artifact} \
                                                         -Djavadoc=${javadoc_jar} \
                                                         -Dsources=${sources_jar} 

    return $?
}