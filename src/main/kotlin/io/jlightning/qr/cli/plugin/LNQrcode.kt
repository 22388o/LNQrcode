/**
 *     C-lightning plugin to create a small QR code when lightningd made somethings
 *     Copyright (C) 2020 Vincenzo Palazzo vincenzopalazzodev@gmail.com
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
package io.jlightning.qr.cli.plugin

import io.jlightning.qr.cli.model.Options
import io.jlightning.qr.cli.printers.PrinterChain
import io.jlightning.qr.cli.ui.AfterRunUIAction
import io.jlightning.qr.cli.ui.QRCliUI
import jrpc.clightning.CLightningRPC
import jrpc.clightning.annotation.PluginOption
import jrpc.clightning.annotation.RPCMethod
import jrpc.clightning.annotation.Subscription
import jrpc.clightning.exceptions.CLightningException
import jrpc.clightning.model.types.AddressType
import jrpc.clightning.model.types.NetworkAddress
import jrpc.clightning.plugins.CLightningPlugin
import jrpc.clightning.plugins.log.PluginLog
import jrpc.service.converters.jsonwrapper.CLightningJsonObject
import java.lang.Exception
import javax.swing.SwingUtilities

/**
 * @author https://github.com/vincenzopalazzo
 */
class LNQrcode : CLightningPlugin() {

    @PluginOption(
        name = "onconsole",
        description = "Enable to print a QR core on the console under some cool format",
        defValue = "false",
        typeValue = "flag"
    )
    private var onConsole: Boolean = false

    @PluginOption(
        name = "ongui",
        description = "Enable to display a QR core on the Desktop GUI",
        typeValue = "flag"
    )
    private var onGui: Boolean = false

    @PluginOption(
        name = "onweb",
        description = "Enable to dysplay a QR core on web page",
        typeValue = "flag"
    )
    private var onWeb: Boolean = false

    private var pluginInit: Boolean = false
    private lateinit var options: Options

    @RPCMethod(name = "peer-url", description = "generate a per URL with invoice")
    fun peerUrl(plugin: CLightningPlugin, request: CLightningJsonObject, response: CLightningJsonObject) {
        plugin.log(PluginLog.DEBUG, "RPC method qrInvoice")
        plugin.log(PluginLog.DEBUG, request)
        this.initPlugin(plugin)
        try {
            val getInfo = CLightningRPC.getInstance().info
            options.pluginInfo.listAddresses = getInfo.address as ArrayList<NetworkAddress>
            PrinterChain.print(plugin, options, response)
        } catch (e: Exception) {
            plugin.log(PluginLog.ERROR, e.localizedMessage)
            response.apply {
                add("error", e.localizedMessage)
            }
        }
    }

    @RPCMethod(
        name = "newaddress",
        description = "generate a new address and display it on QR code",
        parameter = "[type]"
    )
    fun generateAddress(plugin: CLightningPlugin, request: CLightningJsonObject, response: CLightningJsonObject) {
        plugin.log(PluginLog.DEBUG, "newaddress")
        plugin.log(PluginLog.DEBUG, request)
        this.initPlugin(plugin)
        val listParamter = request["params"].asJsonArray.toList()
        var type = ""
        if (listParamter.isNotEmpty()) type = listParamter[0].asString
        val newAddr: String
        if (type.isNotEmpty()) {
            if (type == AddressType.BECH32.value) {
                newAddr = CLightningRPC.getInstance().newAddress(AddressType.BECH32)
            } else {
                newAddr = CLightningRPC.getInstance().newAddress(AddressType.P2SH_SEGWIT)
            }
        } else {
            newAddr = CLightningRPC.getInstance().newAddress(AddressType.BECH32)
        }
        log(PluginLog.DEBUG, "New address $newAddr")
        QRCliUI.instance.title = "${type.toUpperCase()} address"
        // QRCliUI.instance.qrContent = newAddr
        // SwingUtilities.invokeLater { QRCliUI.instance.initApp() }
        response.apply {
            add("address", newAddr)
        }
    }

    @Subscription(notification = "invoice_creation")
    fun invoiceCrated(data: CLightningJsonObject) {
        this.initPlugin(this)
        log(PluginLog.DEBUG, data)
        val invoiceCreation = data.get("params").asJsonObject
            .get("invoice_creation").asJsonObject
        log(PluginLog.DEBUG, invoiceCreation)
        val label = invoiceCreation.get("label").asString
        log(PluginLog.DEBUG, label)
        try {
            QRCliUI.instance.title = "Invoice with label $label"
            QRCliUI.instance.addEventAfterInitApp(object : AfterRunUIAction() {
                override fun run() {
                    val invoice = CLightningRPC.getInstance().listInvoices(label)
                    // QRCliUI.instance.qrContent = invoice.listInvoice[0].bolt11
                }
            })
            SwingUtilities.invokeLater {
                // QRCliUI.instance.initApp()
            }
        } catch (ex: CLightningException) {
            ex.printStackTrace()
            log(PluginLog.WARNING, ex.localizedMessage)
        }
    }

    private fun initPlugin(plugin: CLightningPlugin) {
        if (this.pluginInit)
            return
        options = Options()
        options.nodeConf.toConsole = onConsole
        options.nodeConf.toDesktopGUI = onGui
        options.nodeConf.toWebUI = onWeb
        plugin.log(PluginLog.DEBUG, "On Console %b".format(onConsole))
        plugin.log(PluginLog.DEBUG, "On GUI %b".format(onGui))
        plugin.log(PluginLog.DEBUG, "On Web %b".format(onWeb))
        // TODO init proxy if necessary
        this.pluginInit = true
    }
}
