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

import io.materialtheme.darkstackoverflow.DarkStackOverflowTheme
import mdlaf.MaterialLookAndFeel
import javax.swing.JFrame
import javax.swing.UIManager

/**
 * @author https://github.com/vincenzopalazzo
 */
class QRCliUI : JFrame() {

    init {
        UIManager.setLookAndFeel(MaterialLookAndFeel(DarkStackOverflowTheme()))
    }

    private object HOLDER {
        val SINGLETON = QRCliUI()
    }

    companion object {
        val instance: QRCliUI by lazy { HOLDER.SINGLETON }
    }

    internal var qrContent: String = ""
    private var eventAfterRunApp: AfterRunUIAction? = null
    private lateinit var qrUIContainer: QRUIContainer

    fun initApp() {
        if (eventAfterRunApp != null) {
            eventAfterRunApp!!.run()
            eventAfterRunApp = null
        }

        qrUIContainer = QRUIContainer(this, qrContent)
        contentPane = qrUIContainer
        pack()
        setLocationRelativeTo(null)
        defaultCloseOperation = HIDE_ON_CLOSE
        isVisible = true
    }

    fun addEventAfterInitApp(event: AfterRunUIAction) {
        this.eventAfterRunApp = event
    }
}