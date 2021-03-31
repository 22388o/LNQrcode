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
package io.jlightning.qr.cli.ui

import io.jlightning.qr.cli.model.Options
import io.jlightning.qr.cli.plugin.PluginCommand
import io.vincenzopalazzo.qr.QRCode
import org.material.component.swingsnackbar.SnackBar
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JTextArea
import javax.swing.GroupLayout
import javax.swing.JPanel

/**
 * @author https://github.com/vincenzopalazzo
 */
class QRUIContainer(private val frame: JFrame, private val options: Options) : JPanel() {

    private lateinit var label: JLabel
    private lateinit var textContent: JTextArea
    private lateinit var qrImage: ImageIcon
    private lateinit var qrImageContainer: JLabel
    private lateinit var snackbar: SnackBar

    init {
        initView()
    }

    private fun initView() {
        val content: String
        when (options.command) {
            PluginCommand.NEW_INVOICE -> {
                content = options.pluginInfo.invoice
            }
            PluginCommand.NEW_ADDR -> {
                content = options.pluginInfo.invoice
            }
            PluginCommand.PEER_URL -> {
                throw IllegalStateException("Command PEER_URL inside the GUI view for the single content")
            }
        }
        label = JLabel()
        textContent = JTextArea(content)
        val imageQr = QRCode.getQrToImage(content, 300, 300)
        qrImage = ImageIcon(imageQr)
        qrImageContainer = JLabel(qrImage)
        initLayout()
        initActions()
        isVisible = true
    }

    private fun initLayout() {
        val groupLayout = GroupLayout(this)

        groupLayout.setHorizontalGroup(
            groupLayout.createSequentialGroup()
                .addGap(50)
                .addGroup(
                    groupLayout.createParallelGroup()
                        // .addComponent(url)
                        .addComponent(textContent)
                        .addComponent(qrImageContainer)
                        .addGap(40)
                        .addComponent(label)
                        .addComponent(label)
                )
                .addGap(50)
        )

        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addGap(40)
                // .addComponent(url)
                .addComponent(textContent)
                .addGap(20)
                .addComponent(qrImageContainer)
                .addGap(30)
                .addComponent(label)
                .addGap(30)
        )

        layout = groupLayout
    }

    private fun initActions() {}
}
