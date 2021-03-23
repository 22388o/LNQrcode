package io.jlightning.qr.cli.printers

import io.jlightning.qr.cli.model.Options
import io.jlightning.qr.cli.printers.gui.GUIPrinter
import jrpc.clightning.plugins.CLightningPlugin
import jrpc.service.converters.jsonwrapper.CLightningJsonObject

object PrinterChain {
    private var printers = ArrayList<IPrinter>()

    init {
        printers.addAll(
            listOf(
                GUIPrinter()
            )
        )
    }

    fun print(plugin: CLightningPlugin, options: Options, response: CLightningJsonObject) {
        for (printer in printers)
            printer.print(plugin, options, response)
    }
}
