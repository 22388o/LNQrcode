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

import io.vincenzopalazzo.qr.QRCode
import org.material.component.linklabelui.model.LinkLabel
import java.awt.BorderLayout
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel

/**
 * @author https://github.com/vincenzopalazzo
 */
class QRUIContainer(private val qrcontent: String): JPanel(){

    private lateinit var label: LinkLabel
    private lateinit var qrImage: ImageIcon

    init {
        initView()
    }

    private fun initView(){
        label = LinkLabel(qrcontent)
        val imageQr = QRCode.getQrToImage(qrcontent, 300, 300)
        qrImage = ImageIcon(imageQr)

        initLayout()
        initActions()
        isVisible = true
    }

    private fun initLayout(){
        layout = BorderLayout()
        this.add(JLabel(qrImage), BorderLayout.CENTER)
    }

    private fun initActions(){}
}