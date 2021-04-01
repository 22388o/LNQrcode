package io.jlightning.qr.cli.printers

import io.jlightning.qr.cli.model.Options
import jrpc.clightning.plugins.CLightningPlugin
import jrpc.service.converters.jsonwrapper.CLightningJsonObject

/**
 * @author https://github.com/vincenzopalazzo
 */
interface IPrinter {
    fun print(plugin: CLightningPlugin, options: Options, response: CLightningJsonObject)
}
