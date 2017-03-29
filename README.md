# WorldGrower

WorldGrower is a 2D turn-based rpg simulator focused on creating a detailed world, in which actions have consequences.
It focuses on skills, dialogue, economy and society.

## Table of contents
- [Installation](#installation)
- [Features](#features)
- [Bugs and Feature Requests](#bugs-and-feature-requests)
- [Documentation](#documentation)
- [License](#license)

## Installation<a name="installation"></a>

###Binary installation using installer

*   [Windows 32-bit](https://github.com/WorldGrower/WorldGrower/releases/download/1.3.0/WorldGrower_windows_1_3_0_32bit.exe)
*   [Windows 64-bit](https://github.com/WorldGrower/WorldGrower/releases/download/1.3.0/WorldGrower_windows-x64_1_3_0.exe)
*   [MacOS X](https://github.com/WorldGrower/WorldGrower/releases/download/1.3.0/WorldGrower_macos_1_3_0.dmg)
*   [Linux DEB 32-Bit (Debian, Ubuntu, Mint, etc)](https://github.com/WorldGrower/WorldGrower/releases/download/1.3.0/WorldGrower_linux_1_3_0_32bit.deb)
*   [Linux DEB 64-Bit (Debian, Ubuntu, Mint, etc)](https://github.com/WorldGrower/WorldGrower/releases/download/1.3.0/WorldGrower_linux_1_3_0.deb)
*   [Linux RPM 32-Bit (Fedora, Mandriva, openSUSE, Red Hat, etc)](https://github.com/WorldGrower/WorldGrower/releases/download/1.3.0/WorldGrower_linux_1_3_0_32bit.rpm)
*   [Linux RPM 64-Bit (Fedora, Mandriva, openSUSE, Red Hat, etc)](https://github.com/WorldGrower/WorldGrower/releases/download/1.3.0/WorldGrower_linux_1_3_0.rpm)
*   [Linux 32-Bit (Other Linux distributions)](https://github.com/WorldGrower/WorldGrower/releases/download/1.3.0/WorldGrower_unix_1_3_0_32bit.sh)
*   [Linux 64-Bit (Other Linux distributions)](https://github.com/WorldGrower/WorldGrower/releases/download/1.3.0/WorldGrower_unix_1_3_0_64bit.sh)

*These installers contain the game and will install the game when executed.*

###Binary installation of .zip file

*   [Download .zip file](https://github.com/WorldGrower/WorldGrower/releases/download/1.3.0/worldgrower-1.3.0.zip)
*   Extract the .zip file on the local filesystem
*   Make sure [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) is installed.
	If you're not sure, you can check your java version [here](https://www.java.com/verify).
*	The easiest way to start WorldGrower is to run the .jar file by double-clicking it or to open it with java 8.

###Common problems with binary installation

*	*'java' is not recognized as an internal or external command, operable program or batch file.*<br/> This means that no java version was installed.<br/> Make sure to download java from [here](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html).

*	*Exception in thread "main" java.lang.UnsupportedClassVersionError: org/worldgrower/gui/start/StartScreen : Unsupported major.minor version 52.0*<br/> This means an older java version is installed.<br/> It's best to can check your java version [here](https://www.java.com/verify).

###Source installation

*   If you are installing from source code, it's recommended that you clone the master branch.
*   Make sure Java 8 is installed
*   Run org.worldgrower.gui.start.StartScreen with resources directory in the classpath.

## Features<a name="features"></a>
- dynamic world that is built up from scratch
- play several roles like farmer, lumberjack, priest, etc
- extensive dialogue system
- mislead enemies with disguises and illusions
- use character skills to build up an economy from the ground up 
- found organizations and use their influence to further your own goals
- spread gossip and print newspapers

## Bugs and Feature Requests<a name="bugs-and-feature-requests"></a>

Have a bug or a feature request?<br/>Please search for existing and closed issues
on the [issue list](https://github.com/WorldGrower/WorldGrower/issues).


## Documentation<a name="documentation"></a>

WorldGrower's documentation, included in this repo in the gh-pages branch.

[User Manual](http://worldgrower.github.io/WorldGrower/Overview.htm)

[Developer Guide](http://worldgrower.github.io/WorldGrower/DeveloperGuide.htm)

[Vision](http://worldgrower.github.io/WorldGrower/Vision.html)

[Influences](http://worldgrower.github.io/WorldGrower/Influences.html)

[Gameplay video](https://www.youtube.com/watch?v=pOd-cMtahvM&feature=youtu.be)

[WorldGrower on Travis](https://travis-ci.org/WorldGrower/WorldGrower): [![Build Status](https://travis-ci.org/WorldGrower/WorldGrower.svg?branch=master)]

## License<a name="license"></a>

WorldGrower is under GNU General Public License, see LICENSE for more information.

<a href="https://twitter.com/WorldGrower" class="twitter-follow-button" data-show-count="false">Follow @WorldGrower</a>
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>

## Installer

installer created by [Install4J multi-platform installer builder](http://www.ej-technologies.com/products/install4j/overview.html)