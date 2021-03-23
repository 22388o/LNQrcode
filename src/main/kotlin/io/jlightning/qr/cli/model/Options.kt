package io.jlightning.qr.cli.model

import jrpc.clightning.model.types.NetworkAddress

class Options {

    val nodeConf = CLightningConf()
    val pluginInfo = PluginInfo()

    class CLightningConf {
        var toConsole: Boolean = false
        var toDesktopGUI: Boolean = false
        var toWebUI: Boolean = false
    }

    class PluginInfo {
        var listAddresses = ArrayList<NetworkAddress>()
    }
}
