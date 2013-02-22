Golo-plugin
===========

# About #
This plugin makes it possible to invoke [Golo](http://golo-lang.org/) build script as the main build step.

# Configuration #
To configure available Golo installation on your system, go to Jenkins configuration page, find section 'Golo' and fill the form as shown bellow.
![Golo_Configuration](./img/Golo_Configuration.png)

# Usage #

You can use the plugin to invoke either a Golo script or a inline Golo command.
![Golo_Script](./img/Golo_InvokeGoloScript.png)
![Golo_Command](./img/Golo_InvokeGoloCommand.png)
![Golo_Output](./img/Golo_Output.png)

# Changelog #

## Version 1.0.0 - (February 22, 2013) ##
* Initial release
   * Define a Golo installation
   * Invoke a Golo script
   * Invoke a Golo inline command
