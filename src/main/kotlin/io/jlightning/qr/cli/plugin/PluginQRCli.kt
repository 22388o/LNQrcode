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

import io.jlightning.qr.cli.ui.AfterRunUIAction
import io.jlightning.qr.cli.ui.QRCliUI
import jrpc.clightning.CLightningRPC
import jrpc.clightning.annotation.RPCMethod
import jrpc.clightning.annotation.Subscription
import jrpc.clightning.exceptions.CLightningException
import jrpc.clightning.model.types.AddressType
import jrpc.clightning.plugins.CLightningPlugin
import jrpc.clightning.plugins.log.CLightningLevelLog
import jrpc.service.converters.jsonwrapper.CLightningJsonObject
import java.lang.Exception
import javax.swing.SwingUtilities

/**
 * @author https://github.com/vincenzopalazzo
 */
class PluginQRCli: CLightningPlugin() {


    @RPCMethod(name = "peer-url", description = "generate a per URL with invoice")
    fun peerUrl(plugin: CLightningPlugin, request: CLightningJsonObject, response: CLightningJsonObject){
        plugin.log(CLightningLevelLog.DEBUG, "RPC method qrInvoice")
        plugin.log(CLightningLevelLog.DEBUG, request)
        try{
            val getInfo = CLightningRPC.getInstance().info
            val url = "%s@%s:%s".format(getInfo.id, getInfo.binding[0].address, getInfo.binding[0].port)
            QRCliUI.instance.qrContent = url
            QRCliUI.instance.title = "Node URL"
            SwingUtilities.invokeLater{ QRCliUI.instance.initApp() }
            response.apply {
                add("node_url", url)
            }
        }catch (e: Exception){
            e.printStackTrace()
            response.apply {
                add("error", e.localizedMessage)
            }
        }
    }

    @RPCMethod(name = "newaddress", description = "generate a new address and display it on QR code", parameter = "[type]")
    fun generateAddress(plugin: CLightningPlugin, request: CLightningJsonObject, response: CLightningJsonObject){
        plugin.log(CLightningLevelLog.DEBUG, "newaddress")
        val type = request["params"].asJsonObject["type"].asString
        val newAddr: String
        if(type.isNotEmpty()){
            if(type == AddressType.BECH32.name){
                newAddr = CLightningRPC.getInstance().getNewAddress(AddressType.BECH32)
            }else{
                newAddr = CLightningRPC.getInstance().getNewAddress(AddressType.P2SH_SEGWIT)
            }
        }else{
            newAddr = CLightningRPC.getInstance().getNewAddress(AddressType.BECH32)
        }
        QRCliUI.instance.title = "New address"
        QRCliUI.instance.qrContent = newAddr
        SwingUtilities.invokeLater { QRCliUI.instance.initApp() }
    }

    @Subscription(notification = "invoice_creation")
    fun invoiceCrated(data: CLightningJsonObject) {
        log(CLightningLevelLog.WARNING, data)
        val invoiceCreation = data.get("params").asJsonObject
                                .get("invoice_creation").asJsonObject
        log(CLightningLevelLog.WARNING, invoiceCreation)
        val label = invoiceCreation.get("label").asString
        log(CLightningLevelLog.WARNING, label)
        try{
            QRCliUI.instance.title = "Invoice with label $label"
            QRCliUI.instance.addEventAfterInitApp(object : AfterRunUIAction(){
                override fun run() {
                    val invoice = CLightningRPC.getInstance().listInvoices(label)
                    QRCliUI.instance.qrContent = invoice.listInvoice[0].bolt11
                }
            })
            SwingUtilities.invokeLater {
                QRCliUI.instance.initApp()
            }
        }catch (ex: CLightningException){
            ex.printStackTrace()
            log(CLightningLevelLog.WARNING, ex.localizedMessage)
        }
    }
}