[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.patterntesting/patterntesting-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.patterntesting/patterntesting-parent)

# PatternTesting 2

This is the continuation of the [SourceForge PatternTesting project](https://sourceforge.net/projects/patterntesting/).
End of November 2017 the CVS support of SourceForge ends.
Version 2 of PatternTesting is not only a move from CVS to GIT but also a restart and restructuring of the project.


## Planning

Version 2 will be more focussed on the original vision:

> PatternTesting:
> A new type of automated testing that ensures that development patterns, best practices, architecture design are being correctly implemented.

The plans for version 2 are:

* foreign classes like ClasspathMonitor are moved to a separate project [ClassFish](https://github.com/oboehm/ClazzFish)
* deprecated methods and classes will be removed
* move to JUnit 5
* switch to Java 11 (not yet done)
* consolidation of the different document parts (not yet done)

The runner for JUnit 4 and other outdated classes are moved to [patterntesting-compat](patterntesting-compat/README.adoc).


## Branching Model

Development will follow [a successful Git branching model](http://nvie.com/posts/a-successful-git-branching-model/) von Vincent Driessen.
The actual development you can find on the [develop](https://github.com/oboehm/PatternTesting2/tree/develop) branch.


## More Infos

* Release Notes: [doc/release-notes](doc/release-notes.adoc)

---
Januar 2020,
Oli B.
