[![Build Status](https://travis-ci.org/oboehm/PatternTesting2.svg?branch=master)](https://travis-ci.org/oboehm/PatternTesting2)
# PatternTesting 2

This is the continuation of the [SourceForge PatternTesting project](https://sourceforge.net/projects/patterntesting/).
End of November 2017 the CVS support of SourceForge ends.
Version 2 of PatternTesting is not only a move from CVS to GIT but also a restart and restructuring of the project.


## Planning

Version 2 will be more focussed on the original vision:

> PatternTesting:
> A new type of automated testing that ensures that development patterns, best practices, architecture design are being correctly implemented.

The plans for version 2 are:

* foreign classes like ClasspathMonitor will be moved to a separate project
* deprecated methods and classes will be removed
* code will be prepared for Java 9 module system
* consolidation of the different document parts


## Branching Model

Development will follow [a successful Git branching model](http://nvie.com/posts/a-successful-git-branching-model/) von Vincent Driessen.
The actual development you can find on the [develop](https://github.com/oboehm/PatternTesting2/tree/develop) branch.

---
November 2017
