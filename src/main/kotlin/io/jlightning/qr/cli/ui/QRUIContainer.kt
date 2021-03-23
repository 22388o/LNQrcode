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
import io.vincenzopalazzo.qr.QRCode
import mdlaf.utils.MaterialColors
import mdlaf.utils.MaterialImageFactory
import mdlaf.utils.icons.MaterialIconFont
import org.material.component.linklabelui.model.LinkLabel
import org.material.component.swingsnackbar.SnackBar
import org.material.component.swingsnackbar.action.AbstractSnackBarAction
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.MouseEvent
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JTextArea
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.Icon

/**
 * @author https://github.com/vincenzopalazzo
 */
class QRUIContainer(private val frame: JFrame, private val pluginInfo: Options.PluginInfo) : JPanel() {

    private lateinit var label: JLabel
    private lateinit var url: LinkLabel
    private lateinit var textContent: JTextArea
    private lateinit var qrImage: ImageIcon
    private lateinit var qrImageContainer: JLabel
    private lateinit var snackbar: SnackBar

    init {
        initView()
    }

    private fun initView() {
        label = JLabel()
        val addressChoose = pluginInfo.listAddresses[0].address
        url = LinkLabel(
            addressChoose, addressChoose,
            MaterialImageFactory.getInstance().getImage(
                MaterialIconFont.CONTENT_COPY,
                25,
                MaterialColors.COSMO_DARK_GRAY
            )
        )
        textContent = JTextArea(addressChoose)
        url.addMouseListener(object : AbstractSnackBarAction() {
            override fun mousePressed(e: MouseEvent?) {
                // TODO Introduce a check of type of content and build an URI by type
                val stringSelection = StringSelection("%s:%s".format("bitcoin", addressChoose))
                val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
                clipboard.setContents(stringSelection, null)
                val icon: Icon = MaterialImageFactory.getInstance().getImage(
                    MaterialIconFont.CLOSE,
                    25,
                    MaterialColors.COSMO_DARK_GRAY
                )

                snackbar = SnackBar.make(frame, "Copied to clipboard", icon)
                    .setAction(object : AbstractSnackBarAction() {
                        override fun mousePressed(e: MouseEvent?) {
                            snackbar.dismiss()
                        }
                    })
                    .setDuration(SnackBar.LENGTH_LONG)
                    .setSnackBarBackground(MaterialColors.DARKLY_STRONG_BLUE)
                    .setGap(60)
                    .run()
            }
        })

        // url.isVisible = false

        val imageQr = QRCode.getQrToImage(addressChoose, 300, 300)
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
