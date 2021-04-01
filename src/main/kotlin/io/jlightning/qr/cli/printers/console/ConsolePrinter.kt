/**
 *     C-lightning plugin to create a small QR code when lightningd made somethings
 *     Copyright (C) 2020-2021 Vincenzo Palazzo vincenzopalazzodev@gmail.com
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package io.jlightning.qr.cli.printers.console

import io.jlightning.qr.cli.model.Options
import io.jlightning.qr.cli.plugin.PluginCommand
import io.jlightning.qr.cli.printers.IPrinter
import io.vincenzopalazzo.qr.QRCode
import jrpc.clightning.plugins.CLightningPlugin
import jrpc.service.converters.jsonwrapper.CLightningJsonObject

/**
 * @author https://github.com/vincenzopalazzo
 */
class ConsolePrinter : IPrinter {
    override fun print(plugin: CLightningPlugin, options: Options, response: CLightningJsonObject) {
        if (!options.nodeConf.toConsole)
            return
        when (options.command) {
            PluginCommand.NEW_ADDR -> {
                val qrCodeString = QRCode.getQrToString(options.pluginInfo.addressGenerated, 30, 30)
                response.apply {
                    response.add("address", qrCodeString)
                }
            }
            PluginCommand.PEER_URL -> {
                options.pluginInfo.listAddresses.forEach {
                    val qr = QRCode.getQrToString(it.address, 40, 40)
                    response.apply {
                        response.add(it.type, qr)
                    }
                }
            }
            PluginCommand.NEW_INVOICE -> {
                // TODO not involved because it is a listener method and not a rpc method.
                // this mean that we don't have the response object
            }
        }
    }
}
