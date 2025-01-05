[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.patterntesting/patterntesting-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.patterntesting/patterntesting-parent)

# PatternTesting 2

This is the continuation of the [SourceForge PatternTesting project](https://sourceforge.net/projects/patterntesting/).
End of November 2017 the CVS support of SourceForge ends.
Version 2 of PatternTesting is not only a move from CVS to GIT but also a restart and restructuring of the project.


## Roadmap

Version 2 was released on 05-Jan-2020.
It is more focussed on the original vision:

> PatternTesting:
> A new type of automated testing that ensures that development patterns, best practices, architecture design are being correctly implemented.

The difference to version 1 is:

* foreign classes like ClasspathMonitor are moved to a separate project ([ClazzFish](https://github.com/oboehm/ClazzFish)).
* deprecated methods and classes are removed
* support of JUnit 5

The runner for JUnit 4 and other outdated classes are moved to [patterntesting-compat](patterntesting-compat/README.adoc).


### Next Steps

* switch to Java 11 (will come with v2.2)
* continue the documentation in patterntesting-doc module



## Branching Model

Development will follow [a successful Git branching model](http://nvie.com/posts/a-successful-git-branching-model/) von Vincent Driessen.
The actual development you can find on the [develop](https://github.com/oboehm/PatternTesting2/tree/develop) branch.


## More Infos

* changelog: [CHANGELOG.md](CHANGELOG.md)
* documentation: [doc/README.adoc](doc/README.adoc)
* [PatternTesting Wiki](https://sourceforge.net/p/patterntesting/wiki/Home/)

> [!WARNING]
> The old homepage at www.patterntesting.org is now longer available!

---
January 2025,
Oli B.
