#! /bin/sh
#
# This script deploys the artifacts to https://oss.sonatype.org/
# see https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide#SonatypeOSSMavenRepositoryUsageGuide-7b.StageExistingArtifacts
#
# (c)reated: 06-Jan-2012 by boehm@javatux.de
#

# set up some constants
URL=https://oss.sonatype.org/service/local/staging/deploy/maven2/
VERSION=2.2.0
options="gpg:sign-and-deploy-file -Durl=$URL -DrepositoryId=sonatype-nexus-staging"

# passphrase is needed for signing
echo "passphrase for GPG: "
stty_orig=`stty -g`
stty -echo
read passphrase
stty $stty_orig

options="gpg:sign-and-deploy-file -Durl=$URL -DrepositoryId=sonatype-nexus-staging -Dgpg.passphrase=$passphrase"

deploy_pom_for() {
    subdir=$1
	module=$2
	pushd $subdir
	echo deploying $module in $subdir...
	mvn -N $options -DpomFile=target/$module-$VERSION.pom -Dfile=target/$module-$VERSION.pom
    popd
    echo
}

deploy_jar_for() {
    subdir=$1
	module=$2
	pushd $subdir
	echo deploying $module in $subdir...
    mvn $options -DpomFile=target/$module-$VERSION.pom -Dfile=target/$module-$VERSION.jar
    mvn $options -DpomFile=target/$module-$VERSION.pom -Dfile=target/$module-$VERSION-sources.jar -Dclassifier=sources
    mvn $options -DpomFile=target/$module-$VERSION.pom -Dfile=target/$module-$VERSION-javadoc.jar -Dclassifier=javadoc
    popd
    echo
}

deploy_war_for() {
    subdir=$1
	module=$2
	pushd $subdir
	echo deploying $module in $subdir...
    mvn $options -DpomFile=target/$module-$VERSION.pom -Dfile=target/$module-$VERSION.war
    mvn $options -DpomFile=target/$module-$VERSION.pom -Dfile=target/$module-$VERSION-sources.jar -Dclassifier=sources
    mvn $options -DpomFile=target/$module-$VERSION.pom -Dfile=target/$module-$VERSION-javadoc.jar -Dclassifier=javadoc
    popd
    echo
}

# start deployment
deploy_pom_for . patterntesting-parent
deploy_jar_for patterntesting-rt patterntesting-rt
deploy_pom_for patterntesting-libs/patterntesting-check patterntesting-check
deploy_jar_for patterntesting-libs/patterntesting-check/patterntesting-check-ct patterntesting-check-ct
deploy_jar_for patterntesting-libs/patterntesting-check/patterntesting-check-rt patterntesting-check-rt
deploy_jar_for patterntesting-libs/patterntesting-concurrent patterntesting-concurrent
deploy_jar_for patterntesting-libs/patterntesting-exception patterntesting-exception
deploy_pom_for patterntesting-libs patterntesting-libs
deploy_jar_for patterntesting-tools patterntesting-tools
deploy_jar_for patterntesting-compat patterntesting-compat
deploy_jar_for patterntesting-samples patterntesting-samples
