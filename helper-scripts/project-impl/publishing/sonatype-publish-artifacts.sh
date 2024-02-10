#!/bin/bash

source "./helper-scripts/abstract/abstract-sonatype-publish.sh"
source "./helper-scripts/project-impl/variables.sh"

# obtain dependencies in the form 'groupId:artifact:version'
version=${1}

lib_artifact="${lib_build_dir}/${lib_artifactId_release}-${version}.jar"
lib_sources_jar="${lib_build_dir}/${lib_artifactId_release}-${version}-sources.jar"
lib_javadoc_jar="${lib_build_dir}/${lib_artifactId_release}-${version}-javadoc.jar"

ext_artifact="${ext_build_dir}/${ext_artifactId_release}-${version}.jar"
ext_sources_jar="${ext_build_dir}/${ext_artifactId_release}-${version}-sources.jar"
ext_javadoc_jar="${ext_build_dir}/${ext_artifactId_release}-${version}-javadoc.jar"

generateGenericPom "${groupId}" \
                   "${lib_artifactId_release}" \
                   "${version}" \
                   "The Articular-ES Framework" \
                   "An entity component system (ECS) framework featuring strong articulations among components from different systems through controller interfaces." \
                   "https://github.com/Software-Hardware-Codesign/Articular-ES" \
                   "The AvrSandbox Project, Articular-ES Framework, BSD-3 Clause License" \
                   "https://github.com/Software-Hardware-Codesign/Articular-ES/blob/master/LICENSE" \
                   "Pavly Gerges (aka. pavl_g)" \
                   "pepogerges33@gmail.com" \
                   "The AvrSandbox" \
                   "https://github.com/Software-Hardware-Codesign" \
                   "scm:git:git://github.com/Software-Hardware-Codesign/Articular-ES.git" \
                   "${lib_pomFile}"

# publish 'android' and 'desktop' builds to maven sonatype
publishBuild "${lib_artifactId_release}" "${lib_artifact}" "${version}" "${lib_javadoc_jar}" "${lib_sources_jar}" "${lib_pomFile}"