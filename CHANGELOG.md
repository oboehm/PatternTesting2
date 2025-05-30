# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [2.5.1] - 2025-04-16

### Added

* NetworkTester.isOnline(URI) added
  ([#51](https://github.com/oboehm/PatternTesting2/issues/51))
* NetworkTester.exists(URI) supports SSH and other protocols
  ([#52](https://github.com/oboehm/PatternTesting2/issues/52))


## [2.5.0] - 2025-01-06

### Changed

* AspectJ version updated to 1.9.22.1
* downgrade to Java 11, tested under Java 21
* dependency to commons-logging reduced (used only for testing in patterntesting-check-rt)
* dependencies updated

### Removed

* OSGi support


## [2.4.0] - 2024-01-29

### Changed

* AspectJ version updated to 1.9.21
* Java 17 required
* better output for @Broken annotation
  ([#43](https://github.com/oboehm/PatternTesting2/issues/43))
* AssertArg is now deprecated


## [2.3] - 2023-01-06

### Security

* update to xstream 1.4.20
  ([CVE-2022-41966](https://x-stream.github.io/CVE-2022-41966.html))

### Fixed

* tested under Java 17

### Added

* Localhost.matches(..) accepts now timout parameter
* NetworkTester.isOnline(..) accepts also timeout parameter

### Changed

* switched to SLF4J as logging framework


## [2.2.20-YEARS] - 2022-04-14

### Security

* update to xstream 1.4.19
  ([CVE-2021-43859](https://x-stream.github.io/CVE-2021-43859.html))

### Changed

* AspectJ version updated to 1.9.9.1
* Classloader of OpenJDK is supported
* ClassTester supports module concept of Java 9+

### Deleted

* NamingConventionsAspect was deleted because of conflicts with Java 11


## [2.2.0] - 2022-01-11

### Security

* update to Log4J 2.17.1
  ([CVE-2021-44832](https://github.com/advisories/GHSA-8489-44mv-ggj8))

### Changed

* switch to Java 11 as development and build environment


## [2.1] - 2021-12-14

### Security

* **v2.1.2**: update to Log4J 2.17.0 to fix next Log4J vulnerability
* **v2.1.1**: update to Log4J 2.16.0 to fix Log4J vulnerability
  [CVE-2021-45046](hhttps://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2021-45046)_
* update to Log4J 2.15.0 to fix Log4J vulnerability
  [CVE-2021-44228](https://nvd.nist.gov/vuln/detail/CVE-2021-44228)

### Removed

* ClasspathMonitor and ResourcepathMonitor is deprecated and moved to patterntesting-compat


## [2.0] - 2020-02-05

### Added

* **v2.0.2**: NetworkTester provides now for each assert method also a boolean method

### Changed

* **v2.0.1**: dependencies updated
* move from [SoureForge](https://sourceforge.net/projects/patterntesting/) (CVS) to [GitHub](https://github.com/oboehm/PatternTesting2) (GIT)
* ObjectTester and other XxxTester classes will no longer try to instantiate private classes
  (patterntesting-rt)



## [1.8] - 2017-11-11
 
### Changed
 
* last version with CVS
* depends now on Java 8

### Fixed

* bug [#38](http://sourceforge.net/p/patterntesting/bugs/38/)
* bug [#39](http://sourceforge.net/p/patterntesting/bugs/39/)


## [1.7 and earlier]

* see http://www.patterntesting.com/release/index.html
