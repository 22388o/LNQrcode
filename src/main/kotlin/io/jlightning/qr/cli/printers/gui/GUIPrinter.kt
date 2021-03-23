package io.jlightning.qr.cli.printers.gui

import io.jlightning.qr.cli.model.Options
import io.jlightning.qr.cli.printers.IPrinter
import io.jlightning.qr.cli.ui.QRCliUI
import jrpc.clightning.plugins.CLightningPlugin
import jrpc.service.converters.jsonwrapper.CLightningJsonObject
import javax.swing.SwingUtilities

class GUIPrinter : IPrinter {
    override fun print(plugin: CLightningPlugin, options: Options, response: CLightningJsonObject) {
        if (options.nodeConf.toDesktopGUI) {
            QRCliUI.instance.title = "Node URL"
            SwingUtilities.invokeLater { QRCliUI.instance.initApp(options) }
        }
    }
}
