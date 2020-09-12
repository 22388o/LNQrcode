# LNQrcode 

It is a plugin for c-lightning to display a QR code on a View!

## Install
(!! *FOR THE MOMENT* Compatible only with JDK13 because the library [JRPCLightning]() is under developing and is a static jar inside devlib directory!!)

Java produces a jar and c-lightning needs a bash script to run it! 
The plugin compiles the plugin and makes the script with the command `./gradlew createRunnableScript`

After the gradual process, you have the jar inside the `build/libs/qr-cli-all.jar` and the script `qr-cli-gen.sh` 
in the root directory of the project.

### How bash script look like

The contains only the command to run the jar with java, in my cases the script contains the following result>

```bash
#!/bin/bash
/usr/lib/jvm/jdk-13.0.2/bin/java -jar /home/vincent/Github/qr-cli/build/libs/qr-cli-all.jar
```

In this case, you can move this bash script inside your `./lightning/plugins` the directory or you can add the plugin to the file conf
with `plugin=/PATH/bash/file` or use the command line `--plugin=/path/bash/file`.

## How to contribute

All contributions are welcome, feel free to open a PR or an issue to discuss any improvement or bugs.
In addition, this repository needs a maintainer, if you like to contribute on open source software feel free to write 
an email to [vincenzopalazzodev@gmail.com](mailito:vincenzopalazzodev@gmail.com) 

## Built with

- [JQRInterface](https://gitlab.com/vincenzopalazzo/jconsole-qr)
- [JRPClightning](https://github.com/vincenzopalazzo/JRPClightning)

## License

<div align="center">
  <img src="https://opensource.org/files/osi_keyhole_300X300_90ppi_0.png" width="150" height="150"/>
</div>

 It is a plugin for c-lightning to display a QR code on a View!

 Copyright (C) 2020 Vincenzo Palazzo vincenzopalazzodev@gmail.com
 
 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License along
 with this program; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
